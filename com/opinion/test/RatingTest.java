package com.opinion;

/*
 * Author:     Edgar Perez
 * Assignment: Program 2
 * Class:      CSC 2040 Software Engineering
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;


//JUnit 5 tests for Rating.
public class RatingTest {


     //All enum constants must report a positive star count.
    @ParameterizedTest
    @EnumSource(Rating.class)
    void starsArePositive(Rating r) {
        assertTrue(r.getStars() > 0);
    }

    //Verify mapping between enum names and star values.
    @ParameterizedTest
    @CsvSource({
            "ONE,1",
            "TWO,2",
            "THREE,3"
    })
    void valueOfMapsToCorrectStars(String name, int stars) {
        assertEquals(stars, Rating.valueOf(name).getStars());
    }

    //Invalid enum names should be rejected by valueOf.
    @Test
    void valueOfRejectsInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> Rating.valueOf("FOUR"));
    }
}
