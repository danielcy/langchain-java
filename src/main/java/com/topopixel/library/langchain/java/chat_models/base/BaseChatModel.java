package com.topopixel.library.langchain.java.chat_models.base;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.manager.CallbackManager;
import com.topopixel.library.langchain.java.language.BaseLanguageModel;
import com.topopixel.library.langchain.java.schema.*;
import com.topopixel.library.langchain.java.utils.ClassUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.var;

public abstract class BaseChatModel extends BaseLanguageModel {

    private List<BaseCallbackHandler> callbacks;

    private boolean verbose = false;

    public LLMResult generate(List<List<BaseMessage>> messages, List<String> stop, List<BaseCallbackHandler> callbacks) {

        CallbackManager callbackManager = CallbackManager.configure(callbacks,
            this.callbacks, verbose);
        var runManager = callbackManager.onChatModelStart(
            messages,
            new HashMap<String, Object>() {{
                put("name", getClass().getName());
            }}, null, ClassUtils.getParams(this)
        );

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
    public LLMResult generatePrompt(List<PromptValue> prompts, List<String> stop,
        List<BaseCallbackHandler> callbacks) {
        List<List<BaseMessage>> promptMessages = prompts.stream()
            .map(PromptValue::toMessage).collect(Collectors.toList());
        return generate(promptMessages, stop, callbacks);
    }

    protected abstract ChatResult internalGenerate(List<BaseMessage> messages, List<String> stop);

    /**
     * Return type of llm.
     */
    protected abstract String llmType();

    private Map<String, Object> combineLLMOutputs(List<Map<String, Object>> llmOutputs) {
        return new HashMap<>();
    }

    public BaseMessage run(List<BaseMessage> messages, List<String> output, List<BaseCallbackHandler> callbacks) {
        Generation generation = generate(Arrays.asList(messages), output, callbacks).getGenerations().get(0).get(0);
        if (generation instanceof ChatGeneration) {
            return ((ChatGeneration) generation).getMessage();
        }
        return null;
    }

    public BaseMessage run(List<BaseMessage> messages) {
        return run(messages, null, null);
    }

    @Override
    public String predict(String text, SortedSet<String> stop) {
        if (stop == null) {

        }
        return "";
    }
}
