package com.gorolykmaxim.videoswatched.service.event;

import java.util.Objects;

public class UserEvent {
    private long userId;
    private String userName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEvent that = (UserEvent) o;
        return userId == that.userId &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName);
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
