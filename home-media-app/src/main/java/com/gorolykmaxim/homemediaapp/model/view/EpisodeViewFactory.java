package com.gorolykmaxim.homemediaapp.model.view;

public class EpisodeViewFactory {
    public EpisodeView create(String episodeName) throws IllegalArgumentException {
        if (episodeName == null) {
            throw new IllegalArgumentException("'episodeName' is not defined");
        }
        if (episodeName.isEmpty()) {
            throw new IllegalArgumentException("'episodeName' should not be empty");
        }
        return new EpisodeView(episodeName);
    }
}
