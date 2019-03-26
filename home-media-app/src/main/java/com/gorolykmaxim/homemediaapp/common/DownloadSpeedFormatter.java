package com.gorolykmaxim.homemediaapp.common;

public class DownloadSpeedFormatter {

    private SizeFormatter sizeFormatter;

    public DownloadSpeedFormatter(SizeFormatter sizeFormatter) {
        this.sizeFormatter = sizeFormatter;
    }

    public String format(DownloadSpeed speed) {
        return String.format("%s / %s", sizeFormatter.format(speed.getSize()), speed.getPeriod());
    }

}
