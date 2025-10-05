package com.opinion;

/*
 * Author:     Edgar Perez
 * Assignment: Program 2
 * Class:      CSC 2040 Software Engineering
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

//JUnit 5 tests for ReviewMain.
public class ReviewMainTest {

    //Helper to run program with injected streams, ensure no System.setOut/err
    private static Result run(String... args) {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
        int code = ReviewMain.run(args, new PrintStream(outBuf), new PrintStream(errBuf));
        return new Result(code, outBuf.toString().trim(), errBuf.toString().trim());
    }

    //Simple holder for exit code and captured output.
    private record Result(int code, String out, String err) {}

    //Valid file → prints count, then one lowest, then one highest.
    @Test
    void printsCountAndExtremesForValidFile(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("ok.txt");
        Files.writeString(f, "A#ONE#a#\nZ#THREE#z#");

        Result r = run(f.toString());
        assertEquals(0, r.code);

        String[] lines = r.out.split("\\R+");
        assertEquals("2", lines[0], "first line should be count");
        assertTrue(lines[1].startsWith("A with rating of ONE star(s): a"));
        assertTrue(lines[2].startsWith("Z with rating of THREE star(s): z"));
        assertEquals("", r.err);
    }

    //No args → USAGE message + exit code 2.
    @Test
    void usageErrorWhenArgCountWrong() {
        Result r = run();
        assertEquals(2, r.code);
        assertTrue(r.err.startsWith("USAGE: java com.opinion.ReviewMain <path-to-reviews>"));
    }

    //Nonexistent file → IO error message + exit code 3.
    @Test
    void ioErrorIsReportedAndExitCode3(@TempDir Path dir) {
        Path missing = dir.resolve("does_not_exist.txt");
        Result r = run(missing.toString());
        assertEquals(3, r.code);
        assertTrue(r.err.startsWith("Error reading file:"));
    }

    //Invalid rating → format error message + exit code 4.
    @Test
    void badDataIsReportedAndExitCode4(@TempDir Path dir) throws Exception {
        Path f = dir.resolve("bad.txt");
        Files.writeString(f, "Name#FOUR#oops#"); // FOUR isn't valid

        Result r = run(f.toString());
        assertEquals(4, r.code);
        assertTrue(r.err.contains("Bad format: invalid rating 'FOUR'"));
    }
}
