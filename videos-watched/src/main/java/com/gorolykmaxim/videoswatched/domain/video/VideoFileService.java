package com.gorolykmaxim.videoswatched.domain.video;

import java.nio.file.Path;

public interface VideoFileService {
    boolean exists(Path videoFileRelativePath);
    Path resolvePathToVideoFile(String videoFileName);
}
