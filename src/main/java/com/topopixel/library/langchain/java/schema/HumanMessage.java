package com.topopixel.library.langchain.java.schema;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumanMessage extends BaseMessage {

    private Boolean example = false;

    public HumanMessage(String content) {
        setContent(content);
    }

    @Override
    public String type() {
        return "human";
    }
}
