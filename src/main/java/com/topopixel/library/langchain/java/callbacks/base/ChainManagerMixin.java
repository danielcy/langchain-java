package com.topopixel.library.langchain.java.callbacks.base;

import java.util.Map;
import java.util.UUID;

public class ChainManagerMixin {

    public Object onChainEnd(Map<String, Object> outputs, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    public Object onChainError(Exception error, UUID runId, UUID parentRunId, Object... kwargs) {
        return null;
    }

    // TODO: agent action
    public Object onAgentAction() {
        return null;
    }

    // TODO: agent finish
    public Object onAgentFinish() {
        return null;
    }
}
