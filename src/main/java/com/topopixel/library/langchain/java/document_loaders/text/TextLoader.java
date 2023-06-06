package com.topopixel.library.langchain.java.document_loaders.text;

import com.topopixel.library.langchain.java.document_loaders.base.BaseLoader;
import com.topopixel.library.langchain.java.schema.Document;
import java.io.*;
import java.util.*;
import lombok.Builder;

public class TextLoader extends BaseLoader {

    private String filePath;
    private String encoding;
    private boolean autodetectEncoding;

    @Builder
    public TextLoader(String filePath, String encoding, Boolean autodetectEncoding) {
        this.filePath = filePath;
        this.encoding = encoding;
        this.autodetectEncoding = autodetectEncoding != null ? autodetectEncoding : false;
    }

    @Override
    public List<Document> load() {
        String text = "";
        StringBuilder sb = new StringBuilder();

        try (FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        text = sb.toString();
        Map<String, Object> metadata = new HashMap<String, Object>() {{
            put("source", filePath);
        }};

        return Arrays.asList(Document.builder()
            .pageContent(text).metadata(metadata).build());
    }
}
