package com.topopixel.library.langchain.java.document_loaders.base;

import com.topopixel.library.langchain.java.schema.Document;
import com.topopixel.library.langchain.java.text_splitter.RecursiveCharacterTextSplitter;
import com.topopixel.library.langchain.java.text_splitter.TextSplitter;
import java.util.List;

/**
 *     """Interface for loading documents.
 *
 *     Implementations should implement the lazy-loading method using generators
 *     to avoid loading all documents into memory at once.
 *
 *     The `load` method will remain as is for backwards compatibility, but its
 *     implementation should be just `list(self.lazy_load())`.
 *     """
 */
public abstract class BaseLoader {

    public abstract List<Document> load();

    public List<Document> loadAndSplit(TextSplitter textSplitter) {
        TextSplitter mTextSplitter = RecursiveCharacterTextSplitter.rechBuilder().build();
        if (textSplitter != null) {
            mTextSplitter = textSplitter;
        }
        List<Document> docs = load();
        return mTextSplitter.splitDocuments(docs);
    }

    //TODO" lazy load
}
