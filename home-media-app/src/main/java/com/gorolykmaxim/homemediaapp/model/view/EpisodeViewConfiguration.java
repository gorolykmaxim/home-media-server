package com.gorolykmaxim.homemediaapp.model.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EpisodeViewConfiguration {
    @Bean
    public EpisodeViewFactory factory() {
        return new EpisodeViewFactory();
    }
}
