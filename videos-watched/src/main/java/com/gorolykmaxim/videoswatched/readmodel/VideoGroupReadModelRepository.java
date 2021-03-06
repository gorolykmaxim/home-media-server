package com.gorolykmaxim.videoswatched.readmodel;

import com.gorolykmaxim.videoswatched.domain.video.Video;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VideoGroupReadModelRepository {
    private VideoRepository videoRepository;
    private String durationFormat;
    private Clock clock;

    public VideoGroupReadModelRepository(VideoRepository videoRepository, String durationFormat, Clock clock) {
        this.videoRepository = videoRepository;
        this.durationFormat = durationFormat;
        this.clock = clock;
    }

    public Iterable<VideoGroupReadModel> findAll() {
        List<VideoGroupReadModel> groups = new ArrayList<>();
        for (Video sourceVideo: videoRepository.findAll()) {
            if (!sourceVideo.hasAllInformation()) {
                continue;
            }
            String sourceVideoGroupName = sourceVideo.getRelativePath().getName(0).toString();
            Optional<VideoGroupReadModel> possibleGroup = groups
                    .stream()
                    .filter(g -> sourceVideoGroupName.equals(g.getName()))
                    .findFirst();
            VideoGroupReadModel group;
            if (possibleGroup.isPresent()) {
                group = possibleGroup.get();
            } else {
                group = new VideoGroupReadModel(sourceVideoGroupName);
                groups.add(group);
            }
            VideoReadModel video = new VideoReadModel(
                    sourceVideo.getName(),
                    sourceVideo.getTimePlayed(),
                    sourceVideo.getTotalTime(),
                    sourceVideo.getLastPlayDate(),
                    durationFormat,
                    clock
            );
            group.addVideo(video);
        }
        groups.sort(null);
        return groups;
    }
}
