package com.topopixel.library.langchain.java.language;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.schema.LLMResult;
import com.topopixel.library.langchain.java.schema.PromptValue;
import java.util.List;
import java.util.SortedSet;

public abstract class BaseLanguageModel {

    /**
     * Take in a list of prompt values and return an LLMResult.
     */
    //TODO: callbacks
    public abstract LLMResult generatePrompt(List<PromptValue> prompts,
        List<String> stop, List<BaseCallbackHandler> callbacks);

    //TODO: async def agenerate_prompt

    public abstract String predict(String text, SortedSet<String> stop);
}
