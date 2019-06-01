package com.gorolykmaxim.torrentui.common;

public class EmptyBodyError extends RuntimeException {
    public EmptyBodyError() {
        super("Response body is empty");
    }
}
