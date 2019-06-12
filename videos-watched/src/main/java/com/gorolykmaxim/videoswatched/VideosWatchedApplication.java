package com.gorolykmaxim.videoswatched;

import com.gorolykmaxim.videoswatched.kafka.AcceptOffsetFromKafka;
import com.gorolykmaxim.videoswatched.kafka.StartFromTheBeginning;
import com.gorolykmaxim.videoswatched.peristence.dao.*;
import com.gorolykmaxim.videoswatched.peristence.sql.SqlUserDao;
import com.gorolykmaxim.videoswatched.peristence.sql.SqlVideoDao;
import com.gorolykmaxim.videoswatched.peristence.sql.SqlVideoWatchProgressDao;
import com.gorolykmaxim.videoswatched.peristence.sql.SqlWatchProgressDao;
import com.gorolykmaxim.videoswatched.service.event.UserEvent;
import com.gorolykmaxim.videoswatched.service.event.VideoEvent;
import com.gorolykmaxim.videoswatched.service.eventprocessor.UserEventsProcessor;
import com.gorolykmaxim.videoswatched.service.eventprocessor.VideoEventsProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableKafka
@Configuration
public class VideosWatchedApplication {
    private Logger log = LoggerFactory.getLogger(VideosWatchedApplication.class);
    @Value("${videos-watched.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${videos-watched.kafka.consumer.group_id}")
    private String groupId;

    public Map<String, Object> consumerConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return properties;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserEvent> userAuthenticatedEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(UserEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userAuthenticatedEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userAuthenticatedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, VideoEvent> videoEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(VideoEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VideoEvent> videoEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VideoEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(videoEventConsumerFactory());
        return factory;
    }

    @Bean
    public UserDao userDao(DataSource dataSource) {
        return new SqlUserDao(jdbcTemplate(dataSource));
    }

    @Bean
    public VideoDao videoDao(DataSource dataSource) {
        return new SqlVideoDao(jdbcTemplate(dataSource));
    }

    @Bean
    public WatchProgressDao watchProgressDao(DataSource dataSource) {
        return new SqlWatchProgressDao(jdbcTemplate(dataSource));
    }

    @Bean
    public VideoWatchProgressDao videoWatchProgressDao(DataSource dataSource) {
        return new SqlVideoWatchProgressDao(jdbcTemplate(dataSource));
    }

    @Bean
    public UserEventsProcessor userEventsProcessor(UserDao dao) {
        return new UserEventsProcessor(dao, offsetResolutionStrategy(dao));
    }

    @Bean
    public VideoEventsProcessor videoEventsProcessor(VideoDao videoDao, WatchProgressDao watchProgressDao) {
        return new VideoEventsProcessor(videoDao, watchProgressDao, offsetResolutionStrategy(videoDao, watchProgressDao));
    }

    private ConsumerSeekAware offsetResolutionStrategy(Dao... daos) {
        ConsumerSeekAware strategy = new AcceptOffsetFromKafka();
        for (Dao dao: daos) {
            if (dao.count() > 0) {
                log.info("{} is already populated.", dao);
            } else {
                log.info("There are no entities in {}.", dao);
                strategy = new StartFromTheBeginning();
            }
        }
        log.info("Will use {} strategy.", strategy);
        return strategy;
    }

    public static void main(String[] args) {
        SpringApplication.run(VideosWatchedApplication.class, args);
    }
}
