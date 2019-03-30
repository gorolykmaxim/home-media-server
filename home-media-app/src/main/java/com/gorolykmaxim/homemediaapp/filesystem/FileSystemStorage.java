package com.gorolykmaxim.homemediaapp.filesystem;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
import com.gorolykmaxim.homemediaapp.model.tvshow.*;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeStorage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemStorage implements EpisodeStorage, TvShowStorage {
    private PathResolver pathResolver;

    public FileSystemStorage(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    @Override
    public List<String> findEpisodeNamesByTvShow(TvShow tvShow) throws EpisodeLookupError {
        try {
            return Files.walk(Paths.get(pathResolver.resolve(tvShow.getDirectory())))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(FilenameUtils::removeExtension)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new EpisodeLookupError(tvShow, e);
        }
    }

    @Override
    public void deleteEpisodeOfShow(TvShow tvShow, String episodeName) {
        try {
            List<Path> pathsToDelete = Files.walk(Paths.get(pathResolver.resolve(tvShow.getDirectory())))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(episodeName))
                    .collect(Collectors.toList());
            for (Path path: pathsToDelete) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new EpisodeRemovalError(tvShow, episodeName, e);
        }
    }

    @Override
    public void store(TvShow tvShow) {
        new File(pathResolver.resolve(tvShow.getDirectory())).mkdir();

    }

    @Override
    public void delete(TvShow tvShow) {
        try {
            FileUtils.deleteDirectory(new File(pathResolver.resolve(tvShow.getDirectory())));
        } catch (IOException e) {
            throw new TvShowRemovalError(tvShow, e);
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

    public static class TvShowRemovalError extends RuntimeException {
        public TvShowRemovalError(TvShow tvShow, Throwable cause) {
            super(String.format("Failed to remove TV Show '%s'. Reason: %s", tvShow, cause.getMessage()), cause);
        }
    }
}
