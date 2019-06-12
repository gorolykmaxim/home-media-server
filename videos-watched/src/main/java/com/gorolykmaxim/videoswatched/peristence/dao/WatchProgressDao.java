package com.gorolykmaxim.videoswatched.peristence.dao;

import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WatchProgressDao extends Dao {
    Optional<WatchProgress> findByVideoIdAndUserId(long videoId, long userId);
    void updateByVideoIdAndUserId(long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate);
    void create(long videoId, Long userId, long timePlayed, LocalDateTime lastWatchDate);
}
