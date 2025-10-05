/*
 Author:     Edgar Perez
 Assignment: Program 1 - Reviews, Reduced Boilerplate
 Class:      CSC 2040
 */


package com.opinion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


 //Utility for reading records from a text file.
 //Expected format per-line: name#RATING#comment
 //Optional trailing '#' after comment tolerated.
public final class ReviewReader {

    //Field delimiter used in the input format.
    private static final char DELIM = '#';

     //Regex-ready string version of to avoid magic-literals.
     private static final String DELIM_STR = String.valueOf(DELIM);


     //Regex-ready string version of {@link #DELIM} to avoid magic-literals
     private ReviewReader() { }

    /*
     Reads reviews from path. Blank lines are ignored.

     @param path path to input file
     @return list of parsed reviews
     @throws IOException if file can't be read
     @throws IllegalArgumentException if a line is malformed
     */
    public static List<Review> read(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Review> out = new ArrayList<>(Math.max(0, lines.size()));
        for (String raw : lines) {
            if (raw == null) continue;
            String line = raw.strip();
            if (line.isEmpty()) continue;
            out.add(parse(line));
        }
        return out;
    }

    //Parse single line "name#RATING#comment" (or with trailing '#')
    static Review parse(String line) {
        String work = line;
        if (!work.isEmpty() && work.charAt(work.length() - 1) == DELIM) {
            work = work.substring(0, work.length() - 1);
        }
        String[] parts = work.split("#", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Bad format: expected 3 fields separated by '#': " + line);
        }

        String name = parts[0];
        String ratingStr = parts[1];
        String comment = parts[2];

        final Rating rating;
        try {
            rating = Rating.valueOf(ratingStr);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Bad format: invalid rating '" + ratingStr + "'");
        }

        return new Review(name, rating, comment);
    }
}
