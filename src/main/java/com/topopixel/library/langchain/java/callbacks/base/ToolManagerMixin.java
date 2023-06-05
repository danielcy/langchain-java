package com.topopixel.library.langchain.java.callbacks.base;

import java.util.Map;
import java.util.UUID;

public class ToolManagerMixin {

    public Object onToolEnd(String output, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onToolError(Exception e, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }
}
