package com.gorolykmaxim.torrentui.common;

import java.util.Objects;

public class DownloadSpeed {
    private final long size;
    private final String period;

    public DownloadSpeed(long size, String period) {
        this.size = size;
        this.period = period;
    }

    public long getSize() {
        return size;
    }

    public String getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadSpeed that = (DownloadSpeed) o;
        return size == that.size &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, period);
    }
}
