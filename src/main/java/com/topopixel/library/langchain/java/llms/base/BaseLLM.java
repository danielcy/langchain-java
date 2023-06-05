package com.topopixel.library.langchain.java.llms.base;

import com.topopixel.library.langchain.java.GlobalValues;
import com.topopixel.library.langchain.java.language.BaseLanguageModel;
import com.topopixel.library.langchain.java.llms.base.LLMBaseUtils.GetPromptsResult;
import com.topopixel.library.langchain.java.schema.LLMResult;
import com.topopixel.library.langchain.java.schema.PromptValue;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.var;

/**
 * LLM wrapper should take in a prompt and return a string.
 */
public abstract class BaseLLM extends BaseLanguageModel {

    private Boolean cache;

    //TODO verbose

    //TODO callback & callback_manager


    /**
     * Run the LLM on the given prompt and input.
     */
    // TODO: callback
    public LLMResult genertate(List<String> prompts, List<String> stop) {
        if (prompts == null || prompts.size() == 0) {
            // TODO: log error;
            return null;
        }

        var params = dict();
        params.put("stop", stop);
        GetPromptsResult gpr = LLMBaseUtils.getPrompts(params, prompts);
        Boolean disregardCache = cache != null && !cache;
        // TODO: callback manager

        // TODO: cache
        if (!GlobalValues.llmCache.isPresent() || disregardCache) {
            // TODO: callback manager
            LLMResult output;
            try {
                output = internalGenerate(prompts, stop);
            } catch (Exception e) {
                // TODO: callback manager
                return null;
            }
            return output;
        }

        // TODO: missing prompts

        return null;
    }

    protected abstract LLMResult internalGenerate(List<String> prompts, List<String> stop);

    private Map<String, Object> identifyingParams() {
        return new HashMap<>();
    }

    /**
     * Return type of llm.
     */
    protected abstract String llmType();

    /**
     * Return a dictionary of the LLM.
     */
    public Map<String, Object> dict() {
        Map<String, Object> starterDict = identifyingParams();
        starterDict.put("_type", llmType());
        return starterDict;
    }

    @Override
    public LLMResult generatePrompt(List<PromptValue> prompts,
        List<String> stop) {
        List<String> promptStrings = prompts.stream()
            .map(PromptValue::toString).collect(Collectors.toList());
        return genertate(promptStrings, stop);
    }

    // callback manager
    public String run(String prompt, List<String> stop) {
        LLMResult response = genertate(Arrays.asList(prompt), stop);
        return response.getGenerations().get(0).get(0).getText();
    }

    public String run(String prompt) {
        return run(prompt, null);
    }

    @Override
    public String predict(String text, SortedSet<String> stop) {
        if (stop == null) {

        }
        return "";
    }
}