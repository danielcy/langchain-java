package com.topopixel.library.langchain.java.text_splitter;

import com.topopixel.library.langchain.java.text_splitter.length_functions.LengthFunction;
import java.util.*;
import lombok.Builder;

public class RecursiveCharacterTextSplitter extends TextSplitter {

    private List<String> separators;

    @Builder(builderMethodName = "rechBuilder")
    public RecursiveCharacterTextSplitter(List<String> separators, Integer chunkSize, Integer chunkOverlap,
        LengthFunction lengthFunction, Boolean keepSeparator) {
        super(chunkSize, chunkOverlap, lengthFunction, keepSeparator);
        this.separators = separators != null ? separators : Arrays.asList("\n\n", "\n", " ", "");
    }

    public List<String> splitText(String text, List<String> separators) {
        List<String> finalChunks = new ArrayList<>();
        String separator = separators.get(separators.size()-1);
        List<String> newSeparators = null;
        for (int i = 0; i < separators.size(); i++) {
            String s = separators.get(i);
            if ("".equals(s)) {
                separator = s;
                break;
            }
            if (text.contains(s)) {
                separator = s;
                newSeparators = separators.subList(i+1, separators.size());
                break;
            }
        }

        List<String> splits = TextSplitterUtils.splitTextWithRegex(text, separator, keepSeparator);
        List<String> goodSplits = new ArrayList<>();
        String mSeparator = keepSeparator ? "" : separator;
        for (String s: splits) {
            if (lengthFunction.getLength(s) < chunkSize) {
                goodSplits.add(s);
            } else {
                if (goodSplits.size() > 0) {
                    List<String> mergedText = mergeSplits(goodSplits, mSeparator);
                    finalChunks.addAll(mergedText);
                    goodSplits = new ArrayList<>();
                }
                if (newSeparators == null) {
                    finalChunks.add(s);
                } else {
                    List<String> otherInfo = splitText(s, newSeparators);
                    finalChunks.addAll(otherInfo);
                }
            }
        }
        if (goodSplits.size() > 0) {
            List<String> mergedText = mergeSplits(goodSplits, mSeparator);
            finalChunks.addAll(mergedText);
        }
        return finalChunks;
    }

    @Override
    public List<String> splitText(String text) {
        return splitText(text, separators);
    }
}
