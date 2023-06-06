package document_loaders.text;

import com.topopixel.library.langchain.java.document_loaders.text.TextLoader;
import com.topopixel.library.langchain.java.schema.Document;
import com.topopixel.library.langchain.java.text_splitter.*;
import com.topopixel.library.langchain.java.utils.SnakeJsonUtils;
import java.util.List;
import org.junit.Test;

public class TextLoaderTest {

    @Test
    public void loadTest() {
        String filepath = this.getClass().getClassLoader().getResource("test.txt").getPath();

        TextLoader loader = TextLoader.builder()
            .filePath(filepath)
            .build();
        List<Document> docs = loader.load();
        assert docs.get(0).getPageContent().equals("test1\ntest2\ntest3\n\ntest4\n");
    }

    @Test
    public void loadAndSplitTest() {
        String filepath = this.getClass().getClassLoader().getResource("test.txt").getPath();

        TextLoader loader = TextLoader.builder()
            .filePath(filepath)
            .build();
        TextSplitter splitter = RecursiveCharacterTextSplitter.rechBuilder()
            .chunkSize(5)
            .build();
        List<Document> docs = loader.loadAndSplit(splitter);
        assert docs.size() == 4;
        assert "test1".equals(docs.get(0).getPageContent());
        assert "test2".equals(docs.get(1).getPageContent());
        assert "test3".equals(docs.get(2).getPageContent());
        assert "test4".equals(docs.get(3).getPageContent());
    }
}
