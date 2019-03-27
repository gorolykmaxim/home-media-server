package com.gorolykmaxim.homemediaapp.model.torrent;

public interface DownloadingTorrent {
    String getId();
    String getName();
    String getSize();
    boolean isComplete();
    String getProgress();
    String getState();
    String getDownloadSpeed();
    String getTimeRemaining();
}
