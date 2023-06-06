package com.topopixel.library.langchain.java.text_splitter;

import java.util.*;
import java.util.stream.Collectors;

public class TextSplitterUtils {

    protected static List<String> splitTextWithRegex(String text, String separator,
        boolean keepSeparator) {
        List<String> splits = new ArrayList<>();
        if (separator != null) {
            if (keepSeparator) {
                List<String> mSplit = Arrays.asList(text.split(separator));
                for (int i = 1; i <mSplit.size() - 2; i+=2) {
                    splits.add(mSplit.get(i) + mSplit.get(i+1));
                }
                if (splits.size() % 2 == 0) {
                    splits.add(mSplit.get(mSplit.size() - 1));
                }
                splits.add(0, mSplit.get(0));
            } else {
                splits = Arrays.asList(text.split(separator));
            }
        } else {
            splits = Arrays.asList(text.split(""));
        }
        return splits.stream().filter(s -> !"".equals(s)).collect(Collectors.toList());
    }
}
