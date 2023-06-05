package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import java.util.List;
import java.util.UUID;

public class RunManager extends BaseRunManager {

    public RunManager(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers) {
        super(runId, handlers, inheritableHandlers);
    }

    public RunManager(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers, UUID parentRunId) {
        super(runId, handlers, inheritableHandlers, parentRunId);
    }

    public Object onText(String text, Object... kwargs) {
        CallbackManagerUtils.handleEvent(getHandlers(), "onText",
            null, text, getRunId(), getParentRunId(), kwargs);
        return null;
    }
}
