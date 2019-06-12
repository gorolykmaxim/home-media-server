package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoWatchProgressDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

public class SqlVideoWatchProgressDao implements VideoWatchProgressDao {
    private JdbcTemplate template;
    private VideoWatchProgressRowMapper mapper;

    public SqlVideoWatchProgressDao(JdbcTemplate template) {
        this.template = template;
        mapper = new VideoWatchProgressRowMapper();
    }

    @Override
    public List<VideoWatchProgress> findOnePerGroupByUserId(long userId) {
        try {
            List<VideoWatchProgress> progressList = template.query("SELECT * FROM watch_progress JOIN video ON video.id = watch_progress.video_id WHERE user_id = ? ORDER BY last_watch_date desc", mapper, userId);
            Map<String, VideoWatchProgress> groupNameToWatchProgressMap = new LinkedHashMap<>();
            for (VideoWatchProgress progress: progressList) {
                String groupName = progress.getGroupName().orElse(null);
                VideoWatchProgress existingProgress = groupNameToWatchProgressMap.get(groupName);
                if (existingProgress == null || progress.isMoreRecentThen(existingProgress)) {
                    groupNameToWatchProgressMap.put(groupName, progress);
                }
            }
            return new ArrayList<>(groupNameToWatchProgressMap.values());
        } catch (RuntimeException e) {
            throw new VideoWatchProgressLookupByUserIdError(userId, e);
        }
    }

    @Override
    public List<VideoWatchProgress> findAllByGroupNameAndUserId(String groupName, long userId) {
        try {
            return template.query("SELECT * FROM watch_progress JOIN video ON video.id = watch_progress.video_id WHERE user_id = ? AND group_name = ?", mapper, userId, groupName);
        } catch (RuntimeException e) {
            throw new VideoWatchProgressLookupByUserIdAndGroupNameError(userId, groupName, e);
        }
    }

    public static class AbstractVideoWatchProgressLookupError extends RuntimeException {
        public AbstractVideoWatchProgressLookupError(String attributes, Throwable cause) {
            super(String.format("Failed to find video progress with %s. Reason: %s", attributes, cause.getMessage()), cause);
        }
    }

    public static class VideoWatchProgressLookupByUserIdError extends AbstractVideoWatchProgressLookupError {
        public VideoWatchProgressLookupByUserIdError(long userId, Throwable cause) {
            super(String.format("user ID %s", userId), cause);
        }
    }

    public static class VideoWatchProgressLookupByUserIdAndGroupNameError extends AbstractVideoWatchProgressLookupError {
        public VideoWatchProgressLookupByUserIdAndGroupNameError(long userId, String groupName, Throwable cause) {
            super(String.format("user ID %s and group name %s", userId, groupName), cause);
        }
    }
}
