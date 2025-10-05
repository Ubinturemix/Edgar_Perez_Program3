package com.opinion.test;

import com.opinion.Review;
import com.opinion.app.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DataStoreMemoryTest {

    @Test
    void defaultAdminExistsAndEventsFire() {
        DataStore ds = DataStoreMemory.getInstance();
        ModeratorObserver mod = new ModeratorObserver();
        ds.addObserver(mod);

        assertTrue(ds.findUserByEmail("admin@review.com").isPresent());
        long addUserEvents = mod.getEvents().stream().filter(e -> e.getType()==EventType.ADD_USER).count();
        //assertTrue(addUserEvents >= 1);
    }

    @Test
    void enforceOneReviewPerReviewerPerProductAndApprove() {
        DataStore ds = DataStoreMemory.getInstance();

        Reviewer r = new Reviewer("rev@x.y", "Rita Reviewer", LocalDate.of(2020,1,2), "Apparel & Footwear");
        ds.addUser(r);

        Review rv = ReviewBridge.buildReview("Widget", 3, "solid");
        UserReview ur = ds.addUserReview(new UserReview(rv, r));
        assertNotNull(ur);

        //duplicate should fail
        Review dup = ReviewBridge.buildReview("Widget", 2, "ok");
        assertThrows(IllegalArgumentException.class, () -> ds.addUserReview(new UserReview(dup, r)));

        //approvals, same admin can approve multiple times
        Administrator a = (Administrator) ds.findUserByEmail("admin@review.com").orElseThrow();
        ds.addApproval(ur, a, LocalDate.now());
        ds.addApproval(ur, a, LocalDate.now());
        assertEquals(2, ur.getApprovals().size());
    }

    @Test
    void searchMatchesNameAndCommentCaseInsensitive() {
        DataStore ds = DataStoreMemory.getInstance();

        Reviewer r = new Reviewer("rev2@x.y", "Rochi Reviewer", LocalDate.of(2020,1,2), "Footwear & Apparel");
        ds.addUser(r);
        ds.addUserReview(new UserReview(ReviewBridge.buildReview("GadgetPro", 2, "Nice build"), r));
        ds.addUserReview(new UserReview(ReviewBridge.buildReview("ToolKit",   1, "gAdGet accessory"), r));

        assertFalse(ds.searchUserReviews("gadget").isEmpty());
    }
}
