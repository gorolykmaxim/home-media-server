package com.gorolykmaxim.torrentui.qbittorrent;

import com.gorolykmaxim.torrentui.common.DownloadSpeed;
import com.gorolykmaxim.torrentui.common.DownloadSpeedFormatter;
import com.gorolykmaxim.torrentui.common.DurationFormatter;
import com.gorolykmaxim.torrentui.common.SizeFormatter;
import com.gorolykmaxim.torrentui.model.DownloadingTorrent;

import java.time.Duration;
import java.util.Objects;

public class Qbittorrent implements DownloadingTorrent {

    private final String id, name, state;
    private final long size;
    private final double progress;
    private final DownloadSpeed downloadSpeed;
    private final Duration timeRemaining;
    private SizeFormatter sizeFormatter;
    private DurationFormatter durationFormatter;
    private DownloadSpeedFormatter downloadSpeedFormatter;

    Qbittorrent(String id, String name, long size, double progress, String state, DownloadSpeed downloadSpeed, Duration timeRemaining,
                SizeFormatter sizeFormatter, DurationFormatter durationFormatter, DownloadSpeedFormatter downloadSpeedFormatter) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.progress = progress;
        this.state = state;
        this.downloadSpeed = downloadSpeed;
        this.timeRemaining = timeRemaining;
        this.sizeFormatter = sizeFormatter;
        this.durationFormatter = durationFormatter;
        this.downloadSpeedFormatter = downloadSpeedFormatter;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSize() {
        return sizeFormatter.format(size);
    }

    @Override
    public boolean isComplete() {
        return progress == 1;
    }

    @Override
    public String getProgress() {
        return Double.toString(progress);
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getDownloadSpeed() {
        return downloadSpeedFormatter.format(downloadSpeed);
    }

    @Override
    public String getTimeRemaining() {
        return durationFormatter.format(timeRemaining);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qbittorrent that = (Qbittorrent) o;
        return size == that.size &&
                Double.compare(that.progress, progress) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(downloadSpeed, that.downloadSpeed) &&
                Objects.equals(timeRemaining, that.timeRemaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, progress, downloadSpeed, timeRemaining);
    }
}
