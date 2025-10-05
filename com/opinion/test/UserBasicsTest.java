package com.opinion.test;

import com.opinion.app.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserBasicsTest {

    @Test void reviewerValidSpecialization() {
        Reviewer r = new Reviewer("r@x.y", "Rita Doe", LocalDate.of(2020,1,2), "Apparel & Footwear");
        assertEquals("Apparel & Footwear", r.getSpecialization());
    }

    @Test void reviewerInvalidSpecializationTooShort() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reviewer("r@x.y", "Rita Doe", LocalDate.of(2020,1,2), "TooShort")
        );
    }

    @Test void invalidEmailRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reviewer("bad email", "Rita Doe", LocalDate.of(2020,1,2), "Apparel Retail ***")
        );
    }

    @Test void adminLastAccessNullThenUpdated() {
        Administrator a = new Administrator("admin@x.y", "Alice Admin", LocalDate.of(2020,2,2));
        assertNull(a.getLastAccessDate());
        a.touchAccessNow();
        assertNotNull(a.getLastAccessDate());
    }
}
