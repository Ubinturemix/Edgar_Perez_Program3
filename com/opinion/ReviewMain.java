
/*
 * Author:     Edgar Perez
 * Assignment: Program 1 - Reviews, Reduced Boilerplate
 * Class:      CSC 2040
*/

package com.opinion;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;



/*
 Console entry point for Program 1.

 Responsibilities:

 Read reviews from a file via ReviewReader
 Print the number of reviews, then the lowest and highest rated reviews.
 Exit with appropriate status codes on error

 */
public final class ReviewMain {


    //Exit code for bad usage, wrong argument count
    private static final int EXIT_BAD_USAGE = 2;

    //Exit code for I/O error while reading file
    private static final int EXIT_IO_ERROR = 3;


    //Exit code for malformed review data
    private static final int EXIT_BAD_DATA = 4;

    //Non-instantiable utility class.
    private ReviewMain() { }






    /*
    Runs the application with the given arguments and output streams.

     @param args command line arguments, must contain exactly one file path
     @param out  standard output stream
     @param err  error output stream
     @return process exit code: 0 on success, one of #EXIT_BAD_USAGE,
     #EXIT_IO_ERROR, or #EXIT_BAD_DATA on error
     */
    public static int run(String[] args, PrintStream out, PrintStream err) {
        if (args == null || args.length != 1) {
            err.println("USAGE: java com.opinion.ReviewMain <path-to-reviews>");
            return EXIT_BAD_USAGE;
        }

        final List<Review> reviews;
        try {
            reviews = ReviewReader.read(Path.of(args[0]));
        } catch (IOException e) {
            err.println("Error reading file: " + e.getMessage());
            return EXIT_IO_ERROR;
        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
            return EXIT_BAD_DATA;
        }

        out.println(reviews.size());
        if (reviews.isEmpty()) {
            return 0;
        }

        Review lowest = reviews.get(0);
        Review highest = reviews.get(0);
        for (int i = 1; i < reviews.size(); i++) {
            Review r = reviews.get(i);
            if (r.rating().getStars() < lowest.rating().getStars()) {
                lowest = r;
            }
            if (r.rating().getStars() > highest.rating().getStars()) {
                highest = r;
            }
        }

        out.println(lowest);
        out.println(highest);
        return 0;
    }



     //Standard entry point
     //@param args command line arguments
    public static void main(String[] args) {
        System.exit(run(args, System.out, System.err));
    }
}
