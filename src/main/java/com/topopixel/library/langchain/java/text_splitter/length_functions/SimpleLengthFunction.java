package com.topopixel.library.langchain.java.text_splitter.length_functions;

public class SimpleLengthFunction implements LengthFunction {

    @Override
    public int getLength(String text) {
        return text.length();
    }
}
