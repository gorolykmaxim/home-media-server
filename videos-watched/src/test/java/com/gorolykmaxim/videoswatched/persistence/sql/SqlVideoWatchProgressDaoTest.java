package com.gorolykmaxim.videoswatched.persistence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoWatchProgressDao;
import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import com.gorolykmaxim.videoswatched.peristence.sql.SqlVideoWatchProgressDao;
import com.gorolykmaxim.videoswatched.peristence.sql.VideoWatchProgressRowMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlVideoWatchProgressDaoTest {
    private JdbcTemplate template;
    private VideoWatchProgressDao dao;

    @Before
    public void setUp() throws Exception {
        template = Mockito.mock(JdbcTemplate.class);
        dao = new SqlVideoWatchProgressDao(template);
    }

    @Test
    public void shouldReturnTheLatestWatchedEpisodeOfEachShow() {
        // given
        long userId = 1l;
        String show1 = "Orange is the new Black";
        String show2 = "How i met your mother";
        String show3 = "Friends";
        String episode1 = "Episode 1";
        String episode2 = "Episode 2";
        String episode3 = "Episode 3";
        VideoWatchProgress show1Episode2 = new VideoWatchProgress(
                new Video(2l, episode2, show1, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now())
        );
        VideoWatchProgress show1Episode1 = new VideoWatchProgress(
                new Video(1l, episode1, show1, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now().minusMinutes(45))
        );
        VideoWatchProgress show3Episode3 = new VideoWatchProgress(
                new Video(6l, episode3, show3, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now().minusDays(1))
        );
        VideoWatchProgress show2Episode2 = new VideoWatchProgress(
                new Video(3l, episode2, show2, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now().minusDays(2))
        );
        VideoWatchProgress show3Episode2 = new VideoWatchProgress(
                new Video(5l, episode2, show3, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now().minusDays(3))
        );
        VideoWatchProgress show3Episode1 = new VideoWatchProgress(
                new Video(4l, episode1, show3, null),
                new WatchProgress(0l, 1l, 15l, LocalDateTime.now().minusDays(4))
        );
        List<VideoWatchProgress> progressList = new ArrayList<>();
        progressList.add(show1Episode2);
        progressList.add(show1Episode1);
        progressList.add(show3Episode3);
        progressList.add(show2Episode2);
        progressList.add(show3Episode2);
        progressList.add(show3Episode1);
        Mockito.when(template.query(Mockito.eq("SELECT * FROM watch_progress JOIN video ON video.id = " +
                "watch_progress.video_id WHERE user_id = ? ORDER BY last_watch_date desc"),
                Mockito.any(VideoWatchProgressRowMapper.class), Mockito.eq(userId)))
            .thenReturn(progressList);
        List<VideoWatchProgress> expectedList = new ArrayList<>();
        expectedList.add(show1Episode2);
        expectedList.add(show3Episode3);
        expectedList.add(show2Episode2);
        // when
        List<VideoWatchProgress> lastWatchPerGroup = dao.findOnePerGroupByUserId(userId);
        // then
        Assert.assertEquals(expectedList, lastWatchPerGroup);
    }
}
