package com.topopixel.library.langchain.java.schema;

import com.topopixel.library.langchain.java.exception.NotImplementedException;
import java.util.HashMap;
import java.util.Map;

/**
 * """Class to parse the output of an LLM call.
 *
 *     Output parsers help structure language model responses.
 *     """
 */
public abstract class BaseOutputParser<T> {

    /**
     *         """Parse the output of an LLM call.
     *
     *         A method which takes in a string (assumed output of a language model )
     *         and parses it into some structure.
     *
     *         Args:
     *             text: output of language model
     *
     *         Returns:
     *             structured output
     *         """
     */
    public abstract T parse(String test);

    public Object parseWithPrompt(String completion, PromptValue prompt) {
        return parse(completion);
    }

    public String getFormatInstructions() {
        throw new NotImplementedException("");
    }

    private String type;

    public Map<String, Object> dict(Object... kwargs) {
        Map<String, Object> result = new HashMap<>();
        result.put("_type", type);
        return result;
    }
}
