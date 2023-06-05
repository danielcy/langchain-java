package com.topopixel.library.langchain.java;

import com.topopixel.library.langchain.java.chat_models.ChatOpenAI;
import com.topopixel.library.langchain.java.llms.openai.OpenAIChatConfig;
import com.topopixel.library.langchain.java.schema.HumanMessage;
import com.topopixel.library.langchain.java.schema.SystemMessage;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        OpenAIChatConfig config = OpenAIChatConfig.builder()
            .maxTokens(1024)
            .build();
        ChatOpenAI llm = new ChatOpenAI(config);
        System.out.println(llm.run(Arrays.asList(
            new SystemMessage("你是一个专业的中译英翻译助手，会将听到的所有中文都翻译为英文"),
            new HumanMessage("我很牛逼")
        )).getContent());
    }
}
