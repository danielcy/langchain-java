package llms;

import com.topopixel.library.langchain.java.llms.openai.OpenAI;
import com.topopixel.library.langchain.java.llms.openai.OpenAIConfig;
import org.junit.Test;

public class OpenAITest {

    @Test
    public void openAITest() {
        OpenAIConfig config = OpenAIConfig.builder()
            .maxTokens(1024)
            .build();
        OpenAI llm = new OpenAI(config);
        System.out.println(llm.run("who are you"));
    }
}
