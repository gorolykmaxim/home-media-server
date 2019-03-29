package com.gorolykmaxim.homemediaapp.model.tvshow;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TvShowDao extends CrudRepository<TvShow, UUID> {
}
