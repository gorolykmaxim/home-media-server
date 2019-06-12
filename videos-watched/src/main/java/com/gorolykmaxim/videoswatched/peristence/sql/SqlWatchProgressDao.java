package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.WatchProgressDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

public class SqlWatchProgressDao implements WatchProgressDao {
    private JdbcTemplate template;
    private WatchProgressRowMapper mapper;

    public SqlWatchProgressDao(JdbcTemplate template) {
        this.template = template;
        mapper = new WatchProgressRowMapper();
    }

    @Override
    public int count() {
        try {
            return template.queryForObject("SELECT COUNT(*) FROM watch_progress", Integer.class);
        } catch (RuntimeException e) {
            throw new RecordsCountError("watch_progress", e);
        }
    }

    @Override
    public Optional<WatchProgress> findByVideoIdAndUserId(long videoId, long userId) {
        try {
            WatchProgress progress = template.queryForObject("SELECT * FROM watch_progress WHERE video_id = ? AND user_id = ?", new Object[] {videoId, userId}, mapper);
            return Optional.ofNullable(progress);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new WatchProgressLookupError(videoId, userId, e);
        }
    }

    @Override
    public void updateByVideoIdAndUserId(long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate) {
        try {
            template.update("UPDATE watch_progress SET time_played = ?, last_watch_date = ? " +
                    "WHERE video_id = ? AND user_id = ?", timePlayed, lastWatchDate, videoId, userId);
        } catch (RuntimeException e) {
            throw new WatchProgressUpdateError(videoId, userId, timePlayed, lastWatchDate, e);
        }
    }

    @Override
    public void create(long videoId, Long userId, long timePlayed, LocalDateTime lastWatchDate) {
        try {
            template.update("INSERT INTO watch_progress VALUES(?, ?, ?, ?)", videoId, userId, timePlayed, lastWatchDate);
        } catch (RuntimeException e) {
            throw new WatchProgressCreationError(videoId, userId, timePlayed, lastWatchDate, e);
        }
    }

    public static class WatchProgressLookupError extends RuntimeException {
        public WatchProgressLookupError(long videoId, long userId, Throwable cause) {
            super(String.format("Failed to find watch progress with ID %s and user ID %s. Reason: %s", videoId, userId,
                    cause.getMessage()), cause);
        }
    }

    public static class WatchProgressModificationError extends RuntimeException {
        public WatchProgressModificationError(String action, long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate, Throwable cause) {
            super(String.format("Failed to %s watch progress with ID %s, user ID %s, time played %s and last watch " +
                            "date %s. Reason: %s", action, videoId, userId, timePlayed, lastWatchDate,
                    cause.getMessage()), cause);
        }
    }

    public static class WatchProgressUpdateError extends WatchProgressModificationError {
        public WatchProgressUpdateError(long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate, Throwable cause) {
            super("update", videoId, userId, timePlayed, lastWatchDate, cause);
        }
    }

    public static class WatchProgressCreationError extends WatchProgressModificationError {
        public WatchProgressCreationError(long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate, Throwable cause) {
            super("create", videoId, userId, timePlayed, lastWatchDate, cause);
        }
    }
}
