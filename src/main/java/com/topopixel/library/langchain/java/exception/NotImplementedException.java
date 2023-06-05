package com.topopixel.library.langchain.java.exception;

public class NotImplementedException extends RuntimeException {

    private String msg;

    public NotImplementedException(String msg) {
        this.msg = msg;
    }
}
