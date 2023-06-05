package com.topopixel.library.langchain.java.callbacks.base;

import com.topopixel.library.langchain.java.exception.NotImplementedException;
import com.topopixel.library.langchain.java.schema.BaseMessage;
import java.util.*;

/**
 * Mixin for callback manager.
 */
public class CallbackManagerMixin {

    public Object onLLMStart(Map<String, Object> serialized, List<String> prompts,
         UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onChatModelStart(Map<String, Object> serialized, List<List<BaseMessage>> messages,
        UUID runId, UUID parentRunId, Object... kwargs) {
        throw new NotImplementedException("not implemented onChatModelStart");
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
