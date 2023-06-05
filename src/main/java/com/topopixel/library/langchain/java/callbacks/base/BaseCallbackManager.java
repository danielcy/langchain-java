package com.topopixel.library.langchain.java.callbacks.base;

import java.util.*;
import lombok.Getter;

/**
 * Base callback manager that can be used to handle callbacks from LangChain.
 */
@Getter
public class BaseCallbackManager extends CallbackManagerMixin {

    private List<BaseCallbackHandler> handlers;
    private List<BaseCallbackHandler> inheritableHandlers;
    private UUID parentRunId;

    private boolean isAsync = false;

    public BaseCallbackManager(List<BaseCallbackHandler> handlers) {
        this(handlers, null, null);
    }

    public BaseCallbackManager(
        List<BaseCallbackHandler> handlers,
        List<BaseCallbackHandler> inheritableHandlers,
        UUID parentRunId
    ) {
        this.handlers = handlers;
        this.inheritableHandlers = inheritableHandlers == null ? new ArrayList<>() : inheritableHandlers;
        this.parentRunId = parentRunId;
    }

    public void addHandler(BaseCallbackHandler handler) {
        addHandler(handler, true);
    }

    public void addHandler(BaseCallbackHandler handler, boolean inherit) {
        handlers.add(handler);
        if (inherit) {
            inheritableHandlers.add(handler);
        }
    }

    public void removeHandler(BaseCallbackHandler handler) {
        handlers.remove(handler);
        inheritableHandlers.remove(handler);
    }

    public void setHandlers(List<BaseCallbackHandler> handlers) {
        setHandlers(handlers, true);
    }

    public void setHandlers(List<BaseCallbackHandler> handlers, boolean inherit) {
        this.handlers = new ArrayList<>();
        this.inheritableHandlers = new ArrayList<>();
        for (BaseCallbackHandler handler: handlers) {
            addHandler(handler, inherit);
        }
    }

    public void setHandler(BaseCallbackHandler handler) {
        setHandler(handler, true);
    }

    public void setHandler(BaseCallbackHandler handler, boolean inherit) {
        setHandlers(Arrays.asList(handler), inherit);
    }
}
