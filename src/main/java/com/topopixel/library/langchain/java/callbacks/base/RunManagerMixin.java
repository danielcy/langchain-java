package com.topopixel.library.langchain.java.callbacks.base;

import java.util.Map;
import java.util.UUID;

public class RunManagerMixin {

    public Object onText(String text, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
        return null;
    }
}
