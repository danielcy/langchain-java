package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackManager;
import com.topopixel.library.langchain.java.schema.BaseMessage;
import java.util.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CallbackManager extends BaseCallbackManager {

    public CallbackManager(List<BaseCallbackHandler> handlers) {
        super(handlers);
    }

    public CallbackManager(
        List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers
    ) {
        super(handlers, inheritableHandlers);
    }

    public CallbackManager(
        List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers,
        UUID parentRunId
    ) {
        super(handlers, inheritableHandlers, parentRunId);
    }

    public CallbackManagerForLLMRun onLLMStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, Object... kwargs) {
        if (runId == null) {
            runId = UUID.randomUUID();
        }
        CallbackManagerUtils.handleEvent(getHandlers(), "onLLMStart",
            "isIgnoreLLM", serialized, prompts, runId, getParentRunId(), kwargs);
        return new CallbackManagerForLLMRun(runId, getHandlers(), getInheritableHandlers(),
            getParentRunId());
    }

    public CallbackManagerForLLMRun onChatModelStart(List<BaseMessage> messages, Map<String, Object> serialized,
        UUID runId, Object... kwargs) {
        if (runId == null) {
            runId = UUID.randomUUID();
        }
        CallbackManagerUtils.handleEvent(getHandlers(), "onChatModelStart",
            "isIgnoreChatModel", serialized, messages, runId,
            getParentRunId(), kwargs);
        return new CallbackManagerForLLMRun(runId, getHandlers(), getInheritableHandlers(),
            getParentRunId());
    }

    public CallbackManagerForLLMRun onChainStart(Map<String, Object> serialized, Map<String, Object> inputs,
        UUID runId, Object... kwargs) {
        if (runId == null) {
            runId = UUID.randomUUID();
        }
        CallbackManagerUtils.handleEvent(getHandlers(), "onChainStart",
            "isIgnoreChain", serialized, inputs, runId,
            getParentRunId(), kwargs);
        return new CallbackManagerForLLMRun(runId, getHandlers(), getInheritableHandlers(),
            getParentRunId());
    }

    public CallbackManagerForLLMRun onToolStart(Map<String, Object> serialized, String inputStr,
        UUID runId, Object... kwargs) {
        if (runId == null) {
            runId = UUID.randomUUID();
        }
        CallbackManagerUtils.handleEvent(getHandlers(), "onToolStart",
            "isIgnoreAgent", serialized, inputStr, runId,
            getParentRunId(), kwargs);
        return new CallbackManagerForLLMRun(runId, getHandlers(), getInheritableHandlers(),
            getParentRunId());
    }

    public static CallbackManager configure(List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks, boolean verbose) {
        return CallbackManagerUtils.configure(CallbackManager.class, inheritableCallbacks,
            localCallbacks, verbose);
    }
}
