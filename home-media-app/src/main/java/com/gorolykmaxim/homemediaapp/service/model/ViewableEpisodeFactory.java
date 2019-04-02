package com.gorolykmaxim.homemediaapp.service.model;

public class ViewableEpisodeFactory {
    public ViewableEpisode create(String name, boolean viewed) {
        return new ViewableEpisode(name, viewed);
    }
}
