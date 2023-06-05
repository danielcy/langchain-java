package com.topopixel.library.langchain.java.callbacks.base;

import com.topopixel.library.langchain.java.schema.BaseMessage;
import com.topopixel.library.langchain.java.schema.LLMResult;
import java.util.*;
import lombok.Builder;
import lombok.Data;

@Data
public class BaseCallbackHandler {

    public LLMManagerMixin llmManager = new LLMManagerMixin();
    public ChainManagerMixin chainManager = new ChainManagerMixin();
    public ToolManagerMixin toolManager = new ToolManagerMixin();
    public CallbackManagerMixin callbackManager = new CallbackManagerMixin();
    public RunManagerMixin runManagerMixin = new RunManagerMixin();

    @Builder
    public BaseCallbackHandler(LLMManagerMixin llmManager, ChainManagerMixin chainManager,
        ToolManagerMixin toolManager, CallbackManagerMixin callbackManager,
        RunManagerMixin runManagerMixin) {
        this.llmManager = llmManager != null ? llmManager : new LLMManagerMixin();
        this.chainManager = chainManager != null ? chainManager : new ChainManagerMixin();
        this.toolManager = toolManager != null ? toolManager : new ToolManagerMixin();
        this.callbackManager = callbackManager != null ? callbackManager : new CallbackManagerMixin();
        this.runManagerMixin = runManagerMixin != null ? runManagerMixin : new RunManagerMixin();
    }

    private boolean ignoreLLM = false ;
    private boolean ignoreChain = false;
    private boolean ignoreAgent = false;
    private boolean ignoreChatModel = false;

    public Object onLLMNewToken(String token, UUID runId, UUID parentRunId, Object... kwargs) {
        return llmManager.onLLMNewToken(token, runId, parentRunId, kwargs);
    }

    public Object onLLMEnd(LLMResult response, UUID runId, UUID parentRunId, Object... kwargs) {
        return llmManager.onLLMEnd(response, runId, parentRunId, kwargs);
    }

    public Object onLLMError(Exception error, UUID runId, UUID parentRunId, Object... kwargs) {
        return llmManager.onLLMError(error, runId, parentRunId, kwargs);
    }

    public Object onChainEnd(Map<String, Object> outputs, UUID runId, UUID parentRunId, Object... kwargs) {
        return chainManager.onChainEnd(outputs, runId, parentRunId, kwargs);
    }

    public Object onChainError(Exception error, UUID runId, UUID parentRunId, Object... kwargs) {
        return chainManager.onChainError(error, runId, parentRunId, kwargs);
    }

    public Object onToolEnd(String output, UUID runId, UUID parentRunId, Object... kwargs) {
        return toolManager.onToolEnd(output, runId, parentRunId, kwargs);
    }

    public Object onToolError(Exception e, UUID runId, UUID parentRunId, Object... kwargs) {
        return toolManager.onToolError(e, runId, parentRunId, kwargs);
    }

    public Object onLLMStart(Map<String, Object> serialized, List<String> prompts,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return callbackManager.onLLMStart(serialized, prompts, runId, parentRunId, kwargs);
    }

    public Object onChatModelStart(Map<String, Object> serialized, List<List<BaseMessage>> messages,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return callbackManager.onChatModelStart(serialized, messages, runId, parentRunId, kwargs);
    }

    public Object onChainStart(Map<String, Object> serialized, Map<String, Object> inputs,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return callbackManager.onChainStart(serialized, inputs, runId, parentRunId, kwargs);
    }

    public Object onToolStart(Map<String, Object> serialized, String inputStr,
        UUID runId, UUID parentRunId, Object... kwargs) {
        return callbackManager.onToolStart(serialized, inputStr, runId, parentRunId, kwargs);
    }

    public Object onText(String text, UUID runId, UUID parentRunId, Object... kwargs) {
        return runManagerMixin.onText(text, runId, parentRunId, kwargs);
    }
}
