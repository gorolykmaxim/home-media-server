package com.gorolykmaxim.videoswatched.service.eventprocessor;

import com.gorolykmaxim.videoswatched.peristence.model.User;
import com.gorolykmaxim.videoswatched.peristence.dao.UserDao;
import com.gorolykmaxim.videoswatched.service.event.UserEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.Optional;

public class UserEventsProcessorTest {
    private User user;
    private UserDao dao;
    private UserEventsProcessor processor;

    @Before
    public void setUp() throws Exception {
        user = new User(1, "tin-tin");
        dao = Mockito.mock(UserDao.class);
        processor = new UserEventsProcessor(dao, Mockito.mock(ConsumerSeekAware.class));
    }

    @Test
    public void shouldUpdateExistingUserName() {
        // given
        UserEvent event = new UserEvent();
        event.setUserId(user.getId());
        event.setUserName("ton-ton-2");
        Mockito.when(dao.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        processor.handle(event);
        // then
        Mockito.verify(dao).updateById(event.getUserId(), event.getUserName());
    }

    @Test
    public void shouldNotUpdateExistingUserNameSinceItHasNotChanged() {
        // given
        UserEvent event = new UserEvent();
        event.setUserId(user.getId());
        event.setUserName(user.getName());
        Mockito.when(dao.findById(user.getId())).thenReturn(Optional.of(user));
        // when
        processor.handle(event);
        // then
        Mockito.verify(dao, Mockito.never()).updateById(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    public void shouldCreateNewUser() {
        // given
        UserEvent event = new UserEvent();
        event.setUserId(user.getId());
        event.setUserName(user.getName());
        Mockito.when(dao.findById(user.getId())).thenReturn(Optional.empty());
        // when
        processor.handle(event);
        // then
        Mockito.verify(dao).create(user.getId(), user.getName());
    }

    @Test
    public void shouldCatchAnyError() {
        // given
        UserEvent event = new UserEvent();
        event.setUserId(user.getId());
        event.setUserName(user.getName());
        Mockito.when(dao.findById(Mockito.anyLong())).thenThrow(new RuntimeException());
        // when
        processor.handle(event);
    }
}
