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
### 3. llm chain
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
