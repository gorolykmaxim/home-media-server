package com.gorolykmaxim.homemediaapp.common;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

public class DurationFormatter {

    private String format;
    private boolean padWithZeros;

    public DurationFormatter(String format, boolean padWithZeros) {
        this.format = format;
        this.padWithZeros = padWithZeros;
    }

    public String format(Duration duration) {
        return DurationFormatUtils.formatDuration(duration.toMillis(), format, padWithZeros);
    }
}
