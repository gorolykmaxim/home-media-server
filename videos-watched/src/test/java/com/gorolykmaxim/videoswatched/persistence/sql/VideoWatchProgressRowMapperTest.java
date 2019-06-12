package com.gorolykmaxim.videoswatched.persistence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.sql.VideoWatchProgressRowMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class VideoWatchProgressRowMapperTest {
    private ResultSet resultSet;
    private VideoWatchProgressRowMapper mapper;

    @Before
    public void setUp() throws Exception {
        resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("name")).thenReturn("Episode 15");
        Mockito.when(resultSet.getString("group_name")).thenReturn("Narcos");
        Mockito.when(resultSet.getLong("total_time")).thenReturn(52345235l);
        Mockito.when(resultSet.getLong("video_id")).thenReturn(15l);
        Mockito.when(resultSet.getLong("user_id")).thenReturn(1l);
        Mockito.when(resultSet.getLong("time_played")).thenReturn(23423l);
        Mockito.when(resultSet.getTimestamp("last_watch_date"))
                .thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        mapper = new VideoWatchProgressRowMapper();
    }

    @Test
    public void shouldPopulateNullFieldsWithNullValues() throws SQLException {
        // given
        Mockito.when(resultSet.getLong("total_time")).thenReturn(0l);
        Mockito.when(resultSet.wasNull()).thenReturn(true);
        // when
        VideoWatchProgress progress = mapper.mapRow(resultSet, 0);
        // then
        Assert.assertTrue(progress.getName().isPresent());
        Assert.assertEquals(resultSet.getString("name"), progress.getName().get());
        Assert.assertTrue(progress.getGroupName().isPresent());
        Assert.assertEquals(resultSet.getString("group_name"), progress.getGroupName().get());
        Assert.assertFalse(progress.getTotalTime().isPresent());
        Assert.assertEquals(resultSet.getLong("video_id"), progress.getId());
        Assert.assertEquals(resultSet.getLong("user_id"), progress.getUserId());
        Assert.assertEquals(resultSet.getLong("time_played"), progress.getTimePlayed());
        Assert.assertEquals(resultSet.getTimestamp("last_watch_date").toLocalDateTime(), progress.getLastWatchDate());
    }

    @Test
    public void shouldPopulateFields() throws SQLException {
        // given
        Mockito.when(resultSet.wasNull()).thenReturn(false);
        // when
        VideoWatchProgress progress = mapper.mapRow(resultSet, 0);
        // then
        Assert.assertTrue(progress.getName().isPresent());
        Assert.assertEquals(resultSet.getString("name"), progress.getName().get());
        Assert.assertTrue(progress.getGroupName().isPresent());
        Assert.assertEquals(resultSet.getString("group_name"), progress.getGroupName().get());
        Assert.assertTrue(progress.getTotalTime().isPresent());
        Assert.assertEquals((Long)resultSet.getLong("total_time"), progress.getTotalTime().get());
        Assert.assertEquals(resultSet.getLong("video_id"), progress.getId());
        Assert.assertEquals(resultSet.getLong("user_id"), progress.getUserId());
        Assert.assertEquals(resultSet.getLong("time_played"), progress.getTimePlayed());
        Assert.assertEquals(resultSet.getTimestamp("last_watch_date").toLocalDateTime(), progress.getLastWatchDate());
    }
}
