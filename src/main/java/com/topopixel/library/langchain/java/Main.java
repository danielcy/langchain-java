package com.topopixel.library.langchain.java;

import com.topopixel.library.langchain.java.chain.llm.LLMChain;
import com.topopixel.library.langchain.java.chain.sql_database.SQLDatabaseChain;
import com.topopixel.library.langchain.java.chain.sql_database.SQLDatabasePrompts;
import com.topopixel.library.langchain.java.chat_models.ChatOpenAI;
import com.topopixel.library.langchain.java.llms.openai.*;
import com.topopixel.library.langchain.java.prompts.PromptTemplate;
import com.topopixel.library.langchain.java.schema.HumanMessage;
import com.topopixel.library.langchain.java.schema.SystemMessage;
import com.topopixel.library.langchain.java.sql_database.SQLDatabase;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // test chat
//        OpenAIChatConfig config = OpenAIChatConfig.builder()
//            .maxTokens(1024)
//            .build();
//        ChatOpenAI llm = new ChatOpenAI(config);
//        System.out.println(llm.run(Arrays.asList(
//            new SystemMessage("你是一个专业的中译英翻译助手，会将听到的所有中文都翻译为英文"),
//            new HumanMessage("我很牛逼")
//        )).getContent());

        //test prompt
//        PromptTemplate template = new PromptTemplate(Arrays.asList("product"),
//            "What is a good name for a company that makes {product}?");
//        System.out.println(template.format(new HashMap<String, Object>(){{put("product", "colorful socks");}}));
//
//        //test chain
//        OpenAIChatConfig config = OpenAIChatConfig.builder()
//            .maxTokens(1024)
//            .build();
//        ChatOpenAI llm = new ChatOpenAI(config);
//        LLMChain chain = LLMChain.builder()
//            .llm(llm)
//            .prompt(template)
//            .build();
//        String result = chain.run(new HashMap<String, Object>(){{put("product", "colorful socks");}});
//        System.out.println(result);

        // test mysql
        OpenAIConfig mysqlLLMConfig = OpenAIConfig.builder()
            .temperature(0f)
            .build();
        OpenAI mysqlLLM = new OpenAI(mysqlLLMConfig);
        PromptTemplate mysqlTpl = SQLDatabasePrompts.MYSQL_PROMPT;
        LLMChain sqlChain = LLMChain.builder()
            .llm(mysqlLLM)
            .prompt(mysqlTpl)
            .build();
        SQLDatabase db = SQLDatabase.uri("jdbc:mysql://root:5kasile123@localhost:3306/rhino").build();

        SQLDatabaseChain chain = SQLDatabaseChain.builder()
            .llmChain(sqlChain)
            .sqlDatabase(db)
            .build();

        System.out.println(chain.run(new HashMap<String, Object>(){{
            put("query", "give me user named cy448219947's character type");
            put("table_names_to_use", new ArrayList<String>(Arrays.asList("unit", "user")));
        }}));
    }
}
