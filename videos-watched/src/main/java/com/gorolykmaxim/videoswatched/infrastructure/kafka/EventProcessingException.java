package com.gorolykmaxim.videoswatched.infrastructure.kafka;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(KafkaVideoEvent event, Throwable cause) {
        super(String.format("Failed to process event: %s", event), cause);
    }
}
