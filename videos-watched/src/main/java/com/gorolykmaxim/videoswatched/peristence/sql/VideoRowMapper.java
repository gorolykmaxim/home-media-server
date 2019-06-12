package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VideoRowMapper implements RowMapper<Video> {
    @Override
    public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long totalTime = rs.getLong("total_time");
        totalTime = rs.wasNull() ? null : totalTime;
        return new Video(rs.getLong("id"), rs.getString("name"),
                rs.getString("group_name"), totalTime);
    }
}
