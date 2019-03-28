package com.gorolykmaxim.homemediaapp.model.tvshow;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TvShowRepository extends CrudRepository<TvShow, UUID> {
}
