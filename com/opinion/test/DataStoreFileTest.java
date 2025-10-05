package com.opinion.app.test;

import com.opinion.app.*;
import com.opinion.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataStoreFileTest {

    @TempDir static Path tmp;

    @BeforeAll
    static void useTemp() {
        System.setProperty("opinion.dataDir", tmp.resolve("data").toString());
    }

    @AfterAll
    static void clear() {
        System.clearProperty("opinion.dataDir");
    }

    @Test
    void user_add_and_find_survive_restart() {
        DataStore ds1 = DataStoreFile.getInstance();
        var u = new Reviewer("a@b.com", "Alice",
                LocalDate.of(2022,1,1), "Home electronics specialist");
        ds1.addUser(u);

        // “restart”: new reference (reads from file each call)
        DataStore ds2 = DataStoreFile.getInstance();
        assertTrue(ds2.findUserByEmail("A@B.COM").isPresent());
    }

    @Test
    void user_update_persists() {
        DataStore ds = DataStoreFile.getInstance();
        var u = new Reviewer("x@y.com", "X",
                LocalDate.of(2021,5,5), "Kitchen tools specialist");
        ds.addUser(u);

        var u2 = new Reviewer("x@y.com", "Xavier",
                LocalDate.of(2021,5,5), "Kitchen tools specialist");
        ds.updateUser(u2);

        var found = ds.findUserByEmail("x@y.com").orElseThrow();
        assertEquals("Xavier", found.getName());
    }

    @Test
    void add_review_and_search_substring() {
        DataStore ds = DataStoreFile.getInstance();

        var author = new Reviewer("r@r.com", "R",
                LocalDate.of(2020,1,1), "Toys specialist");
        ds.addUser(author);

        // If your UserReview is a class you defined, adapt constructor calls below:
        var review1 = new UserReview(author, new Review("Toy Car", Rating.THREE, "fast and shiny"));
        var review2 = new UserReview(author, new Review("Doll",    Rating.ONE,   "cheap fabric"));
        ds.addUserReview(review1);
        ds.addUserReview(review2);

        List<UserReview> hits = ds.searchUserReviews("shiNy"); // case-insensitive
        assertEquals(1, hits.size());
        assertEquals("Toy Car", hits.get(0).getReview().name());
    }

    @Test
    void approvals_persist() {
        DataStore ds = DataStoreFile.getInstance();

        var author = new Reviewer("z@z.com", "Z",
                LocalDate.of(2021,2,2), "Outdoors specialist");
        ds.addUser(author);
        var ur = new UserReview(author, new Review("Tent", Rating.TWO, "ok for beginners"));
        ds.addUserReview(ur);

        var admin = new Administrator("admin@site.com", "Admin",
                LocalDate.of(2023,3,3));
        var appr = ds.addApproval(ur, admin, LocalDate.of(2024,1,1));
        assertNotNull(appr);
    }
}
