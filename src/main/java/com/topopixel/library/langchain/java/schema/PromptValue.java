package com.topopixel.library.langchain.java.schema;

import java.util.List;

public abstract class PromptValue {

    /**
     * Return prompt as string.
     */
    @Override
    public abstract String toString();

    /**
     * Return prompt as messages.
     */
    public abstract List<BaseMessage> toMessage();
}
