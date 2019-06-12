package com.gorolykmaxim.videoswatched.service.api;

import com.gorolykmaxim.videoswatched.peristence.model.User;
import com.gorolykmaxim.videoswatched.peristence.dao.UserDao;
import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoWatchProgressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private UserDao userDao;
    private VideoWatchProgressDao watchProgressDao;

    @Autowired
    public MainController(UserDao userDao, VideoWatchProgressDao watchProgressDao) {
        this.userDao = userDao;
        this.watchProgressDao = watchProgressDao;
    }

    @GetMapping("user")
    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    @GetMapping("user/{id}/video")
    public List<VideoWatchProgress> findVideosWatchedByUserLatelyForEachGroup(@PathVariable long id) {
        return watchProgressDao.findOnePerGroupByUserId(id);
    }

    @GetMapping("user/{id}/group/{groupName}")
    public List<VideoWatchProgress> findVideosWatchedByUserInGroup(@PathVariable long id, @PathVariable String groupName) {
        return watchProgressDao.findAllByGroupNameAndUserId(groupName, id);
    }

}
