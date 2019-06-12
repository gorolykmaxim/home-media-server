package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class SqlVideoDao implements VideoDao {
    private JdbcTemplate template;
    private VideoRowMapper mapper;

    public SqlVideoDao(JdbcTemplate template) {
        this.template = template;
        mapper = new VideoRowMapper();
    }

    @Override
    public int count() {
        try {
            return template.queryForObject("SELECT COUNT(*) FROM video", Integer.class);
        } catch (RuntimeException e) {
            throw new RecordsCountError("video", e);
        }
    }

    @Override
    public Optional<Video> findById(long videoId) {
        try {
            Video video = template.queryForObject("SELECT * FROM video WHERE id = ?", new Object[] {videoId}, mapper);
            return Optional.ofNullable(video);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new VideoLookupError(videoId, e);
        }
    }

    @Override
    public void updateNameById(long videoId, String name) {
        try {
            template.update("UPDATE video SET name = ? WHERE id = ?", name, videoId);
        } catch (RuntimeException e) {
            throw new VideoUpdateError(videoId, name, null, null, e);
        }
    }

    @Override
    public void updateGroupNameById(long videoId, String groupName) {
        try {
            template.update("UPDATE video SET group_name = ? WHERE id = ?", groupName, videoId);
        } catch (RuntimeException e) {
            throw new VideoUpdateError(videoId, null, groupName, null, e);
        }
    }

    @Override
    public void updateTotalTimeById(long videoId, long totalTime) {
        try {
            template.update("UPDATE video SET total_time = ? WHERE id = ?", totalTime, videoId);
        } catch (RuntimeException e) {
            throw new VideoUpdateError(videoId, null, null, totalTime, e);
        }
    }

    @Override
    public void create(long videoId, String name, String groupName, Long totalTime) {
        try {
            template.update("INSERT INTO video VALUES(?, ?, ?, ?)", videoId, name, groupName, totalTime);
        } catch (RuntimeException e) {
            throw new VideoCreationError(videoId, name, groupName, totalTime, e);
        }
    }

    public static class VideoLookupError extends RuntimeException {
        public VideoLookupError(long videoId, Throwable cause) {
            super(String.format("Failed to find video with ID %s. Reason: %s", videoId, cause.getMessage()), cause);
        }
    }

    public static class VideoModificationError extends RuntimeException {
        public VideoModificationError(String action, long videoId, String name, String groupName, Long totalTime, Throwable cause) {
            super(String.format("Failed to %s video with ID %s to name %s, video group name %s and total time %s. Reason: %s",
                    action, videoId, name, groupName, totalTime, cause.getMessage()), cause);
        }
    }

    public static class VideoUpdateError extends VideoModificationError {
        public VideoUpdateError(long videoId, String name, String groupName, Long totalTime, Throwable cause) {
            super("update", videoId, name, groupName, totalTime, cause);
        }
    }

    public static class VideoCreationError extends VideoModificationError {
        public VideoCreationError(long videoId, String name, String groupName, Long totalTime, Throwable cause) {
            super("create", videoId, name, groupName, totalTime, cause);
        }
    }
}
