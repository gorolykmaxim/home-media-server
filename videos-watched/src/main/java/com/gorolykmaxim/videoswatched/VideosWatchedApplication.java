package com.gorolykmaxim.videoswatched;

import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoFileService;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import com.gorolykmaxim.videoswatched.infrastructure.FileSystemVideoFileService;
import com.gorolykmaxim.videoswatched.infrastructure.kafka.KafkaEventProcessor;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;

@SpringBootApplication
@Configuration
public class VideosWatchedApplication {
    @Value("${videos-watched.library-root}")
    private String libraryRoot;

    @Value("${videos-watched.duration-format}")
    private String durationFormat;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Bean
    public VideoGroupReadModelRepository videoGroupReadModelRepository() {
        Clock clock = Clock.systemDefaultZone();
        return new VideoGroupReadModelRepository(videoRepository, durationFormat, clock);
    }

    @Bean
    public KafkaEventProcessor eventProcessor() {
        Logger logger = LoggerFactory.getLogger(VideosWatchedApplication.class);
        Path libraryRootPath = Paths.get(libraryRoot);
        VideoFileService service = new FileSystemVideoFileService(libraryRootPath, notificationRepository);
        return new KafkaEventProcessor(videoRepository, notificationRepository, service, logger);
    }

    public static void main(String[] args) {
        SpringApplication.run(VideosWatchedApplication.class, args);
    }
}
