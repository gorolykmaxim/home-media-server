package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.view.EpisodeView;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeViewFactory;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeViewRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EpisodeViewControllerTest {
    private EpisodeViewFactory factory;
    private EpisodeViewRepository repository;
    private EpisodeViewController controller;
    private EpisodeViewPrototype prototype;

    @Before
    public void setUp() throws Exception {
        factory = Mockito.mock(EpisodeViewFactory.class);
        repository = Mockito.mock(EpisodeViewRepository.class);
        controller = new EpisodeViewController(factory, repository);
        prototype = new EpisodeViewPrototype();
        prototype.setEpisodeName("Some episode name");
    }

    @Test
    public void createEpisodeView() {
        EpisodeView view = Mockito.mock(EpisodeView.class);
        Mockito.when(factory.create(prototype.getEpisodeName())).thenReturn(view);
        controller.createEpisodeView(prototype);
        Mockito.verify(repository).save(view);
    }

    @Test(expected = EpisodeViewController.InvalidEpisodeViewError.class)
    public void failToCreateEpisodeViewDueToBadName() {
        Mockito.when(factory.create(prototype.getEpisodeName())).thenThrow(Mockito.mock(IllegalArgumentException.class));
        controller.createEpisodeView(prototype);
    }

    @Test(expected = EpisodeViewController.EpisodeViewCreationError.class)
    public void failToCreateEpisodeViewDueToError() {
        EpisodeView view = Mockito.mock(EpisodeView.class);
        Mockito.when(factory.create(prototype.getEpisodeName())).thenReturn(view);
        Mockito.doThrow(Mockito.mock(RuntimeException.class)).when(repository).save(view);
        controller.createEpisodeView(prototype);
    }
}
