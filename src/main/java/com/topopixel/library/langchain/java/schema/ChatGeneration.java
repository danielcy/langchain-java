package com.topopixel.library.langchain.java.schema;

import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
public class ChatGeneration extends Generation {

    private String text = "";
    private BaseMessage message;

    @Builder(builderMethodName = "chatBuilder")
    public ChatGeneration(String text, BaseMessage message) {
        this.text = text;
        this.message = message;
    }
}
