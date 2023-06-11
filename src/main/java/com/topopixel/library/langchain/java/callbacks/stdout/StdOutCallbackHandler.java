package com.topopixel.library.langchain.java.callbacks.stdout;

import com.topopixel.library.langchain.java.callbacks.base.*;
import java.util.*;
import lombok.Builder;

public class StdOutCallbackHandler extends BaseCallbackHandler {

    private String color;

    @Builder(builderMethodName = "stdOutBuilder")
    public StdOutCallbackHandler(String color, LLMManagerMixin llmManager, ChainManagerMixin chainManager,
        ToolManagerMixin toolManager, CallbackManagerMixin callbackManager,
        RunManagerMixin runManagerMixin) {
        super(llmManager, chainManager, toolManager, callbackManager, runManagerMixin);
        this.color = color;
    }

    @Override
    public Object onLLMStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Object... kwargs) {
        // do nothing;
        return null;
    }

    @Override
    public Object onText(String text, UUID runId, UUID parentRunId, Object... kwargs) {
        System.out.println(text);
        return null;
    }

}
