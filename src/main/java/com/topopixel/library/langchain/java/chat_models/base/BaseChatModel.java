package com.topopixel.library.langchain.java.chat_models.base;

import com.topopixel.library.langchain.java.language.BaseLanguageModel;
import com.topopixel.library.langchain.java.schema.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseChatModel extends BaseLanguageModel {

    public LLMResult generate(List<List<BaseMessage>> messages, List<String> stop) {

        List<ChatResult> results = new ArrayList<>();
        for (List<BaseMessage> m: messages) {
            results.add(internalGenerate(m, stop));
        }
        Map<String, Object> llmOutput = combineLLMOutputs(results.stream().map(ChatResult::getLlmOutput).collect(
            Collectors.toList()));
        List<List<Generation>> generations = new ArrayList<>();
        for (ChatResult result: results) {
            List<Generation> list = new ArrayList<>(result.getGenerations());
            generations.add(list);
        }
        return LLMResult.builder()
            .generations(generations)
            .llmOutput(llmOutput)
            .build();
    }

    @Override
    public LLMResult generatePrompt(List<PromptValue> prompts, List<String> stop) {
        List<List<BaseMessage>> promptMessages = prompts.stream()
            .map(PromptValue::toMessage).collect(Collectors.toList());
        return generate(promptMessages, stop);
    }

    protected abstract ChatResult internalGenerate(List<BaseMessage> messages, List<String> stop);

    /**
     * Return type of llm.
     */
    protected abstract String llmType();

    private Map<String, Object> combineLLMOutputs(List<Map<String, Object>> llmOutputs) {
        return new HashMap<>();
    }

    public BaseMessage run(List<BaseMessage> messages, List<String> output) {
        Generation generation = generate(Arrays.asList(messages), output).getGenerations().get(0).get(0);
        if (generation instanceof ChatGeneration) {
            return ((ChatGeneration) generation).getMessage();
        }
        return null;
    }

    public BaseMessage run(List<BaseMessage> messages) {
        return run(messages, null);
    }

    @Override
    public String predict(String text, SortedSet<String> stop) {
        if (stop == null) {

        }
        return "";
    }
}
