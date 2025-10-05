/*
 Author:     Edgar Perez
 Assignment: Program 1 - Reviews, Reduced Boilerplate
 Class:      CSC 2040
 */
package com.opinion;

import java.io.Serializable;


import java.io.Serializable;

import java.util.Objects;


/*
 Immutable value type representing a single product review.
 Each review contains a product name, a Rating, and a comment.
 */
public record Review(String name, Rating rating, String comment) implements Serializable {

    private static final long serialVersionUID = 1L;


    //Maximum allowed product name length
    private static final int MAX_NAME_LEN = 64;

    //Maximum allowed comment length
    private static final int MAX_COMMENT_LEN = 254;



    /*
     Canonical constructor validates fields.

     @param name    product name
     @param rating  rating enum value
     @param comment review comment
     @throws NullPointerException if any argument is null
     @throws IllegalArgumentException if constraints are violated
     */
    public Review {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(rating, "rating");
        Objects.requireNonNull(comment, "comment");

        if (name.length() < 1 || name.length() > MAX_NAME_LEN) {
            throw new IllegalArgumentException("Bad format: name length out of range");
        }
        if (comment.length() < 1 || comment.length() > MAX_COMMENT_LEN) {
            throw new IllegalArgumentException("Bad format: comment length out of range");
        }

        ensurePrintableAsciiNoHash("name", name);
        ensurePrintableAsciiNoHash("comment", comment);
    }



    /*
     Factory method for constructing a Review} from raw strings.

     @param rawName    product name string
     @param rawRating  rating string, must match
     @param rawComment comment string
     @return constructed review
     @throws NullPointerException if any parameter is null
     @throws IllegalArgumentException if validation fails or rating is invalid
     */
    public static Review create(String rawName, String rawRating, String rawComment) {
        Objects.requireNonNull(rawName, "Bad format: missing name");
        Objects.requireNonNull(rawRating, "Bad format: missing rating");
        Objects.requireNonNull(rawComment, "Bad format: missing comment");

        final Rating rating = Rating.valueOf(rawRating);
        return new Review(rawName, rating, rawComment);
    }


    /*
     Ensures that a string contains only printable ASCII characters
     @param label field name for error messages
     @param s string to validate
     @throws IllegalArgumentException if string contains illegal characters
     */
    private static void ensurePrintableAsciiNoHash(String label, String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '#') {
                throw new IllegalArgumentException("Bad format: " + label + " contains '#'");
            }
            if (c < 32 || c > 126) {
                throw new IllegalArgumentException("Bad format: " + label + " contains non-printable ASCII");
            }
        }
    }



    /*
     Returns a human-readable string representation of review.
     @return formatted summary string
     */
    @Override
    public String toString() {
        return name + " with rating of " + rating.name() + " star(s): " + comment;
    }
}
