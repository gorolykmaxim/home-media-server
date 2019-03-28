package com.gorolykmaxim.homemediaapp.contextualwebsearch.api;

import com.gorolykmaxim.homemediaapp.common.EmptyBodyError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ImageSearchApi {

    private RestTemplate restTemplate;
    private URI baseUri;

    public ImageSearchApi(RestTemplate restTemplate, URI baseUri) {
        this.restTemplate = restTemplate;
        this.baseUri = baseUri;
    }

    public ImageList findImagesBySearchTerm(String searchTerm) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("pageSize", "30");
        parameters.put("pageNumber", "1");
        return findImagesBySearchTerm(searchTerm, parameters);
    }

    public ImageList findImagesBySearchTerm(String searchTerm, Map<String, String> options) {
        options.put("q", searchTerm);
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri.resolve("/api/Search/GetImageSearch").toString());
            for (Map.Entry<String, String> option: options.entrySet()) {
                builder.queryParam(option.getKey(), option.getValue());
            }
            ResponseEntity<ImageList> response = restTemplate.getForEntity(builder.build().toUri(), ImageList.class);
            ImageList imageList = response.getBody();
            if (imageList == null) {
                throw new EmptyBodyError();
            }
            return imageList;
        } catch (RuntimeException e) {
            throw new ImageSearchError(baseUri, options, e);
        }
    }

    public static class ImageSearchError extends RuntimeException {
        public ImageSearchError(URI baseUri, Map<String, String> options, Throwable cause) {
            super(String.format("Failed to find images via '%s' with parameters '%s'. Reason: %s", baseUri, options, cause.getMessage()), cause);
        }
    }
}
