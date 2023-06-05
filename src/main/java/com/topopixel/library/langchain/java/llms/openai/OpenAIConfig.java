package com.topopixel.library.langchain.java.llms.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.topopixel.library.langchain.java.utils.SnakeJsonUtils;
import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAIConfig {

    private String modelName;

    private Float temperature;

    private Integer maxTokens;

    private Float topP;

    private Float frequencyPenalty;

    private Float presencePenalty;

    private Integer n;

    private Integer bestOf;

    public Map<String, Object> toMap() {
        return SnakeJsonUtils.convert(this, new TypeReference<Map<String, Object>>() {
        });
    }
}
