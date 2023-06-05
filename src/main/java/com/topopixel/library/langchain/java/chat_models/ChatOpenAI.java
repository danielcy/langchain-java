package com.topopixel.library.langchain.java.chat_models;

import com.theokanning.openai.completion.chat.*;
import com.topopixel.library.langchain.java.chat_models.base.BaseChatModel;
import com.topopixel.library.langchain.java.chat_models.base.ChatModelBaseUtils;
import com.topopixel.library.langchain.java.llms.openai.OpenAIChatConfig;
import com.topopixel.library.langchain.java.llms.openai.sdk.OpenAiService;
import com.topopixel.library.langchain.java.schema.*;
import com.topopixel.library.langchain.java.utils.LangChainUtils;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import lombok.var;

public class ChatOpenAI extends BaseChatModel {

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

    public ChatOpenAI(OpenAIChatConfig config) {
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
            Field[] selfFields = ChatOpenAI.class.getDeclaredFields();
            List<String> configNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
            List<String> selfNames = Arrays.stream(selfFields).map(Field::getName).collect(Collectors.toList());
            for (int i = 0; i < configNames.size(); i++) {
                String name = configNames.get(i);
                if (!selfNames.contains(name)) {
                    continue;
                }
                Field configField = fields[i];
                Field selfField = ChatOpenAI.class.getDeclaredField(name);
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

    @Override
    protected ChatResult internalGenerate(List<BaseMessage> messages, List<String> stop) {
        List<ChatMessage> chatMessages = messages.stream()
            .map(ChatModelBaseUtils::convertToChatMessage).collect(Collectors.toList());
        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model(modelName)
            .messages(chatMessages)
            .temperature(temperature.doubleValue())
            .maxTokens(maxTokens)
            .topP(topP.doubleValue())
            .frequencyPenalty(frequencyPenalty.doubleValue())
            .presencePenalty(presencePenalty.doubleValue())
            .n(n)
            .build();
        ChatCompletionResult response = service.createChatCompletion(request);
        List<ChatGeneration> generations = new ArrayList<>();
        for (var res: response.getChoices()) {
            BaseMessage message = ChatModelBaseUtils.convertToBaseMessage(res.getMessage());
            generations.add(ChatGeneration.chatBuilder().message(message).text(message.getContent()).build());
        }
        Map<String, Object> llmOutput = new HashMap<String, Object>() {{
            put("token_usage", response.getUsage());
            put("model_name", modelName);
        }};
        return ChatResult.builder()
            .generations(generations)
            .llmOutput(llmOutput)
            .build();
    }

    @Override
    protected String llmType() {
        return "openai-chat";
    }
}
