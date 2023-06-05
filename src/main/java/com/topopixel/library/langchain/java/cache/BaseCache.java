package com.topopixel.library.langchain.java.cache;

import com.topopixel.library.langchain.java.schema.Generation;
import java.util.List;
import java.util.Optional;

/**
 * Base interface for cache.
 */
public abstract class BaseCache {

    /**
     * Look up based on prompt and llm_string.
     */
    public abstract Optional<List<Generation>> lookup(String prompt, String llmString);

    /**
     * Update cache based on prompt and llm_string.
     */
    public abstract void update(String prompt, String llmString, List<Generation> returnVal);

    /**
     * Clear cache that can take additional keyword arguments.
     */
    public abstract void clear();
}
