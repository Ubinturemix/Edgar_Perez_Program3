package com.opinion;

/*
 * Author:     Edgar Perez
 * Assignment: Program 2
 * Class:      CSC 2040 Software Engineering
*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

//JUnit 5 tests for Review (valid/invalid creation, formatting).
public class ReviewTest {

    // Helper for asserting IllegalArgumentException.
    private static IllegalArgumentException iae(Executable e) {
        return assertThrows(IllegalArgumentException.class, e);
    }

    //Helper for checking that an exception message contains a substring (case-insensitive).
    private static void assertMsgHas(IllegalArgumentException ex, String needleLower) {
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains(needleLower));
    }

    //Happy path: valid inputs create a Review and format correctly.
    @Test
    void createValidReviewBuildsObjectAndFormatsToString() {
        // Use the compatibility factory to exercise string→enum mapping:
        Review r = Review.create("Widget", "TWO", "Nice item");
        assertAll(
                () -> assertEquals("Widget", r.name()),
                () -> assertEquals(Rating.TWO, r.rating()),
                () -> assertEquals("Nice item", r.comment()),
                () -> assertEquals("Widget with rating of TWO star(s): Nice item", r.toString())
        );
    }

    //Also verify direct constructor path (record + enum).
    @Test
    void directConstructorIsValidatedAndFormats() {
        Review r = new Review("Gadget", Rating.ONE, "ok");
        assertEquals("Gadget with rating of ONE star(s): ok", r.toString());
    }

    //Name must respect min/max length.
    @Test
    void createEnforcesNameLengthBounds() {
        assertMsgHas(iae(() -> Review.create("", "ONE", "ok")), "name");
        assertMsgHas(iae(() -> Review.create("X".repeat(65), "ONE", "ok")), "name");
    }

    //Comment must respect min/max length.
    @Test
    void createEnforcesCommentLengthBounds() {
        assertMsgHas(iae(() -> Review.create("A", "ONE", "")), "comment");
        String longComment = "Y".repeat(255);
        assertMsgHas(iae(() -> Review.create("A", "ONE", longComment)), "comment");
    }

    //Reject illegal characters and non-printable ASCII.
    @Test
    void createRejectsIllegalCharacters() {
        assertMsgHas(iae(() -> Review.create("Wi#dget", "ONE", "ok")), "#");
        assertMsgHas(iae(() -> Review.create("Widget", "ONE", "bad#comment")), "#");
        assertMsgHas(iae(() -> Review.create("Widget\n", "ONE", "ok")), "printable");
    }
}
