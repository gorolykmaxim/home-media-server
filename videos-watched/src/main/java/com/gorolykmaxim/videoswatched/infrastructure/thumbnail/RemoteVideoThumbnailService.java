package com.gorolykmaxim.videoswatched.infrastructure.thumbnail;

import com.gorolykmaxim.videoswatched.domain.video.VideoThumbnailService;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Path;

public class RemoteVideoThumbnailService implements VideoThumbnailService {
    private URI baseUri;
    private RestTemplate template;

    public RemoteVideoThumbnailService(URI baseUri) {
        this.baseUri = baseUri;
        template = new RestTemplate();
    }

    @Override
    public void createThumbnailForVideo(Path relativeVideoPath, String thumbnailName) {
        try {
            Thumbnail thumbnail = new Thumbnail(thumbnailName, relativeVideoPath.toString());
            template.postForObject(baseUri.resolve("/api/thumbnail"), new HttpEntity<>(thumbnail), Void.class);
        } catch (Exception e) {
            throw new ThumbnailCreationException(thumbnailName, relativeVideoPath, e);
        }
    }
}
