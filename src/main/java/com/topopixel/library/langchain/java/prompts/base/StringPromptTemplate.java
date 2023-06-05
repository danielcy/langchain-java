package com.topopixel.library.langchain.java.prompts.base;

import com.topopixel.library.langchain.java.schema.PromptValue;
import java.util.Map;

public abstract class StringPromptTemplate extends BasePromptTemplate {

    @Override
    public PromptValue formatPrompt(Map<String, Object> kwargs) {
        return new StringPromptValue(format(kwargs));
    }
}
