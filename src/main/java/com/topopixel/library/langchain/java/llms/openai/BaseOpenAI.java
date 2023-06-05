package com.topopixel.library.langchain.java.llms.openai;


import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.topopixel.library.langchain.java.callbacks.manager.CallbackManagerForLLMRun;
import com.topopixel.library.langchain.java.llms.base.BaseLLM;
import com.topopixel.library.langchain.java.llms.openai.sdk.OpenAiService;
import com.topopixel.library.langchain.java.schema.Generation;
import com.topopixel.library.langchain.java.schema.LLMResult;
import com.topopixel.library.langchain.java.utils.LangChainUtils;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
public abstract class BaseOpenAI extends BaseLLM {

    public OpenAiService service;
    public String modelName = "text-davinci-003";

    public String openaiApiKey = null;
    public String openaiApiBase = null;
    public String openaiOrganization = null;

    // configs
    private Float temperature = 0.7f;
    private Integer maxTokens = 256;
    private Float topP = 1f;
    private Float frequencyPenalty = 0f;
    private Float presencePenalty = 0f;
    private Integer n = 1;
    private Integer bestOf = 1;

    protected BaseOpenAI() {}

    protected BaseOpenAI(OpenAIConfig config) {
        // TODO: make exception when use gpt3.5 or gpt4
        validataEnvironment(config);
    }

    private void validataEnvironment(OpenAIConfig config) {
        Map<String, Object> configMap = config.toMap();
        if (openaiApiKey == null) {
            openaiApiKey = LangChainUtils.getFromDictOrEnv(configMap, "openai_api_key", "OPENAI_API_KEY", Optional.empty());
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

    private void injectConfig(OpenAIConfig config) {
        try {
            Field[] fields = OpenAIConfig.class.getDeclaredFields();
            Field[] selfFields = BaseOpenAI.class.getDeclaredFields();
            List<String> configNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
            List<String> selfNames = Arrays.stream(selfFields).map(Field::getName).collect(Collectors.toList());
            for (int i = 0; i < configNames.size(); i++) {
                String name = configNames.get(i);
                if (!selfNames.contains(name)) {
                    continue;
                }
                Field configField = fields[i];
                Field selfField = BaseOpenAI.class.getDeclaredField(name);
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
        // for now only support 1 string elem
        // TODO: support streaming
        // TODO: support sub prompts
        CompletionRequest request = CompletionRequest.builder()
            .model(modelName)
            .temperature(temperature.doubleValue())
            .maxTokens(maxTokens)
            .topP(topP.doubleValue())
            .frequencyPenalty(frequencyPenalty.doubleValue())
            .presencePenalty(presencePenalty.doubleValue())
            .n(n)
            .bestOf(bestOf)
            .prompt(prompts.get(0))
            .build();
        if (stop != null) {
            request.setStop(stop);
        }
        CompletionResult response = service.createCompletion(request);

        return createLLMResult(prompts, response);
    }

    private LLMResult createLLMResult(List<String> prompts, CompletionResult response) {
        // for now only support 1 string elem
        List<Generation> generation = response.getChoices().stream()
            .map(choice ->  Generation.builder()
                    .text(choice.getText())
                    .generationInfo(new HashMap<String, Object>() {{
                        put("finish_reason", choice.getFinish_reason());
                        put("logprobs", choice.getLogprobs());
                    }}).build()
            ).collect(Collectors.toList());
        Map<String, Object> llmOuput = new HashMap<String, Object>() {{
            put("token_usage", response.getUsage());
            put("model_name", modelName);
        }};
        return LLMResult.builder()
            .generations(Arrays.asList(generation))
            .llmOutput(llmOuput)
            .build();
    }

    @Override
    protected String llmType() {
        return "openai";
    }
}
