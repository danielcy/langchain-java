package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.base.LLMManagerMixin;
import com.topopixel.library.langchain.java.schema.LLMResult;
import java.util.List;
import java.util.UUID;

public class CallbackManagerForLLMRun extends LLMManagerMixin {

    public RunManager runManager;

    public CallbackManagerForLLMRun(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers) {
        runManager = new RunManager(runId, handlers, inheritableHandlers);
    }

    public CallbackManagerForLLMRun(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers, UUID parentRunId) {
        runManager = new RunManager(runId, handlers, inheritableHandlers, parentRunId);
    }

    public void onLLMNewToken(String token, Object...kwargs) {
        CallbackManagerUtils.handleEvent(runManager.getHandlers(), "onLLMNewToken",
            "isIgnoreLLM", token, runManager.getRunId(), runManager.getParentRunId(),
            kwargs);
    }

    public void onLLMEnd(LLMResult response, Object...kwargs) {
        CallbackManagerUtils.handleEvent(runManager.getHandlers(), "onLLMEnd",
            "isIgnoreLLM", response, runManager.getRunId(), runManager.getParentRunId(),
            kwargs);
    }

    public void onLLMError(Exception error, Object...kwargs) {
        CallbackManagerUtils.handleEvent(runManager.getHandlers(), "onLLMError",
            "isIgnoreLLM", error, runManager.getRunId(), runManager.getParentRunId(),
            kwargs);
    }
}
