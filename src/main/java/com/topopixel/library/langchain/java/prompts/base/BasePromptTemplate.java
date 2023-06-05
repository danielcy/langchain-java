package com.topopixel.library.langchain.java.prompts.base;

import com.topopixel.library.langchain.java.schema.BaseOutputParser;
import com.topopixel.library.langchain.java.schema.PromptValue;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasePromptTemplate {

    private List<String> inputVariables;

    private BaseOutputParser parse = null;

    // TODO partial variable

    public abstract PromptValue formatPrompt(Map<String, Object> kwargs);

    // TODO: partial function

    /**
     * """Format the prompt with the inputs.
     *
     *         Args:
     *             kwargs: Any arguments to be passed to the prompt template.
     *
     *         Returns:
     *             A formatted string.
     *
     *         Example:
     *
     *         .. code-block:: python
     *
     *             prompt.format(variable1="foo")
     *         """
     */
    public abstract String format(Map<String, Object> kwargs);

    private String promptType;

    public Map<String, Object> dict(Object... kwargs) {
        Map<String, Object> result = new HashMap<>();
        result.put("_type", promptType);
        return result;
    }

    public void save(String filePath) {
        // TODO: to be implements
    }
}
