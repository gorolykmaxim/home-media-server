package com.gorolykmaxim.homemediaapp.filesystem;

import com.gorolykmaxim.homemediaapp.model.tvshow.Episode;
import com.gorolykmaxim.homemediaapp.model.tvshow.EpisodeFactory;
import com.gorolykmaxim.homemediaapp.model.tvshow.EpisodeRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.TvShow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemEpisodeRepository implements EpisodeRepository {
    private EpisodeFactory factory;

    public FileSystemEpisodeRepository(EpisodeFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Episode> findByTvShow(TvShow tvShow) throws EpisodeLookupError {
        try {
            return Files.walk(Paths.get(tvShow.getDirectory()))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .sorted()
                    .map(factory::create)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new EpisodeLookupError(tvShow, e);
        }
    }

    @Override
    public void deleteEpisodeOfShow(TvShow tvShow, String episodeName) {
        try {
            List<Path> pathsToDelete = Files.walk(Paths.get(tvShow.getDirectory()))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(episodeName))
                    .collect(Collectors.toList());
            for (Path path: pathsToDelete) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new EpisodeRemovalError(tvShow, episodeName, e);
        }
    }

    public static class EpisodeLookupError extends RuntimeException {
        public EpisodeLookupError(TvShow tvShow, Throwable cause) {
            super(String.format("Failed to find episode of '%s'. Reason: %s", tvShow, cause.getMessage()), cause);
        }
    }

    public static class EpisodeRemovalError extends RuntimeException {
        public EpisodeRemovalError(TvShow tvShow, String episodeName, Throwable cause) {
            super(String.format("Failed to remove episode '%s' of '%s'. Reason: %s", episodeName, tvShow, cause.getMessage()), cause);
        }
    }
}
