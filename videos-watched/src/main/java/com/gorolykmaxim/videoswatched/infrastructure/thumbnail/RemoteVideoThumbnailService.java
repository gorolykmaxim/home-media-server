package com.gorolykmaxim.videoswatched.infrastructure.thumbnail;

import com.gorolykmaxim.videoswatched.domain.video.VideoThumbnailService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Override
    public ResponseEntity<?> downloadThumbnail(String thumbnailName) {
        URI uri = UriComponentsBuilder.fromUri(baseUri.resolve("/api/thumbnail-image"))
                .queryParam("name", thumbnailName)
                .build()
                .toUri();
        return template.exchange(uri, HttpMethod.GET, null, byte[].class);
    }
}
