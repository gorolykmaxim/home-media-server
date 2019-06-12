package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.User;
import com.gorolykmaxim.videoswatched.peristence.dao.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class SqlUserDao implements UserDao {
    private JdbcTemplate template;
    private UserRowMapper mapper;

    public SqlUserDao(JdbcTemplate template) {
        this.template = template;
        mapper = new UserRowMapper();
    }

    @Override
    public int count() {
        try {
            return template.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
        } catch (RuntimeException e) {
            throw new RecordsCountError("user", e);
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            User user = template.queryForObject("SELECT * FROM user WHERE id = ?", new Object[] {id}, mapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new UserLookupError(id, e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return template.query("SELECT * FROM user", mapper);
        } catch (RuntimeException e) {
            throw new UserFetchError(e);
        }
    }

    @Override
    public void updateById(long userId, String name) {
        try {
            template.update("UPDATE user SET name = ? WHERE id = ?", name, userId);
        } catch (RuntimeException e) {
            throw new UserUpdateError(userId, name, e);
        }
    }

    @Override
    public void create(long userId, String name) {
        try {
            template.update("INSERT INTO user VALUES(?, ?)", userId, name);
        } catch (RuntimeException e) {
            throw new UserCreationError(userId, name, e);
        }
    }

    public static class UserFetchError extends RuntimeException {
        public UserFetchError(Throwable cause) {
            super(String.format("Failed to fetch all known users. Reason: %s", cause.getMessage()), cause);
        }
    }

    public static class UserLookupError extends RuntimeException {
        public UserLookupError(long id, Throwable cause) {
            super(String.format("Failed to find a user with ID %s. Reason: %s", id, cause.getMessage()), cause);
        }
    }

    public static class UserUpdateError extends RuntimeException {
        public UserUpdateError(long id, String name, Throwable cause) {
            super(String.format("Failed to update user with ID %s with a new name %s. Reason: %s", id, name,
                    cause.getMessage()), cause);
        }
    }

    public static class UserCreationError extends RuntimeException {
        public UserCreationError(long id, String name, Throwable cause) {
            super(String.format("Failed to create user with ID %s and name %s. Reason: %s", id, name,
                    cause.getMessage()), cause);
        }
    }
}
