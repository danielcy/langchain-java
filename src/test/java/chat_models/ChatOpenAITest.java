package chat_models;

import com.topopixel.library.langchain.java.chat_models.ChatOpenAI;
import com.topopixel.library.langchain.java.llms.openai.OpenAIChatConfig;
import com.topopixel.library.langchain.java.schema.HumanMessage;
import java.util.Arrays;
import org.junit.Test;

public class ChatOpenAITest {

    @Test
    public void test() {
        OpenAIChatConfig config = OpenAIChatConfig.builder()
            .maxTokens(256)
            .build();
        ChatOpenAI llm = new ChatOpenAI(config);
        System.out.println(llm.run(Arrays.asList(new HumanMessage("who are you"))).getContent());
    }
}
