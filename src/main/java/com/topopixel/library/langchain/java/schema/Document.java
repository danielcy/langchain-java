package com.topopixel.library.langchain.java.schema;

import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    private String pageContent;
    private Map<String, Object> metadata;
}
