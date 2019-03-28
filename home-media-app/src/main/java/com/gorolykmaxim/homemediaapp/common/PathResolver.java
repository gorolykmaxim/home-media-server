package com.gorolykmaxim.homemediaapp.common;

import java.nio.file.Paths;

public class PathResolver {
    private String rootDirectory;

    public PathResolver(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String resolve(String name) {
        return Paths.get(rootDirectory, name).toString();
    }
}
