package com.topopixel.library.langchain.java;

import com.topopixel.library.langchain.java.chain.llm.LLMChain;
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
        // SQLDatabase db = SQLDatabase.uri("")

        String tableInfo = "\nCREATE TABLE unit (\n\tid BIGINT UNSIGNED NOT NULL COMMENT 'Unit ID' AUTO_INCREMENT, \n\tuid BIGINT UNSIGNED NOT NULL COMMENT '创建者ID', \n\ttitle VARCHAR(100) NOT NULL COMMENT 'Unit标题', \n\tmodel_id BIGINT NOT NULL, \n\tarticle TEXT NOT NULL COMMENT 'Unit介绍内容', \n\trender_type INTEGER NOT NULL COMMENT '渲染类型', \n\tscene VARCHAR(50) NOT NULL COMMENT '场景名', \n\textra_info VARCHAR(4096) COMMENT '额外信息', \n\tviewable TINYINT NOT NULL COMMENT '是否可见', \n\tcreated_at DATETIME COMMENT '创建时间' DEFAULT CURRENT_TIMESTAMP, \n\tupdated_at DATETIME COMMENT '更新时间' DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, \n\tcategory_id INTEGER, \n\tPRIMARY KEY (id)\n)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_0900_ai_ci\n\n/*\n3 rows from unit table:\nid\tuid\ttitle\tmodel_id\tarticle\trender_type\tscene\textra_info\tviewable\tcreated_at\tupdated_at\tcategory_id\n9\t1\t1\t9\t12312\t2\t123\t123\t0\t2022-05-31 18:50:33\t2022-05-31 18:50:33\t1\n*/\n\n\nCREATE TABLE user (\n\tid BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, \n\tuid BIGINT UNSIGNED NOT NULL, \n\tusername VARCHAR(50) NOT NULL, \n\tpassword VARCHAR(50) NOT NULL, \n\tnickname VARCHAR(50), \n\tmobile VARCHAR(20), \n\temail VARCHAR(50), \n\tcharacter_type VARCHAR(50), \n\tavatar_created TINYINT UNSIGNED DEFAULT '0', \n\tgender TINYINT DEFAULT '1', \n\tsignature VARCHAR(255), \n\tcreated_at DATETIME DEFAULT CURRENT_TIMESTAMP, \n\tupdated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, \n\thas_login TINYINT UNSIGNED DEFAULT '0', \n\tPRIMARY KEY (id)\n)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_0900_ai_ci\n\n/*\n3 rows from user table:\nid\tuid\tusername\tpassword\tnickname\tmobile\temail\tcharacter_type\tavatar_created\tgender\tsignature\tcreated_at\tupdated_at\thas_login\n8\t10886111771791360\tcy448219947\t5kasile123\tNone\t13482892612\t448219947@qq.com\tman_pirate\t1\t1\tNone\tNone\tNone\t0\n9\t14402982835949568\tcynblalala\t5kasile123\tNone\t13482892612\t448219947@qq.com\tman_astronaut\t1\t1\tNone\tNone\t2022-02-11 01:29:57\t0\n*/";
        Map<String, Object> inputs = new HashMap<String, Object>() {{
            put("input", "how many users have email?\nSQLQuery:");
            put("table_info", tableInfo);
            put("top_k", 5);
            put("stop", Arrays.asList("\nSQLResult:"));
        }};
        System.out.println(sqlChain.predict(inputs, null).trim());
    }
}
