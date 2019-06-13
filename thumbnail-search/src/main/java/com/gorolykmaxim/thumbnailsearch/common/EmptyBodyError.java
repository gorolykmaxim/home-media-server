package com.gorolykmaxim.thumbnailsearch.common;

public class EmptyBodyError extends RuntimeException {
    public EmptyBodyError() {
        super("Response body is empty");
    }
}
