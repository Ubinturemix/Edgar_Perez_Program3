/*
 Author:     Edgar Perez
 Assignment: Program 3 - Persistent Reviews
 Class:      CSC 2040
 */

package com.opinion.app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.opinion.Review;
import com.opinion.Rating;
import com.opinion.app.User;
import com.opinion.app.Reviewer;

public final class DataStoreFile implements DataStore {

    private static final DataStoreFile INSTANCE = new DataStoreFile();
    public static DataStoreFile getInstance() { return INSTANCE; }
    private DataStoreFile() {}

    private Path usersFile()   { return StoragePaths.baseDir().resolve("users.dat"); }
    private Path reviewsFile() { return StoragePaths.baseDir().resolve("reviews.dat"); }

    //TODO: Implement all DataStore methods tomorrow (read -> modify -> write)
}
