package com.gorolykmaxim.torrentui.common;

import org.apache.commons.io.FileUtils;

public class SizeFormatter {
    public String format(long bytes) {
        return FileUtils.byteCountToDisplaySize(bytes);
    }
}
