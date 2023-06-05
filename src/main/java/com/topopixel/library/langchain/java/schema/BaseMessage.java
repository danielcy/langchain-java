package com.topopixel.library.langchain.java.schema;

import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMessage {

    private String content;
    private Map<String, Object> additionalKwargs;

    /**
     * Type of the message, used for serialization.
     */
    public abstract String type();
}
