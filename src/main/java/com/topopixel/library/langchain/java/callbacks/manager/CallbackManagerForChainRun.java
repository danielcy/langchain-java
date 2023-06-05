package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.base.ChainManagerMixin;
import java.util.*;

public class CallbackManagerForChainRun extends ChainManagerMixin {

    public RunManager runManager;

    public CallbackManagerForChainRun(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers) {
        runManager = new RunManager(runId, handlers, inheritableHandlers);
    }

    public CallbackManagerForChainRun(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers, UUID parentRunId) {
        runManager = new RunManager(runId, handlers, inheritableHandlers, parentRunId);
    }

    public CallbackManager getChild() {
        CallbackManager manager = new CallbackManager(new ArrayList<>(), runManager.getParentRunId());
        manager.setHandlers(runManager.getInheritableHandlers());
        return manager;
    }

    public void onChainEnd(Map<String, Object> outputs, Object... kwargs) {
        CallbackManagerUtils.handleEvent(runManager.getHandlers(), "onChainEnd",
            "isIgnoreChain", outputs, runManager.getRunId(),
            runManager.getParentRunId(), kwargs);
    }

    public void onChainError(Exception error, Object... kwargs) {
        CallbackManagerUtils.handleEvent(runManager.getHandlers(), "onChainError",
            "isIgnoreChain", error, runManager.getRunId(),
            runManager.getParentRunId(), kwargs);
    }

    // TODO: agent
}
