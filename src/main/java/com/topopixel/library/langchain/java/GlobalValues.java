package com.topopixel.library.langchain.java;

import com.topopixel.library.langchain.java.cache.BaseCache;
import java.util.Optional;

public class GlobalValues {

    public static Optional<BaseCache> llmCache = Optional.empty();
}
