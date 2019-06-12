package com.gorolykmaxim.videoswatched.service.eventprocessor;

import com.gorolykmaxim.videoswatched.peristence.model.User;
import com.gorolykmaxim.videoswatched.peristence.dao.UserDao;
import com.gorolykmaxim.videoswatched.service.event.UserEvent;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserEventsProcessor implements ConsumerSeekAware {
    private Logger log;
    private UserDao dao;
    private ConsumerSeekAware offsetResolutionStrategy;

    public UserEventsProcessor(UserDao dao, ConsumerSeekAware offsetResolutionStrategy) {
        this.dao = dao;
        this.offsetResolutionStrategy = offsetResolutionStrategy;
        log = LoggerFactory.getLogger(UserEventsProcessor.class);
    }

    @KafkaListener(topics = "user", containerFactory = "userAuthenticatedEventConcurrentKafkaListenerContainerFactory")
    @Transactional
    public void handle(UserEvent event) {
        try {
            log.debug("Incoming event: {}", event);
            Optional<User> possibleUser = dao.findById(event.getUserId());
            if (possibleUser.isPresent()) {
                User user = possibleUser.get();
                log.debug("Found existing user {}", user);
                // Kafka will bombard us with the same event, so we should not update user each time.
                if (!Objects.equals(user.getName(), event.getUserName())) {
                    log.debug("Name of the user with ID {} has changed from {} to {}. Going to change it.",
                            event.getUserId(), user.getName(), event.getUserName());
                    dao.updateById(user.getId(), event.getUserName());
                }
            } else {
                log.debug("Going to create a new user with ID {} and name {}", event.getUserId(), event.getUserName());
                dao.create(event.getUserId(), event.getUserName());
            }
        } catch (RuntimeException e) {
            log.error("Failed to process event: {}. Reason: {}", event, e.getMessage());
        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekAware.ConsumerSeekCallback callback) {
        offsetResolutionStrategy.registerSeekCallback(callback);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekAware.ConsumerSeekCallback callback) {
        offsetResolutionStrategy.onPartitionsAssigned(assignments, callback);
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekAware.ConsumerSeekCallback callback) {
        offsetResolutionStrategy.onIdleContainer(assignments, callback);
    }
}
