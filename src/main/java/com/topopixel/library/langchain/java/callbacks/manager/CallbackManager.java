package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackManager;
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

    @Override
    public Object onLLMStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        return null;
    }

    @Override
    public Object onChatModelStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        throw new NotImplementedException();
    }

    @Override
    public Object onChainStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        return null;
    }

    @Override
    public Object onToolStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        return null;
    }
}
