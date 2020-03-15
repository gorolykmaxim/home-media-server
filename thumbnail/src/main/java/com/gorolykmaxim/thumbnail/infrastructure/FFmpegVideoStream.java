package com.gorolykmaxim.thumbnail.infrastructure;

import com.gorolykmaxim.thumbnail.domain.VideoStream;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.time.Duration;

public class FFmpegVideoStream implements VideoStream {
    private FFmpegStream stream;

    public FFmpegVideoStream(FFmpegStream stream) {
        this.stream = stream;
    }

    @Override
    public Duration getDuration() {
        return Duration.ofSeconds((long) stream.duration);
    }
}
