package com.gorolykmaxim.thumbnailsearch.service;

import com.gorolykmaxim.thumbnailsearch.model.Thumbnail;
import com.gorolykmaxim.thumbnailsearch.model.ThumbnailRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("thumbnail")
public class ThumbnailController {
    private ThumbnailRepository repository;

    public ThumbnailController(ThumbnailRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Optional<Thumbnail> findBySearchTerm(@RequestParam String searchTerm) {
        return repository.findThumbnailBySearchTermAndIndex(searchTerm);
    }
}