## LangChain For JAVA

### Project Status
still WIP

### What's Already Done
#### 1. openai model

```java
public class Main {

    public static void main(String[] args) {
        OpenAIConfig config = OpenAIConfig.builder()
            .maxTokens(1024)
            .build();
        OpenAI llm = new OpenAI(config);
        System.out.println(llm.run("如何评价人工智能"));
    }
}
```
#### 2. openai chat model
```java
public class Main {

    public static void main(String[] args) {
        OpenAIChatConfig config = OpenAIChatConfig.builder()
            .maxTokens(1024)
            .build();
        ChatOpenAI llm = new ChatOpenAI(config);
        System.out.println(llm.run(Arrays.asList(new HumanMessage("如何评价人工智能"))).getContent());
    }
}
```
#### 3. llm chain
```java
public class Main {

    public static void main(String[] args) {
        PromptTemplate template = new PromptTemplate(Arrays.asList("product"),
            "What is a good name for a company that makes {product}?");

        OpenAIChatConfig config = OpenAIChatConfig.builder()
            .maxTokens(1024)
            .build();
        ChatOpenAI llm = new ChatOpenAI(config);
        LLMChain chain = LLMChain.builder()
            .llm(llm)
            .prompt(template)
            .build();
        String result = chain.run(new HashMap<String, Object>(){{put("product", "colorful socks");}});
        System.out.println(result);
    }
}
```
####4. sql database(for now only support mysql)
```java
public class Main {

    public static void main(String[] args) {
        // create a llm model
        OpenAIConfig mysqlLLMConfig = OpenAIConfig.builder()
            .temperature(0f)
            .build();
        OpenAI mysqlLLM = new OpenAI(mysqlLLMConfig);
        // create a mysql prompt template
        PromptTemplate mysqlTpl = SQLDatabasePrompts.MYSQL_PROMPT;
        // make a LLMChain
        LLMChain sqlChain = LLMChain.builder()
            .llm(mysqlLLM)
            .prompt(mysqlTpl)
            .build();
        
        //create a database
        SQLDatabase db = SQLDatabase.uri("jdbc:mysql://your_mysql_host/your_database").build();

        // create a SQLDatabaseChain and inject LLMChain & Database
        SQLDatabaseChain chain = SQLDatabaseChain.builder()
            .llmChain(sqlChain)
            .sqlDatabase(db)
            .build();

        System.out.println(chain.run(new HashMap<String, Object>(){{
            put("query", "how many users are there");
            put("table_names_to_use", 
            new ArrayList<String>(Arrays.asList("your_table_1", "your_table_2")));
        }}));
    }
}
```
