package com.opinion;

/*
 * Author:     Edgar Perez
 * Assignment: Program 2
 * Class:      CSC 2040 Software Engineering
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

 //JUnit 5 tests for ReviewReader.
public class ReviewReaderTest {

    //write given content to a file.
    private static Path write(Path dir, String name, String content) throws Exception {
        Path p = dir.resolve(name);
        Files.writeString(p, content, UTF_8);
        return p;
    }

    //Happy path, two lines, second with trailing '#', preserve order.
    @Test
    void readParsesMultipleLines(@TempDir Path tmp) throws Exception {
        Path file = write(tmp, "reviews.txt",
                "Alpha#ONE#ok\n" +
                        "Beta#THREE#awesome#\n");

        List<Review> rs = ReviewReader.read(file);
        assertEquals(2, rs.size());
        assertEquals("Alpha", rs.get(0).name());
        assertEquals("ok", rs.get(0).comment());
        assertEquals("Beta", rs.get(1).name());
        assertEquals("awesome", rs.get(1).comment());
    }

    //Optional trailing '#' after  comment is accepted; blank lines ignored.
    @Test
    void acceptsOptionalTrailingHashAndIgnoresBlanks(@TempDir Path tmp) throws Exception {
        Path file = write(tmp, "mixed.txt",
                "\n" +
                        "A#TWO#meh#\n" +      // trailing '#'
                        "   \n" +              // whitespace-only line
                        "B#ONE#bad\n");

        List<Review> rs = ReviewReader.read(file);
        assertEquals(2, rs.size());
        assertEquals("A", rs.get(0).name());
        assertEquals("B", rs.get(1).name());
    }

    //Malformed lines (wrong field count) should throw with a useful message.
    @Test
    void rejectsWrongFieldCount(@TempDir Path tmp) throws Exception {
        // Only two fields
        Path two = write(tmp, "two.txt", "X#ONE\n");
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> ReviewReader.read(two));
        assertTrue(ex1.getMessage().contains("expected 3 fields"));

        //Four fields (no trailing '#', so it's truly 4)
        Path four = write(tmp, "four.txt", "Y#TWO#c#extra\n");
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> ReviewReader.read(four));
        assertTrue(ex2.getMessage().contains("expected 3 fields"));
    }

    //Invalid rating name should be reported clearly.
    @Test
    void rejectsInvalidRating(@TempDir Path tmp) throws Exception {
        Path bad = write(tmp, "bad.txt", "Name#FOUR#oops\n");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ReviewReader.read(bad));
        assertTrue(ex.getMessage().contains("invalid rating 'FOUR'"));
    }
}
