package com.topopixel.library.langchain.java.callbacks.base;

import java.util.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Mixin for callback manager.
 */
public class CallbackManagerMixin {

    public Object onLLMStart(Map<String, Object> serialized, List<String> prompts,
         UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onChatModelStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Object... kwargs) {
        throw new NotImplementedException();
    }

    public Object onChainStart(Map<String, Object> serialized, Map<String, Object> inputs,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onToolStart(Map<String, Object> serialized, String inputStr,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }
}
