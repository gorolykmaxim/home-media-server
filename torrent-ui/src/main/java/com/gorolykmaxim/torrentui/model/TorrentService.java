package com.gorolykmaxim.torrentui.model;

import java.util.List;
import java.util.Map;

public interface TorrentService {
    List<DownloadingTorrent> find(Map<String, String> parameters);
    void downloadViaMagnetLink(String magnetLink, String downloadFolder);
    void deleteTorrentById(String id, boolean deleteData);
}
