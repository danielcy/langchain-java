package com.topopixel.library.langchain.java.llms.openai;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.*;
import com.topopixel.library.langchain.java.callbacks.manager.CallbackManagerForLLMRun;
import com.topopixel.library.langchain.java.llms.base.BaseLLM;
import com.topopixel.library.langchain.java.llms.openai.sdk.OpenAiService;
import com.topopixel.library.langchain.java.schema.Generation;
import com.topopixel.library.langchain.java.schema.LLMResult;
import com.topopixel.library.langchain.java.utils.LangChainUtils;
import com.topopixel.library.langchain.java.utils.SnakeJsonUtils;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class OpenAIChat extends BaseLLM {

    public OpenAiService service;
    public String modelName = "gpt-3.5-turbo";

    public String openaiApiKey = null;
    public String openaiApiBase = null;
    public String openaiOrganization = null;

    public List<ChatMessage> prefixMessages = new ArrayList<>();

    // configs
    private Float temperature = 0.7f;
    private Integer maxTokens = 256;
    private Float topP = 1f;
    private Float frequencyPenalty = 0f;
    private Float presencePenalty = 0f;
    private Integer n = 1;

    public OpenAIChat(OpenAIChatConfig config) {
        validataEnvironment(config);
    }

    private void validataEnvironment(OpenAIChatConfig config) {
        Map<String, Object> configMap = config.toMap();
        if (openaiApiKey == null) {
            openaiApiKey = LangChainUtils
                .getFromDictOrEnv(configMap, "openai_api_key", "OPENAI_API_KEY", Optional.empty());
        }

        if (openaiApiBase == null) {
            openaiApiBase = LangChainUtils.getFromDictOrEnv(configMap, "openai_api_base", "OPENAI_API_BASE", Optional.empty());
        }

        if (openaiApiBase != null) {
            service = new OpenAiService(openaiApiKey, openaiApiBase);
        } else {
            service = new OpenAiService(openaiApiKey);
        }

        injectConfig(config);
    }

    private void injectConfig(OpenAIChatConfig config) {
        try {
            Field[] fields = OpenAIChatConfig.class.getDeclaredFields();
            Field[] selfFields = OpenAIChat.class.getDeclaredFields();
            List<String> configNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
            List<String> selfNames = Arrays.stream(selfFields).map(Field::getName).collect(Collectors.toList());
            for (int i = 0; i < configNames.size(); i++) {
                String name = configNames.get(i);
                if (!selfNames.contains(name)) {
                    continue;
                }
                Field configField = fields[i];
                Field selfField = OpenAIChat.class.getDeclaredField(name);
                selfField.setAccessible(true);
                configField.setAccessible(true);
                if (configField.get(config) == null) {
                    continue;
                }
                selfField.set(this, configField.get(config));
            }
        } catch (Exception e) {
            return;
        }

    }

    // TODO: callback manager
    @Override
    protected LLMResult internalGenerate(List<String> prompts, List<String> stop,
        CallbackManagerForLLMRun runManager) {
        List<ChatMessage> messages = prefixMessages;
        messages.add(new ChatMessage("user", prompts.get(0)));
        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model(modelName)
            .messages(messages)
            .temperature(temperature.doubleValue())
            .maxTokens(maxTokens)
            .topP(topP.doubleValue())
            .frequencyPenalty(frequencyPenalty.doubleValue())
            .presencePenalty(presencePenalty.doubleValue())
            .n(n)
            .build();
        ChatCompletionResult response = service.createChatCompletion(request);
        Map<String, Object> llmOutput = new HashMap<String, Object>() {{
           put("token_usage", response.getUsage());
           put("model_name", modelName);
        }};
        return LLMResult.builder()
            .llmOutput(llmOutput)
            .generations(Arrays.asList(Arrays.asList(
                Generation.builder()
                    .text(response.getChoices().get(0).getMessage().getContent())
                    .build()
            )))
            .build();
    }



    @Override
    protected String llmType() {
        return "openai-chat";
    }
}
