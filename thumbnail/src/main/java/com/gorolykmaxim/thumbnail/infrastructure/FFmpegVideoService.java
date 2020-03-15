package com.gorolykmaxim.thumbnail.infrastructure;

import com.gorolykmaxim.thumbnail.domain.VideoFrameExtractionException;
import com.gorolykmaxim.thumbnail.domain.VideoService;
import com.gorolykmaxim.thumbnail.domain.VideoStream;
import com.gorolykmaxim.thumbnail.domain.VideoStreamReadException;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FFmpegVideoService implements VideoService {
    private static final Logger log = LoggerFactory.getLogger(FFmpegVideoService.class);

    private String resolution;
    private FFprobe fprobe;
    private FFmpegExecutor fmpegExecutor;
    private Map<Path, Object> imageFilesBeingProcessed;
    private Object dummyValue;

    public FFmpegVideoService(String resolution, FFprobe fprobe, FFmpegExecutor fmpegExecutor) {
        this.resolution = resolution;
        this.fprobe = fprobe;
        this.fmpegExecutor = fmpegExecutor;
        imageFilesBeingProcessed = new ConcurrentHashMap<>();
        dummyValue = new Object();
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
        boolean haveBeenExtractingFrame = false;
        try {
            // The operation will take couple of minutes and will consume a considerable amount of CPU. We don't want
            // to run it when there is no necessity. The domain model is already checking if a thumbnail already exists
            // (and does not try to generate a new one if a thumbnail exists).
            // The only thing we need to do here is to not try to generate a thumbnail that is being generated at this
            // time on another thread.
            // If the image is not being generated somewhere else - the map will return null (meaning no other thread
            // has putted the image file path to the map). In such case - we will generate the thumbnail.
            // In the opposite case - the map will return the value, that has been putted there by some other thread.
            // This means that the image is already being generated. In such case this method call will just skip
            // generation and exit.
            if (imageFilesBeingProcessed.putIfAbsent(outputImageFilePath, dummyValue) == null) {
                log.info("Starting extracting frame at {} of video file {} to {}", startOffset, videoFilePath, outputImageFilePath);
                haveBeenExtractingFrame = true;
                FFmpegBuilder builder = new FFmpegBuilder()
                        .addInput(videoFilePath.toString())
                        .overrideOutputFiles(true)
                        .addOutput(outputImageFilePath.toString())
                        .setVideoResolution(resolution)
                        .disableAudio()
                        .setFrames(1)
                        .setStartOffset(startOffset.getSeconds(), TimeUnit.SECONDS)
                        .done();
                fmpegExecutor.createJob(builder).run();
            } else {
                log.info("{} is currently being generated. Skipping...", outputImageFilePath);
            }
        } catch (Exception e) {
            throw new VideoFrameExtractionException(videoFilePath, outputImageFilePath, startOffset, e);
        } finally {
            // Release image generation "lock", so that other threads would be able to overwrite the existing image
            // if necessary. At this point the image will be already present on the disk and the domain logic thumbnail
            // existence validation will kick in.
            if (haveBeenExtractingFrame) {
                log.info("Releasing processing lock for {}", outputImageFilePath);
                imageFilesBeingProcessed.remove(outputImageFilePath);
            }
        }
    }
}
