package com.gorolykmaxim.thumbnail.infrastructure;

import com.gorolykmaxim.thumbnail.domain.VideoFrameExtractionException;
import com.gorolykmaxim.thumbnail.domain.VideoService;
import com.gorolykmaxim.thumbnail.domain.VideoStream;
import com.gorolykmaxim.thumbnail.domain.VideoStreamReadException;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FFmpegVideoService implements VideoService {
    private String resolution;
    private FFprobe fprobe;
    private FFmpegExecutor fmpegExecutor;

    public FFmpegVideoService(String resolution, FFprobe fprobe, FFmpegExecutor fmpegExecutor) {
        this.resolution = resolution;
        this.fprobe = fprobe;
        this.fmpegExecutor = fmpegExecutor;
    }

    @Override
    public List<VideoStream> getStreamsOfFile(Path videoFilePath) {
        try {
            FFmpegProbeResult result = fprobe.probe(videoFilePath.toString());
            return result.getStreams().stream().map(FFmpegVideoStream::new).collect(Collectors.toList());
        } catch (IOException e) {
            throw new VideoStreamReadException(videoFilePath, e);
        }
    }

    @Override
    public void extractFrameAsImageFile(Path videoFilePath, Path outputImageFilePath, Duration startOffset) {
        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setStartOffset(startOffset.getSeconds(), TimeUnit.SECONDS)
                    .addInput(videoFilePath.toString())
                    .overrideOutputFiles(true)
                    .addOutput(outputImageFilePath.toString())
                    .setVideoResolution(resolution)
                    .disableAudio()
                    .setFrames(1)
                    .done();
            fmpegExecutor.createJob(builder).run();
        } catch (Exception e) {
            throw new VideoFrameExtractionException(videoFilePath, outputImageFilePath, startOffset, e);
        }
    }
}
