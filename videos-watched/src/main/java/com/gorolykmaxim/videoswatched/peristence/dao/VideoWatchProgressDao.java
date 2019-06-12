package com.gorolykmaxim.videoswatched.peristence.dao;

import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;

import java.util.List;

public interface VideoWatchProgressDao {
    List<VideoWatchProgress> findOnePerGroupByUserId(long userId);
    List<VideoWatchProgress> findAllByGroupNameAndUserId(String groupName, long userId);
}
