package com.gorolykmaxim.thumbnail.api;

import com.gorolykmaxim.thumbnail.domain.ThumbnailService;
import com.gorolykmaxim.thumbnail.domain.VideoService;
import com.gorolykmaxim.thumbnail.domain.VideoStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ThumbnailApiControllerTest {
    private VideoService videoService;
    private ThumbnailService thumbnailService;
    private ThumbnailApiController controller;
    private String thumbnailPlaceholder;
    private String thumbnailFormat;
    private Path rootThumbnailFolder;
    private Path rootVideoFolder;
    private String thumbnailName;
    private Path relativeVideoPath;
    private Path expectedThumbnailPath;
    private CreateThumbnailRequest createThumbnailRequest;

    @Before
    public void setUp() throws Exception {
        thumbnailName = "thumbnail-name";
        thumbnailPlaceholder = "thumbnail_placeholder.jpg";
        thumbnailFormat = "jpg";
        relativeVideoPath = Paths.get("group", "video.mp4");
        rootThumbnailFolder = Paths.get("/data");
        rootVideoFolder = Paths.get("/media");
        expectedThumbnailPath = rootThumbnailFolder.resolve(thumbnailName + "." + thumbnailFormat);
        createThumbnailRequest = new CreateThumbnailRequest();
        createThumbnailRequest.setThumbnailName(thumbnailName);
        createThumbnailRequest.setRelativeVideoFilePath(relativeVideoPath.toString());
        videoService = mock(VideoService.class);
        thumbnailService = mock(ThumbnailService.class);
        controller = new ThumbnailApiController(videoService, thumbnailService);
        controller.setThumbnailPlaceholder(thumbnailPlaceholder);
        controller.setThumbnailFormat(thumbnailFormat);
        controller.setRootThumbnailFolder(rootThumbnailFolder);
        controller.setRootVideoFolder(rootVideoFolder);
    }

    @Test
    public void shouldReturnActualGeneratedThumbnail() {
        // given
        when(thumbnailService.exists(expectedThumbnailPath)).thenReturn(true);
        // when
        FileSystemResource resource = (FileSystemResource) controller.getThumbnail(thumbnailName);
        // then
        assertEquals(expectedThumbnailPath.toString(), resource.getPath());
    }

    @Test
    public void shouldReturnAThumbnailPlaceholder() {
        // when
        ClassPathResource resource = (ClassPathResource) controller.getThumbnail(thumbnailName);
        // then
        assertEquals(thumbnailPlaceholder, resource.getPath());
    }

    @Test
    public void shouldReturnAListOfAllGeneratedThumbnails() {
        // given
        List<String> expectedThumbnails = Arrays.asList("thumbnail-1", "thumbnail-2");
        List<Path> thumbnailPaths = expectedThumbnails.stream().map(name -> rootThumbnailFolder.resolve(name + "." + thumbnailFormat)).collect(Collectors.toList());
        when(thumbnailService.findAllThumbnailsInFolder(rootThumbnailFolder)).thenReturn(thumbnailPaths);
        // when
        List<String> thumbnails = controller.getAllThumbnails();
        // then
        assertEquals(expectedThumbnails, thumbnails);
    }

    @Test
    public void shouldNotTryToGenerateAThumbnailForAVideoSinceTheThumbnailAlreadyExists() {
        // given
        when(thumbnailService.exists(expectedThumbnailPath)).thenReturn(true);
        // when
        controller.createThumbnail(createThumbnailRequest);
        // then
        verify(videoService, never()).extractFrameAsImageFile(any(), any(), any());
    }

    @Test
    public void shouldGenerateAThumbnailOfTheMiddleOfTheVideo() {
        // given
        Duration videoDuration = Duration.ofMinutes(10);
        VideoStream stream = mock(VideoStream.class);
        when(stream.getDuration()).thenReturn(videoDuration);
        when(videoService.getStreamsOfFile(rootVideoFolder.resolve(relativeVideoPath)))
                .thenReturn(Collections.singletonList(stream));
        // when
        controller.createThumbnail(createThumbnailRequest);
        // then
        verify(videoService).extractFrameAsImageFile(rootVideoFolder.resolve(relativeVideoPath), expectedThumbnailPath, Duration.ofMinutes(5));
    }

    @Test
    public void shouldFailToGenerateAThumbnailForAVideoDueToNoVideoStreamsAvailable() {
        // given
        when(videoService.getStreamsOfFile(rootVideoFolder.resolve(relativeVideoPath))).thenReturn(Collections.emptyList());
        // when
        controller.createThumbnail(createThumbnailRequest); // should not re-throw no video streams exception
    }

    @Test
    public void shouldFailToGenerateAThumbnailForAVideoDueToAnUnknownReason() {
        // given
        doThrow(new RuntimeException()).when(videoService).extractFrameAsImageFile(any(), any(), any());
        // when
        controller.createThumbnail(createThumbnailRequest); // should not re-throw any exceptions
    }

    @Test
    public void shouldDeleteSpecifiedThumbnail() {
        // when
        controller.deleteThumbnails(thumbnailName);
        // then
        verify(thumbnailService).removeThumbnail(expectedThumbnailPath);
    }

    @Test
    public void shouldDeleteAllGeneratedThumbnails() {
        // given
        List<Path> expectedThumbnailPaths = Stream.of("thumbnail-1", "thumbnail-2")
                .map(name -> rootThumbnailFolder.resolve(name + "." + thumbnailFormat))
                .collect(Collectors.toList());
        when(thumbnailService.findAllThumbnailsInFolder(rootThumbnailFolder)).thenReturn(expectedThumbnailPaths);
        // when
        controller.deleteThumbnails(null);
        // then
        expectedThumbnailPaths.forEach(path -> verify(thumbnailService).removeThumbnail(path));
    }
}
