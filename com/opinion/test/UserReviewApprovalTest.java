/*
 * Author:     Edgar Perez
 * Assignment: Program 2
 * Class:      CSC 2040 Software Engineering
 */

package com.opinion.test;

import com.opinion.Review;
import com.opinion.app.Administrator;
import com.opinion.app.ReviewApproval;
import com.opinion.app.ReviewBridge;
import com.opinion.app.Reviewer;
import com.opinion.app.UserReview;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserReviewApprovalTest {

    @Test
    void userReviewRequiresReviewAndReviewer() {
        Reviewer r = new Reviewer("r@x.y", "Rita Doe",
                LocalDate.of(2020,1,2), "Apparel & Footwear");
        Review rev = ReviewBridge.buildReview("Widget", 1, "ok");

        assertThrows(IllegalArgumentException.class, () -> new UserReview(null, r));
        assertThrows(IllegalArgumentException.class, () -> new UserReview(rev, null));
        assertDoesNotThrow(() -> new UserReview(rev, r));
    }

    @Test
    void approvalRequiresAdminAndValidDate() {
        Reviewer r = new Reviewer("r2@x.y", "Rita Two",
                LocalDate.of(2020,1,2), "Apparel & Footwear");
        Review rev = ReviewBridge.buildReview("Widget2", 1, "ok");
        UserReview ur = new UserReview(rev, r);
        Administrator a = new Administrator("admin2@x.y", "Alice Admin", LocalDate.of(2020,2,2));


        //invalid date
        assertThrows(IllegalArgumentException.class,
                () -> new ReviewApproval(ur, a, LocalDate.of(2009,12,31)));

        //null args
        assertThrows(IllegalArgumentException.class,
                () -> new ReviewApproval(null, a, LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
                () -> new ReviewApproval(ur, null, LocalDate.now()));

        //valid
        assertDoesNotThrow(() -> new ReviewApproval(ur, a, LocalDate.now()));
    }

    @Test
    void approvalsAccumulateAndAreUnmodifiable() {
        Reviewer r = new Reviewer("r3@x.y", "Rita Three",
                LocalDate.of(2020,1,2), "Apparel & Footwear");
        Review rev = ReviewBridge.buildReview("Widget3", 1, "ok");
        UserReview ur = new UserReview(rev, r);
        Administrator a = new Administrator("admin3@x.y", "Alice Admin", LocalDate.of(2020,2,2));

        //use test hook to add approvals (no datastore yet)
        //TestHooks.addApproval(ur, new ReviewApproval(ur, a, LocalDate.now()));
        //TestHooks.addApproval(ur, new ReviewApproval(ur, a, LocalDate.now()));

        //assertEquals(2, ur.getApprovals().size());

        // list is unmodifiable to callers
        assertThrows(UnsupportedOperationException.class, () ->
                ur.getApprovals().add(new ReviewApproval(ur, a, LocalDate.now())));
    }
}
