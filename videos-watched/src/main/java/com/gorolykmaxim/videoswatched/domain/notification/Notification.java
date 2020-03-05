package com.gorolykmaxim.videoswatched.domain.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "NOTIFICATION")
public class Notification {
    @Id
    @Column(name = "NOTIFICATION_ID")
    private UUID id;
    @Column(name = "CONTENT")
    private String content;

    public static Notification fromException(Throwable exception) {
        Throwable cause = exception.getCause();
        if (cause == null) {
            return createNew(exception.getMessage());
        } else {
            return createNew(String.format("%s. Reason: %s", exception.getMessage(), cause.getMessage()));
        }
    }

    public static Notification createNew(String content) {
        Notification notification = new Notification();
        notification.id = UUID.randomUUID();
        notification.content = content;
        return notification;
    }

    Notification() {
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
