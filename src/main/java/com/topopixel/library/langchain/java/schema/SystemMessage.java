package com.topopixel.library.langchain.java.schema;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SystemMessage extends BaseMessage {

    public SystemMessage(String content) {
        setContent(content);
    }

    @Override
    public String type() {
        return "system";
    }
}
