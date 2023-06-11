package com.topopixel.library.langchain.java.chain.llm;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.manager.CallbackManagerForChainRun;
import com.topopixel.library.langchain.java.chain.base.Chain;
import com.topopixel.library.langchain.java.exception.ValueErrorException;
import com.topopixel.library.langchain.java.language.BaseLanguageModel;
import com.topopixel.library.langchain.java.prompts.base.BasePromptTemplate;
import com.topopixel.library.langchain.java.schema.LLMResult;
import com.topopixel.library.langchain.java.schema.PromptValue;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.*;

public class LLMChain extends Chain {

    private BasePromptTemplate prompt;

    private BaseLanguageModel llm;

    private String outputKey;

    @Builder
    public LLMChain(BasePromptTemplate prompt, BaseLanguageModel llm,
        String outputKey, boolean verbose) {
        this.prompt = prompt;
        this.llm = llm;
        this.outputKey = outputKey != null ? outputKey : "text";
        this.verbose = verbose;
    }

    @Override
    public List<String> getInputKeys() {
        return prompt.getInputVariables();
    }

    @Override
    public List<String> getOutputKeys() {
        return Arrays.asList(outputKey);
    }

    @Override
    public Map<String, Object> internalCall(Map<String, Object> inputs,
        CallbackManagerForChainRun runManager) {
        LLMResult response = generate(Arrays.asList(inputs), runManager);
        return createOutput(response).get(0);
    }

    public LLMResult generate(List<Map<String, Object>> inputList, CallbackManagerForChainRun runManager) {
        PromptInput pi = prepPrompt(inputList, runManager);
        if (runManager != null) {
            return llm.generatePrompt(pi.getPrompts(), pi.getStop(), runManager.getChild().getHandlers());
        } else {
            return llm.generatePrompt(pi.getPrompts(), pi.getStop(), null);
        }
    }



    public List<Map<String, Object>> createOutput(LLMResult response) {
        return response.getGenerations().stream().
            map(g -> new HashMap<String, Object>() {{put(outputKey, g.get(0).getText());}})
            .collect(Collectors.toList());
    }

    public PromptInput prepPrompt(List<Map<String, Object>> inputList, CallbackManagerForChainRun runManager) {
        List<String> stop = null;
        if (inputList.get(0).containsKey("stop")) {
            stop = (List<String>) inputList.get(0).get("stop");
        }
        List<PromptValue> prompts = new ArrayList<>();
        for (Map<String, Object> inputs: inputList) {
            Map<String, Object> selectedInputs = inputs.keySet().stream()
                .filter(k -> this.prompt.getInputVariables().contains(k))
                .collect(Collectors.toMap(Function.identity(), inputs::get));
            PromptValue prompt = this.prompt.formatPrompt(selectedInputs);
            // TODO: colored text
            String oText = "Prompt after formatting: " + prompt.toString();
            if (runManager != null) {
                runManager.runManager.onText(oText, "\n", verbose);
            }
            if (inputs.containsKey("stop") && stop != inputs.get("stop")) {
                throw new ValueErrorException("If `stop` is present in any inputs, should be present in all.");
            }
            prompts.add(prompt);
        }
        return new PromptInput(prompts, stop);
    }

    public String predict(Map<String, Object> inputs, List<BaseCallbackHandler> callbacks) {
        return String.valueOf(call(inputs, callbacks).get(outputKey));
    }

    @Data
    @AllArgsConstructor
    private static class PromptInput {
        private List<PromptValue> prompts;
        private List<String> stop;
    }
}
