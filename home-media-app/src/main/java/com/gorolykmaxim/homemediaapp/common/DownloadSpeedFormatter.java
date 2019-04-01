package com.gorolykmaxim.homemediaapp.common;

public class DownloadSpeedFormatter {

    private SizeFormatter sizeFormatter;
    private String downloadSpeedFormat;

    public DownloadSpeedFormatter(String downloadSpeedFormat, SizeFormatter sizeFormatter) {
        this.downloadSpeedFormat = downloadSpeedFormat;
        this.sizeFormatter = sizeFormatter;
    }

    public String format(DownloadSpeed speed) {
        return String.format(downloadSpeedFormat, sizeFormatter.format(speed.getSize()), speed.getPeriod());
    }

}
