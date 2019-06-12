package com.gorolykmaxim.videoswatched.peristence.dao;

import com.gorolykmaxim.videoswatched.peristence.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao {
    Optional<User> findById(long id);
    List<User> findAll();
    void updateById(long userId, String name);
    void create(long userId, String name);
}
