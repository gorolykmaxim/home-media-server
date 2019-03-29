package com.gorolykmaxim.homemediaapp.model.tvshow;

import java.util.Optional;
import java.util.UUID;

public class TvShowRepository {
    private TvShowStorage storage;
    private TvShowDao dao;

    public TvShowRepository(TvShowStorage storage, TvShowDao dao) {
        this.storage = storage;
        this.dao = dao;
    }

    public Iterable<TvShow> findAll() {
        return dao.findAll();
    }

    public TvShow findById(UUID id) throws TvShowDoesNotExistException {
        Optional<TvShow> possibleTvShow = dao.findById(id);
        if (!possibleTvShow.isPresent()) {
            throw new TvShowDoesNotExistException(id);
        }
        return possibleTvShow.get();
    }

    public void save(TvShow tvShow) {
        storage.store(tvShow);
        dao.save(tvShow);
    }

    public void delete(TvShow tvShow) {
        storage.delete(tvShow);
        dao.delete(tvShow);
    }

    public static class TvShowDoesNotExistException extends Exception {
        public TvShowDoesNotExistException(UUID id) {
            super(String.format("TV Show with ID '%s' does not exist", id));
        }
    }
}
