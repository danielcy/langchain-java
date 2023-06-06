package com.topopixel.library.langchain.java.schema;

import java.util.List;

public abstract class BaseDocumentTransformer {

    public abstract List<Document> transformDocuments(List<Document> documents);
}
