package com.topopixel.library.langchain.java.prompts.base;

import com.topopixel.library.langchain.java.schema.*;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class StringPromptValue extends PromptValue {

    private String text;

    @Override
    public String toString() {
        return text;
    }

    @Override
    public List<BaseMessage> toMessages() {
        return Arrays.asList(new HumanMessage(text));
    }
}
