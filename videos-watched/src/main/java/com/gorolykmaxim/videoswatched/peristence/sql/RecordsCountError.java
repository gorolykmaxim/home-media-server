package com.gorolykmaxim.videoswatched.peristence.sql;

public class RecordsCountError extends RuntimeException {
    public RecordsCountError(String tableName, Throwable cause) {
        super(String.format("Failed to calculate amount of records in %s. Reason: %s", tableName, cause.getMessage()), cause);
    }
}
