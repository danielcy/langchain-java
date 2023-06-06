package com.topopixel.library.langchain.java.text_splitter;

import com.topopixel.library.langchain.java.schema.BaseDocumentTransformer;
import com.topopixel.library.langchain.java.schema.Document;
import com.topopixel.library.langchain.java.text_splitter.length_functions.LengthFunction;
import com.topopixel.library.langchain.java.text_splitter.length_functions.SimpleLengthFunction;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Builder;

public abstract class TextSplitter extends BaseDocumentTransformer {

    protected int chunkSize;
    protected int chunkOverlap;
    protected LengthFunction lengthFunction;
    protected boolean keepSeparator;

    protected TextSplitter() {}

    public TextSplitter(Integer chunkSize, Integer chunkOverlap,
        LengthFunction lengthFunction, Boolean keepSeparator) {
        this.chunkSize = chunkSize != null ? chunkSize : 4000;
        this.chunkOverlap = chunkOverlap != null ? chunkOverlap : 200;
        this.lengthFunction = lengthFunction != null ? lengthFunction : new SimpleLengthFunction();
        this.keepSeparator = keepSeparator != null ? keepSeparator : false;
    }

    public abstract List<String> splitText(String text);

    public List<Document> createDocuments(List<String> texts) {
        return createDocuments(texts, null);
    }

    public List<Document> createDocuments(List<String> texts, List<Map<String, Object>> metadatas) {
        List<Map<String, Object>> realMetadatas = new ArrayList<>();
        if (metadatas != null) {
            realMetadatas = metadatas;
        } else {
            realMetadatas = texts.stream().map(t -> new HashMap<String, Object>())
                .collect(Collectors.toList());
        }

        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            for (String chunk: splitText(texts.get(i))) {
                documents.add(Document.builder()
                    .pageContent(chunk)
                    .metadata(realMetadatas.get(i))
                    .build());
            }
        }
        return documents;
    }

    public List<Document> splitDocuments(Collection<Document> documents) {
        List<String> texts = new ArrayList<>();
        List<Map<String, Object>> metadatas = new ArrayList<>();
        for (Document doc: documents) {
            texts.add(doc.getPageContent());
            metadatas.add(doc.getMetadata());
        }
        return createDocuments(texts, metadatas);
    }

    protected String joinDocs(List<String> docs, String separator) {
        String text = String.join(separator, docs);
        text = text.trim();
        if ("".equals(text)) {
            return null;
        }
        return text;
    }

    protected List<String> mergeSplits(Collection<String> splits, String separator) {
        int separatorLen = lengthFunction.getLength(separator);

        List<String> docs = new ArrayList<>();
        List<String> currentDoc = new ArrayList<>();
        int total = 0;
        for (String d: splits) {
            int len = lengthFunction.getLength(d);
            int sp = currentDoc.size() > 0 ? separatorLen : 0;
            if (total + len + sp > chunkSize) {
                if (total > chunkSize) {
                    // TODO: warning
                }
                if (currentDoc.size() > 0) {
                    String doc = joinDocs(currentDoc, separator);
                    if (doc != null) {
                        docs.add(doc);
                    }
                    while (total > chunkOverlap ||
                        (total + len + sp ) > chunkSize &&
                        total > 0) {
                        total -= lengthFunction.getLength(currentDoc.get(0)) + sp;
                        currentDoc = currentDoc.subList(1, currentDoc.size()-1);
                    }
                }
            }
            currentDoc.add(d);
            total += len + sp;
        }
        String doc = joinDocs(currentDoc, separator);
        if (doc != null) {
            docs.add(doc);
        }
        return docs;
    }

    // TODO: from huggingface tokenizer

    // TODO: from tiktoken encoder

    @Override
    public List<Document> transformDocuments(List<Document> documents) {
        return splitDocuments(documents);
    }
}
