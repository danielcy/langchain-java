package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.base.RunManagerMixin;
import java.util.*;
import lombok.*;

@Getter
@Setter
public class BaseRunManager extends RunManagerMixin {

    private UUID runId;
    private List<BaseCallbackHandler> handlers;
    private List<BaseCallbackHandler> inheritableHandlers;
    private UUID parentRunId;

    public BaseRunManager(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers) {
        this(runId, handlers, inheritableHandlers, null);
    }

    public BaseRunManager(UUID runId, List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers, UUID parentRunId) {
        this.runId = runId;
        this.handlers = handlers;
        this.inheritableHandlers = inheritableHandlers;
        this.parentRunId = parentRunId;
    }

    public static BaseRunManager getNoopManager() {
        return new BaseRunManager(UUID.randomUUID(), new ArrayList<>(), new ArrayList<>());
    }
}
