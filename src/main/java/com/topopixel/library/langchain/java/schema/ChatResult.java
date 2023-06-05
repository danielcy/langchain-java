package com.topopixel.library.langchain.java.schema;

import java.util.List;
import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResult {

    private List<ChatGeneration> generations;

    private Map<String, Object> llmOutput;
}
