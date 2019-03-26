package com.gorolykmaxim.homemediaapp.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.NumberFormat;
import java.util.Locale;

@Configuration
public class CommonConfiguration {

    @Bean
    public SizeFormatter sizeFormatter() {
        return new SizeFormatter();
    }

    @Bean
    public DurationFormatter durationFormatter() {
        return new DurationFormatter("H:mm:ss", false);
    }

    @Bean
    public DownloadSpeedFormatter downloadSpeedFormatter(SizeFormatter sizeFormatter) {
        return new DownloadSpeedFormatter(sizeFormatter);
    }

    @Bean
    public NumberFormat percentFormat() {
        return NumberFormat.getPercentInstance(Locale.US);
    }

}
