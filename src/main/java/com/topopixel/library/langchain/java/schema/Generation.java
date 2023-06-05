package com.topopixel.library.langchain.java.schema;

import java.util.Map;
import lombok.*;

/**
 * Output of a single generation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Generation {

    /**
     * Generated text output.
     */
    private String text;

    /**
     * Raw generation info response from the provider
     * May include things like reason for finishing (e.g. in OpenAI)
     */
    private Map<String, Object> generationInfo;
}
