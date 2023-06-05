package com.topopixel.library.langchain.java.schema;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIMessage extends BaseMessage {

    private Boolean example = false;

    public AIMessage(String content) {
        setContent(content);
    }

    @Override
    public String type() {
        return "ai";
    }
}
