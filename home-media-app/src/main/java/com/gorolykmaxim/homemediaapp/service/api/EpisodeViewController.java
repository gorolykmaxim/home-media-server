package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.view.EpisodeView;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeViewFactory;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeViewRepository;
import com.gorolykmaxim.homemediaapp.service.model.EpisodeViewPrototype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("apiEpisodeViewController")
@RequestMapping("/api/v1/episode/view")
public class EpisodeViewController {

    private EpisodeViewFactory factory;
    private EpisodeViewRepository repository;

    @Autowired
    public EpisodeViewController(EpisodeViewFactory factory, EpisodeViewRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEpisodeView(@RequestBody EpisodeViewPrototype prototype) {
        try {
            EpisodeView episodeView = factory.create(prototype.getEpisodeName());
            repository.save(episodeView);
        } catch (IllegalArgumentException e) {
            throw new InvalidEpisodeViewError(prototype, e);
        } catch (RuntimeException e) {
            throw new EpisodeViewCreationError(prototype, e);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidEpisodeViewError extends RuntimeException {
        public InvalidEpisodeViewError(EpisodeViewPrototype prototype, Throwable cause) {
            super(String.format("Failed to create view of episode '%s'. Reason: %s", prototype.getEpisodeName(), cause.getMessage()), cause);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class EpisodeViewCreationError extends RuntimeException {
        public EpisodeViewCreationError(EpisodeViewPrototype prototype, Throwable cause) {
            super(String.format("Failed to create view of episode '%s'. Reason: %s", prototype.getEpisodeName(), cause.getMessage()), cause);
        }
    }

}
