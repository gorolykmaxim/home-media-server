package com.gorolykmaxim.videoswatched.persistence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.sql.VideoRowMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VideoRowMapperTest {
    private ResultSet resultSet;
    private VideoRowMapper mapper;

    @Before
    public void setUp() throws Exception {
        resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong("id")).thenReturn(1l);
        Mockito.when(resultSet.getString("name")).thenReturn("Full Season 3");
        Mockito.when(resultSet.getString("group_name")).thenReturn("Black Mirror");
        Mockito.when(resultSet.getLong("total_time")).thenReturn(324325436365236l);
        mapper = new VideoRowMapper();
    }

    @Test
    public void shouldPopulateNullFieldsWithNullValues() throws SQLException {
        // given
        Mockito.when(resultSet.getLong("total_time")).thenReturn(0l);
        Mockito.when(resultSet.wasNull()).thenReturn(true);
        // when
        Video video = mapper.mapRow(resultSet, 1);
        // then
        Assert.assertEquals(resultSet.getLong("id"), video.getId());
        Assert.assertTrue(video.getName().isPresent());
        Assert.assertEquals(resultSet.getString("name"), video.getName().get());
        Assert.assertTrue(video.getGroupName().isPresent());
        Assert.assertEquals(resultSet.getString("group_name"), video.getGroupName().get());
        Assert.assertFalse(video.getTotalTime().isPresent());
    }

    @Test
    public void shouldPopulateFields() throws SQLException {
        // given
        Mockito.when(resultSet.wasNull()).thenReturn(false);
        // when
        Video video = mapper.mapRow(resultSet, 1);
        // then
        Assert.assertEquals(resultSet.getLong("id"), video.getId());
        Assert.assertTrue(video.getName().isPresent());
        Assert.assertEquals(resultSet.getString("name"), video.getName().get());
        Assert.assertTrue(video.getGroupName().isPresent());
        Assert.assertEquals(resultSet.getString("group_name"), video.getGroupName().get());
        Assert.assertTrue(video.getTotalTime().isPresent());
        Assert.assertEquals((Long)resultSet.getLong("total_time"), video.getTotalTime().get());
    }
}
