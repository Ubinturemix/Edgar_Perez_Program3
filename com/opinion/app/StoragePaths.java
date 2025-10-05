package com.opinion.app;

import java.nio.file.Path;

public final class StoragePaths {
    private StoragePaths() {}

    public static Path baseDir() {
        String override = System.getProperty("opinion.dataDir");
        return Path.of(override != null ? override : "./data");
    }
}
