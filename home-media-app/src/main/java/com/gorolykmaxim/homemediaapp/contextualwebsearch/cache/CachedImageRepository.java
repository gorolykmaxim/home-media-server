package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.sql.Date;
import java.util.List;

@Repository
public interface CachedImageRepository extends PagingAndSortingRepository<CachedImage, URI> {
    List<CachedImage> findAllBySearchTermLike(String searchTerm, Pageable pageable);
    @Transactional
    void deleteAllByCreationDateBefore(Date date);
}
