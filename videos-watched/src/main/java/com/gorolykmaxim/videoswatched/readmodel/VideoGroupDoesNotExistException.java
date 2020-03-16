package com.gorolykmaxim.videoswatched.readmodel;

public class VideoGroupDoesNotExistException extends RuntimeException {
    public VideoGroupDoesNotExistException(int groupId) {
        super(String.format("Video group with ID %d does not exist", groupId));
    }
}
