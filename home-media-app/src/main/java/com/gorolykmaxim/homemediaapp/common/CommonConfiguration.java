package com.gorolykmaxim.homemediaapp.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.NumberFormat;
import java.util.Locale;

@Configuration
public class CommonConfiguration {
    @Value("${home-media-app.common.duration-format}")
    private String durationFormat;
    @Value("${home-media-app.common.download-speed-format}")
    private String downloadSpeedFormat;

    @Bean
    public SizeFormatter sizeFormatter() {
        return new SizeFormatter();
    }

    @Bean
    public DurationFormatter durationFormatter() {
        return new DurationFormatter(durationFormat, false);
    }

    @Bean
    public DownloadSpeedFormatter downloadSpeedFormatter(SizeFormatter sizeFormatter) {
        return new DownloadSpeedFormatter(downloadSpeedFormat, sizeFormatter);
    }

    @Bean
    public NumberFormat percentFormat() {
        return NumberFormat.getPercentInstance(Locale.US);
    }

}
