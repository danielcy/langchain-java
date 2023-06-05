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
