package com.topopixel.library.langchain.java.text_splitter;

import com.topopixel.library.langchain.java.text_splitter.length_functions.LengthFunction;
import java.util.List;
import lombok.Builder;

public class CharacterTextSplitter extends TextSplitter {

    private String separator = "\n\n";

    @Builder(builderMethodName = "chBuilder")
    public CharacterTextSplitter(String separator, Integer chunkSize, Integer chunkOverlap,
        LengthFunction lengthFunction, Boolean keepSeparator) {
        super(chunkSize, chunkOverlap, lengthFunction, keepSeparator);
        this.separator = separator;
    }

    @Override
    public List<String> splitText(String text) {
        List<String> splits = TextSplitterUtils.splitTextWithRegex(text, separator, keepSeparator);
        String sep = keepSeparator ? "" : separator;
        return mergeSplits(splits, separator);
    }
}
