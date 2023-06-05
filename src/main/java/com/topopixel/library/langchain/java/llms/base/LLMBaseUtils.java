package com.topopixel.library.langchain.java.llms.base;

import com.topopixel.library.langchain.java.GlobalValues;
import com.topopixel.library.langchain.java.schema.Generation;
import java.util.*;
import lombok.*;

public class LLMBaseUtils {

    /**
     * Get prompts that are already cached.
     */
    public static GetPromptsResult getPrompts(Map<String, Object> params, List<String> prompts) {
        String llmString = genStringParams(params);
        List<String> missingPrompts = new ArrayList<>();
        List<Integer> missingPromptIdxs = new ArrayList<>();
        Map<Integer, List<Generation>> existingPrompts = new HashMap<>();
        for (int i = 0; i < prompts.size(); i++) {
            String prompt = prompts.get(i);
            if (GlobalValues.llmCache.isPresent()) {
                var cacheVal = GlobalValues.llmCache.get().lookup(prompt, llmString);
                if (cacheVal.isPresent()) {
                    existingPrompts.put(i, cacheVal.get());
                } else {
                    missingPrompts.add(prompt);
                    missingPromptIdxs.add(i);
                }
            }
        }
        return GetPromptsResult.builder()
            .existingPrompts(existingPrompts)
            .missingPrompts(missingPrompts)
            .missingPromptIdxs(missingPromptIdxs)
            .llmString(llmString)
            .build();
    }

    private static String genStringParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("[");
        for (var entries: params.entrySet()) {
            sb.append("(");
            sb.append(entries.getKey());
            sb.append(", ");
            sb.append(entries.getValue());
            sb.append("),");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPromptsResult {
        private Map<Integer, List<Generation>> existingPrompts;
        private String llmString;
        private List<Integer> missingPromptIdxs;
        private List<String> missingPrompts;
    }
}
