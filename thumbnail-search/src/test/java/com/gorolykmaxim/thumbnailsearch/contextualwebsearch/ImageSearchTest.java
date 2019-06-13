package com.gorolykmaxim.thumbnailsearch.contextualwebsearch;

import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.Image;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageList;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache.*;
import com.gorolykmaxim.thumbnailsearch.model.Thumbnail;
import com.gorolykmaxim.thumbnailsearch.model.ThumbnailRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ImageSearchTest {
    private int pageSize, pageNumber;
    private String searchTerm;
    private Set<String> expectedImageUrls;
    private ImageList imageList;
    private CachedImage expectedCachedImage;
    private URI baseUri;
    private RestTemplate template;
    private CachedImageRepository cachedImageRepository;
    private ThumbnailRepository repository;

    @Before
    public void setUp() throws Exception {
        pageSize = 30;
        pageNumber = 1;
        searchTerm = "cat";
        String[] imageUrls = new String[] {
                "https://img.purch.com/w/660/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzEwNC84MzAvb3JpZ2luYWwvc2h1dHRlcnN0b2NrXzExMTA1NzIxNTkuanBn",
                "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80",
                "https://upload.wikimedia.org/wikipedia/commons/6/66/An_up-close_picture_of_a_curious_male_domestic_shorthair_tabby_cat.jpg"
        };
        expectedImageUrls = new HashSet<>(Arrays.asList(imageUrls));
        List<Image> images = new ArrayList<>();
        for (String imageUrl: imageUrls) {
            Image image = new Image();
            image.setImageUrl(imageUrl);
            images.add(image);
        }
        imageList = new ImageList();
        imageList.setImages(images);
        baseUri = URI.create("https://www.contextualwebsearch.com");
        template = Mockito.mock(RestTemplate.class);
        cachedImageRepository = Mockito.mock(CachedImageRepository.class);
        ImageSearchApi imageSearchApi = new ImageSearchApi(template, baseUri, pageSize, pageNumber);
        CleanupStrategy cleanupStrategy = new OlderThanTodayStrategy();
        CachedImageFactory factory = new CachedImageFactory();
        ImageCache imageCache = new ImageCache(cachedImageRepository, factory, cleanupStrategy);
        repository = new ImageSearch(imageCache, imageSearchApi);
        expectedCachedImage = factory.create(imageUrls[0], searchTerm);
    }

    @Test
    public void shouldSearchForImagesUsingApiAndCacheResults() {
        // given
        storeInCache(Collections.emptyList());
        respondToApiSearchWithImageList(Optional.of(imageList));
        // when
        Optional<Thumbnail> possibleThumbnail = repository.findThumbnailBySearchTerm(searchTerm);
        // then
        Assert.assertTrue(possibleThumbnail.isPresent());
        Assert.assertTrue(imageList.getImages().contains(possibleThumbnail.get()));
        List<CachedImage> cachedImages = getCachedImages();
        for (CachedImage cachedImage: cachedImages) {
            Assert.assertEquals(searchTerm, cachedImage.getSearchTerm());
            Assert.assertTrue(expectedImageUrls.contains(cachedImage.getUri()));
        }
        cachedImageRepository.deleteAllByCreationDateBefore(Date.valueOf(LocalDate.now()));
    }

    @Test(expected = ImageSearchApi.ImageSearchError.class)
    public void shouldFailFindImagesUsingApiDueToEmptyBody() {
        // given
        storeInCache(Collections.emptyList());
        respondToApiSearchWithImageList(Optional.empty());
        // when
        repository.findThumbnailBySearchTerm(searchTerm);
    }

    @Test
    public void shouldFindImagesInCache() {
        // given
        storeInCache(Collections.singletonList(expectedCachedImage));
        // when
        Optional<Thumbnail> possibleThumbnail = repository.findThumbnailBySearchTerm(searchTerm);
        // then
        Assert.assertTrue(possibleThumbnail.isPresent());
        Assert.assertEquals(expectedCachedImage, possibleThumbnail.get());
        Mockito.verify(template, Mockito.never()).getForEntity(Mockito.any(), Mockito.any());
    }

    @Test
    public void shouldFindNoImageForSearchTerm() {
        // given
        storeInCache(Collections.emptyList());
        imageList.setImages(Collections.emptyList());
        respondToApiSearchWithImageList(Optional.of(imageList));
        // when
        Optional<Thumbnail> possibleThumbnail = repository.findThumbnailBySearchTerm(searchTerm);
        // then
        Assert.assertFalse(possibleThumbnail.isPresent());
    }

    @Test
    public void shouldNotCacheImageUrlOfWhichIsTooBig() {
        // given
        storeInCache(Collections.emptyList());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 2001; i++) {
            builder.append('c');
        }
        Image image = new Image();
        image.setImageUrl(builder.toString());
        imageList.setImages(Collections.singletonList(image));
        respondToApiSearchWithImageList(Optional.of(imageList));
        // when
        repository.findThumbnailBySearchTerm(searchTerm);
        // then
        Mockito.verify(cachedImageRepository, Mockito.never()).save(Mockito.any());
    }

    private void storeInCache(List<CachedImage> cachedImages) {
        Mockito.when(cachedImageRepository.findAllBySearchTermLike(searchTerm))
                .thenReturn(cachedImages);
    }

    private void respondToApiSearchWithImageList(Optional<ImageList> possibleImageList) {
        Map<String, String> options = new HashMap<>();
        options.put("q", searchTerm);
        options.put("pageSize", Integer.toString(pageSize));
        options.put("pageNumber", Integer.toString(pageNumber));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri.resolve("/api/Search/GetImageSearch").toString());
        for (Map.Entry<String, String> option: options.entrySet()) {
            builder.queryParam(option.getKey(), option.getValue());
        }
        ResponseEntity<ImageList> response = ResponseEntity.of(possibleImageList);
        Mockito.when(template.getForEntity(builder.build().toUri(), ImageList.class))
                .thenReturn(response);
    }

    private List<CachedImage> getCachedImages() {
        ArgumentCaptor<CachedImage> cachedImageCaptor = ArgumentCaptor.forClass(CachedImage.class);
        Mockito.verify(cachedImageRepository, Mockito.atLeastOnce()).save(cachedImageCaptor.capture());
        return cachedImageCaptor.getAllValues();
    }
}
