package com.gorolykmaxim.homemediaapp.service.view.tvshow.episode;

public class ViewableEpisodeFactory {
    public ViewableEpisode create(String name, boolean viewed) {
        return new ViewableEpisode(name, viewed);
    }
}
