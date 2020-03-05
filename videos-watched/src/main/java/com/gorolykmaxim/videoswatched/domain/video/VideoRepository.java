package com.gorolykmaxim.videoswatched.domain.video;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {
    @Transactional
    void deleteAllByRelativePathStartingWith(String groupName);
}
