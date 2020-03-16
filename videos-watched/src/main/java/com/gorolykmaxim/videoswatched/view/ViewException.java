package com.gorolykmaxim.videoswatched.view;

public class ViewException extends RuntimeException {
    public static ViewException showWatchedVideoGroups(Throwable cause) {
        return new ViewException("show watched video groups", cause);
    }

    public static ViewException discardNotifications(Throwable cause) {
        return new ViewException("discard notifications", cause);
    }

    public static ViewException clearWatchHistoryForGroup(int groupId, Throwable cause) {
        return new ViewException(String.format("clear watch history for group with ID '%d'", groupId), cause);
    }

    public ViewException(String action, Throwable cause) {
        super(String.format("Failed to %s. Reason: %s", action, cause.getMessage()), cause);
    }
}
