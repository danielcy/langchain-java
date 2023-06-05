package com.topopixel.library.langchain.java.callbacks.base;

import com.topopixel.library.langchain.java.schema.LLMResult;
import java.util.Map;
import java.util.UUID;

/**
 * Mixin for LLM callbacks.
 */
public class LLMManagerMixin {

    public Object onLLMNewToken(String token, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onLLMEnd(LLMResult response, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onLLMError(Exception error, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }
}
