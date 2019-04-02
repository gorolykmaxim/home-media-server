package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;
import com.gorolykmaxim.homemediaapp.model.tvshow.ThumbnailRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("apiThumbnailController")
@RequestMapping("/api/v1/thumbnail")
public class ThumbnailController {
    private ThumbnailRepository repository;

    public ThumbnailController(ThumbnailRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Thumbnail> findBySearchTerm(@RequestParam String searchTerm) {
        return repository.findThumbnailsBySearchTerm(searchTerm);
    }
}
