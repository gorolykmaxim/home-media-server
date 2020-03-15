package com.gorolykmaxim.thumbnail;

import com.gorolykmaxim.thumbnail.domain.ThumbnailService;
import com.gorolykmaxim.thumbnail.domain.VideoService;
import com.gorolykmaxim.thumbnail.infrastructure.FFmpegVideoService;
import com.gorolykmaxim.thumbnail.infrastructure.FileSystemThumbnailService;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@Configuration
@EnableAsync
public class ThumbnailApplication {
    @Value("${thumbnail.resolution}")
    private String resolution;

    @Bean
    public VideoService videoService() throws IOException {
        FFprobe fprobe = new FFprobe("ffprobe");
        FFmpeg fmpeg = new FFmpeg("ffmpeg");
        FFmpegExecutor fmpegExecutor = new FFmpegExecutor(fmpeg, fprobe);
        return new FFmpegVideoService(resolution, fprobe, fmpegExecutor);
    }

    @Bean
    public ThumbnailService thumbnailService() {
        return new FileSystemThumbnailService();
    }

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailApplication.class, args);
    }
}
