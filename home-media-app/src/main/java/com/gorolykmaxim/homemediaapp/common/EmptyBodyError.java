package com.gorolykmaxim.homemediaapp.common;

public class EmptyBodyError extends RuntimeException {
    public EmptyBodyError() {
        super("Response body is empty");
    }
}
