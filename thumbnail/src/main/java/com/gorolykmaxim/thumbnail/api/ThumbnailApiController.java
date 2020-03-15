package com.gorolykmaxim.thumbnail.api;

import com.gorolykmaxim.thumbnail.domain.MediaFile;
import com.gorolykmaxim.thumbnail.domain.Thumbnail;
import com.gorolykmaxim.thumbnail.domain.ThumbnailService;
import com.gorolykmaxim.thumbnail.domain.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ThumbnailApiController {
    private static final Logger log = LoggerFactory.getLogger(ThumbnailApiController.class);

    private String thumbnailPlaceholder;
    private String thumbnailFormat;
    private Path rootThumbnailFolder;
    private Path rootVideoFolder;
    private VideoService videoService;
    private ThumbnailService thumbnailService;

    @Autowired
    public ThumbnailApiController(VideoService videoService, ThumbnailService thumbnailService) {
        this.videoService = videoService;
        this.thumbnailService = thumbnailService;
    }

    @Value("${thumbnail.placeholder-resource-file}")
    public void setThumbnailPlaceholder(String thumbnailPlaceholder) {
        this.thumbnailPlaceholder = thumbnailPlaceholder;
    }

    @Value("${thumbnail.format}")
    public void setThumbnailFormat(String thumbnailFormat) {
        this.thumbnailFormat = thumbnailFormat;
    }

    @Value("${thumbnail.root-thumbnail-folder}")
    public void setRootThumbnailFolder(Path rootThumbnailFolder) {
        this.rootThumbnailFolder = rootThumbnailFolder;
    }

    @Value("${thumbnail.root-video-folder}")
    public void setRootVideoFolder(Path rootVideoFolder) {
        this.rootVideoFolder = rootVideoFolder;
    }

    @GetMapping(value = "thumbnail-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public AbstractResource getThumbnail(@RequestParam String name) {
        MediaFile thumbnailFile = new MediaFile(rootThumbnailFolder, Paths.get(name), thumbnailFormat);
        return thumbnailService.exists(thumbnailFile.getAbsolutePath()) ? new FileSystemResource(thumbnailFile.getAbsolutePath()) : new ClassPathResource(thumbnailPlaceholder);
    }

    @GetMapping("thumbnail")
    @ResponseBody
    public List<String> getAllThumbnails() {
        return thumbnailService.findAllThumbnailsInFolder(rootThumbnailFolder)
                .stream()
                .map(path -> new MediaFile(rootThumbnailFolder, rootThumbnailFolder.relativize(path)).getName())
                .collect(Collectors.toList());
    }

    @PostMapping("thumbnail")
    @Async
    public void createThumbnail(@Valid @RequestBody CreateThumbnailRequest request) {
        try {
            Path relativeVideoFilePath = Paths.get(request.getRelativeVideoFilePath());
            Path thumbnailFileName = Paths.get(request.getThumbnailName());
            log.info("Going to create a thumbnail for a video {} that will have a name {}", relativeVideoFilePath, thumbnailFileName);
            MediaFile videoFile = new MediaFile(rootVideoFolder, relativeVideoFilePath);
            MediaFile thumbnailFile = new MediaFile(rootThumbnailFolder, thumbnailFileName, thumbnailFormat);
            Thumbnail thumbnail = new Thumbnail(thumbnailFile);
            thumbnail.generateIfNotExistFor(videoFile, videoService, thumbnailService);
        } catch (Exception e) {
            log.error(String.format("Failed to process %s. Reason: %s", request, e.getMessage()), e);
        }
    }

    @DeleteMapping("thumbnail")
    public void deleteThumbnails(@RequestParam(required = false) String name) {
        List<Path> thumbnailsToRemove;
        if (name != null) {
            MediaFile thumbnailFile = new MediaFile(rootThumbnailFolder, Paths.get(name), thumbnailFormat);
            thumbnailsToRemove = Collections.singletonList(thumbnailFile.getAbsolutePath());
        } else {
            thumbnailsToRemove = thumbnailService.findAllThumbnailsInFolder(rootThumbnailFolder);
        }
        log.info("Going to remove following thumbnails: {}", thumbnailsToRemove);
        thumbnailsToRemove.forEach(thumbnailService::removeThumbnail);
    }
}
