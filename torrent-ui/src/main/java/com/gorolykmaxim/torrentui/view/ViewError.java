package com.gorolykmaxim.torrentui.view;

public class ViewError extends RuntimeException {
    public ViewError(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
