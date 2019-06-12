package com.gorolykmaxim.videoswatched.peristence.dao;

import com.gorolykmaxim.videoswatched.peristence.model.Video;

import java.util.Optional;

public interface VideoDao extends Dao {
    Optional<Video> findById(long videoId);
    void updateNameById(long videoId, String name);
    void updateGroupNameById(long videoId, String groupName);
    void updateTotalTimeById(long videoId, long totalTime);
    void create(long videoId, String name, String groupName, Long totalTime);
}
