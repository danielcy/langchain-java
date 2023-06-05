package com.topopixel.library.langchain.java.prompts;

import com.topopixel.library.langchain.java.prompts.base.StringPromptTemplate;
import java.util.*;
import lombok.*;

@Getter
@Setter
public class PromptTemplate extends StringPromptTemplate {

    private List<String> inputVariables = new ArrayList<>();
    private String template;
    private String templateFormat;
    private boolean validateTemplate = true;

    private String promptType = "prompt";

    public PromptTemplate(String template) {
        this.template = template;
    }

    public PromptTemplate(List<String> inputVariables, String template) {
        this.inputVariables = inputVariables;
        this.template = template;
    }

    /**
     *         """Format the prompt with the inputs.
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
    @Override
    public String format(Map<String, Object> kwargs) {
        String result = template;
        for (var entry: kwargs.entrySet()) {
            if (inputVariables.contains(entry.getKey())) {
                String searchWord = "{" + entry.getKey() + "}";
                String replaceValue = String.valueOf(entry.getValue());
                result = result.replace(searchWord, replaceValue);
            }
        }
        return result;
    }
}
