package com.topopixel.library.langchain.java.callbacks.base;

public class BaseCallbackHandler {

    public LLMManagerMixin llmManager = new LLMManagerMixin();
    public ChainManagerMixin chainManager = new ChainManagerMixin();
    public ToolManagerMixin toolManager = new ToolManagerMixin();
    public CallbackManagerMixin callbackManager = new CallbackManagerMixin();
    public RunManagerMixin runManagerMixin = new RunManagerMixin();

    public boolean ignoreLLM = false;
    public boolean ignoreChain = false;
    public boolean ignoreAgent = false;
    public boolean ignoreChatModel = false;
}
