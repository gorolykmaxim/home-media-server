package com.gorolykmaxim.torrentui.qbittorrent;

import com.gorolykmaxim.torrentui.common.DownloadSpeed;
import com.gorolykmaxim.torrentui.common.DownloadSpeedFormatter;
import com.gorolykmaxim.torrentui.common.DurationFormatter;
import com.gorolykmaxim.torrentui.common.SizeFormatter;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QbittorrentFactory {

    private SizeFormatter sizeFormatter;
    private DurationFormatter durationFormatter;
    private DownloadSpeedFormatter downloadSpeedFormatter;
    private NumberFormat percentageFormat;
    private final Set<String> mandatoryFields;

    public QbittorrentFactory(SizeFormatter sizeFormatter, DurationFormatter durationFormatter, DownloadSpeedFormatter downloadSpeedFormatter, NumberFormat percentageFormat) {
        this.sizeFormatter = sizeFormatter;
        this.durationFormatter = durationFormatter;
        this.downloadSpeedFormatter = downloadSpeedFormatter;
        this.percentageFormat = percentageFormat;
        mandatoryFields = new HashSet<>(Arrays.asList("hash", "name", "size", "progress", "dlspeed", "eta", "state"));
    }

    public Qbittorrent create(Map<String, String> qbittorrent) throws MissingMandatoryFieldsError {
        Set<String> missingMandatoryFields = new HashSet<>();
        for (String mandatoryField: mandatoryFields) {
            if (!qbittorrent.containsKey(mandatoryField)) {
                missingMandatoryFields.add(mandatoryField);
            }
        }
        if (!missingMandatoryFields.isEmpty()) {
            throw new MissingMandatoryFieldsError(missingMandatoryFields);
        }
        return new Qbittorrent(
                qbittorrent.get("hash"),
                qbittorrent.get("name"),
                Long.parseLong(qbittorrent.get("size")),
                Double.parseDouble(qbittorrent.get("progress")),
                qbittorrent.get("state"),
                new DownloadSpeed(Long.parseLong(qbittorrent.get("dlspeed")), "second"),
                Duration.ofSeconds(Long.parseLong(qbittorrent.get("eta"))),
                sizeFormatter,
                percentageFormat,
                durationFormatter,
                downloadSpeedFormatter);
    }

    public static class MissingMandatoryFieldsError extends IllegalArgumentException {
        public MissingMandatoryFieldsError(Set<String> missingMandatoryFields) {
            super(String.format("Following mandatory arguments are missing: %s", missingMandatoryFields));
        }
    }

}
