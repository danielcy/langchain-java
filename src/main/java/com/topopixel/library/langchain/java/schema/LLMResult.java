package com.topopixel.library.langchain.java.schema;

import java.util.List;
import java.util.Map;
import lombok.*;

/**
 * Class that contains all relevant information for an LLM Result.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LLMResult {

    /**
     * List of the things generated. This is List[List[]] because
     * each input could have multiple generations.
     */
    List<List<Generation>> generations;

    /**
     * For arbitrary LLM provider specific output.
     */
    Map<String, Object> llmOutput;
}
