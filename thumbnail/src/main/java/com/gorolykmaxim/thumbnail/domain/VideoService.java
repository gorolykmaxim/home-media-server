package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public interface VideoService {
    List<VideoStream> getStreamsOfFile(Path videoFilePath);
    void extractFrameAsImageFile(Path videoFilePath, Path outputImageFilePath, Duration startOffset);
}
