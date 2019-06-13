package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ImageSearchApiTest {
    private RestTemplate restTemplate;
    private URI baseUri;
    private String searchTerm;
    private ImageSearchApi api;

    @Before
    public void setUp() throws Exception {
        restTemplate = Mockito.mock(RestTemplate.class);
        baseUri = URI.create("http://search.com");
        searchTerm = "search term";
        api = new ImageSearchApi(restTemplate, baseUri, 30, 1);    }

    @Test
    public void findImagesBySearchTerm() {
        URI expectedUri = baseUri.resolve("/api/Search/GetImageSearch?q=search%20term&pageNumber=1&pageSize=30");
        ImageList expectedImageList = new ImageList();
        Mockito.when(restTemplate.getForEntity(expectedUri, ImageList.class))
                .thenReturn(ResponseEntity.of(Optional.of(expectedImageList)));
        ImageList images = api.findImagesBySearchTerm(searchTerm);
        Assert.assertEquals(expectedImageList, images);
    }

    @Test
    public void findImagesBySearchTermAndOptions() {
        URI expectedUri = baseUri.resolve("/api/Search/GetImageSearch?pageSize=100&q=search%20term");
        ImageList expectedImageList = new ImageList();
        Mockito.when(restTemplate.getForEntity(expectedUri, ImageList.class))
                .thenReturn(ResponseEntity.of(Optional.of(expectedImageList)));
        Map<String, String> parameters = new TreeMap<>();
        parameters.put("pageSize", "100");
        ImageList images = api.findImagesBySearchTerm(searchTerm, parameters);
        Assert.assertEquals(expectedImageList, images);
    }

    @Test(expected = ImageSearchApi.ImageSearchError.class)
    public void failToFindImagesDueToEmptyBody() {
        URI expectedUri = baseUri.resolve("/api/Search/GetImageSearch?q=search%20term&pageNumber=1&pageSize=30");
        Mockito.when(restTemplate.getForEntity(expectedUri, ImageList.class))
                .thenReturn(ResponseEntity.of(Optional.empty()));
        api.findImagesBySearchTerm(searchTerm);
    }

    @Test(expected = ImageSearchApi.ImageSearchError.class)
    public void failToFindImagesDueToError() {
        URI expectedUri = baseUri.resolve("/api/Search/GetImageSearch?q=search%20term&pageNumber=1&pageSize=30");
        Mockito.when(restTemplate.getForEntity(expectedUri, ImageList.class))
                .thenThrow(Mockito.mock(RuntimeException.class));
        api.findImagesBySearchTerm(searchTerm);
    }
}