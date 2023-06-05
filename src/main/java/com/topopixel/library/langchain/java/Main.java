package com.topopixel.library.langchain.java;

import com.topopixel.library.langchain.java.chat_models.ChatOpenAI;
import com.topopixel.library.langchain.java.llms.openai.OpenAIChat;
import com.topopixel.library.langchain.java.llms.openai.OpenAIChatConfig;
import com.topopixel.library.langchain.java.schema.HumanMessage;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        OpenAIChatConfig config = OpenAIChatConfig.builder()
            .maxTokens(1024)
            .build();
        ChatOpenAI llm = new ChatOpenAI(config);
        System.out.println(llm.run(Arrays.asList(new HumanMessage("如何评价人工智能"))).getContent());
    }
}
