package com.topopixel.library.langchain.java.chain.sql_database;

import com.topopixel.library.langchain.java.callbacks.manager.CallbackManagerForChainRun;
import com.topopixel.library.langchain.java.callbacks.manager.RunManager;
import com.topopixel.library.langchain.java.chain.base.Chain;
import com.topopixel.library.langchain.java.chain.llm.LLMChain;
import com.topopixel.library.langchain.java.exception.ValueErrorException;
import com.topopixel.library.langchain.java.language.BaseLanguageModel;
import com.topopixel.library.langchain.java.prompts.base.BasePromptTemplate;
import com.topopixel.library.langchain.java.sql_database.SQLDatabase;
import java.util.*;
import lombok.Builder;

/**
 *     """Chain for interacting with SQL Database.
 *
 *     Example:
 *         .. code-block:: python
 *
 *             from langchain import SQLDatabaseChain, OpenAI, SQLDatabase
 *             db = SQLDatabase(...)
 *             db_chain = SQLDatabaseChain.from_llm(OpenAI(), db)
 *     """
 */
public class SQLDatabaseChain extends Chain {

    public static final String INTERMEDIATE_STEPS_KEY = "intermediate_steps";

    private LLMChain llmChain;
    private BaseLanguageModel llm;
    private SQLDatabase sqlDatabase;
    private BasePromptTemplate prompt;
    private Integer topK;
    private String inputKey;
    private String outputKey;
    private Boolean returnIntermediateSteps;
    private Boolean returnDirect;
    private Boolean useQueryChecker;
    private BasePromptTemplate queryCheckerPrompt;

    @Builder
    public SQLDatabaseChain(LLMChain llmChain, BaseLanguageModel llm, SQLDatabase sqlDatabase,
        BasePromptTemplate prompt, Integer topK, String inputKey, String outputKey,
        Boolean returnIntermediateSteps, Boolean returnDirect, Boolean useQueryChecker,
        BasePromptTemplate queryCheckerPrompt) {

        if (sqlDatabase == null) {
            throw new ValueErrorException("SqlDatabase cannot be null");
        }

        if (llmChain == null && llm == null) {
            throw new ValueErrorException("llmChain and llm cannot both be null");
        }

        if (llmChain == null && llm != null) {
            // TODO: warn deprecate
            // TODO: support mysql only for now
            prompt = SQLDatabasePrompts.MYSQL_PROMPT;
            llmChain = LLMChain.builder()
                .llm(llm)
                .prompt(prompt)
                .build();
        }

        this.llm = llm;
        this.llmChain = llmChain;
        this.sqlDatabase = sqlDatabase;
        this.prompt = prompt;

        this.topK = topK != null ? topK : 5;
        this.inputKey = inputKey != null ? inputKey : "query";
        this.outputKey = outputKey != null ? outputKey : "output";
        this.returnIntermediateSteps = returnIntermediateSteps != null ? returnIntermediateSteps : false;
        this.returnDirect = returnDirect != null ? returnDirect : false;
        this.useQueryChecker = useQueryChecker != null ? useQueryChecker : false;
        this.queryCheckerPrompt = queryCheckerPrompt;
    }

    @Override
    public List<String> getInputKeys() {
        return Arrays.asList(inputKey);
    }

    @Override
    public List<String> getOutputKeys() {
        if (!returnIntermediateSteps) {
            return Arrays.asList(outputKey);
        } else {
            return Arrays.asList(outputKey, INTERMEDIATE_STEPS_KEY);
        }
    }

    @Override
    public Map<String, Object> internalCall(Map<String, Object> inputs,
        CallbackManagerForChainRun runManager) {

        runManager = runManager != null ? runManager : CallbackManagerForChainRun.getNoopManager();

        String inputText = String.valueOf(inputs.get(inputKey)) + "\nSQLQuery:";
        runManager.runManager.onText(inputText, verbose);
        List<String> tableNamesToUse = (List<String>) inputs.getOrDefault("table_names_to_use", null);
        String tableInfo = sqlDatabase.getTableInfo(tableNamesToUse);
        Map<String, Object> llmInputs = new HashMap<String, Object>() {{
           put("top_k", topK);
           //TODO dialect support
           put("table_info", tableInfo);
           put("stop", Arrays.asList("\nSQLResult:"));
        }};
        llmInputs.put("input", inputText);;
        List<Object> intermediateStep = new ArrayList<>();
        String result = "";
        try {
            intermediateStep.add(llmInputs);
            String sqlCmd = llmChain.predict(llmInputs, runManager.getChild().getHandlers()).trim();
            if (!useQueryChecker) {
                runManager.runManager.onText(sqlCmd, verbose);
                intermediateStep.add(sqlCmd);
                intermediateStep.add(new HashMap<String, String>() {{
                    put("sql_cmd", sqlCmd);
                }});
                result = sqlDatabase.run(sqlCmd);
                intermediateStep.add(result);
            } else {
                // TODO: query checker
            }

            runManager.runManager.onText("\nSQLResult: ", verbose);
            runManager.runManager.onText(result, verbose);
            String finalResult;
            if (returnDirect) {
                finalResult = result;
            } else {
                runManager.runManager.onText("\nAnswer:", verbose);
                inputText += sqlCmd + "\nSQLResult: " + result + "\nAnswer:";
                llmInputs.put("input", inputText);
                intermediateStep.add(llmInputs);
                finalResult = llmChain.predict(llmInputs, runManager.getChild().getHandlers());
                intermediateStep.add(finalResult);
                runManager.runManager.onText(finalResult, verbose);
            }
            Map<String, Object> chainResult = new HashMap<String, Object>() {{
               put(outputKey, finalResult);
            }};
            if (returnIntermediateSteps) {
                chainResult.put(INTERMEDIATE_STEPS_KEY, intermediateStep);
            }
            return chainResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValueErrorException("failed to exec sql chain");
        }
    }
}
