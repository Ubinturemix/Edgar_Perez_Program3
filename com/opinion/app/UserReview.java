/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */

package com.opinion.app;

import com.opinion.Review;

import java.util.*;

public class UserReview {

    //Program 1 review, product, rating, comment
    private final Review review;
    //review author
    private final Reviewer reviewer;
    private final List<ReviewApproval> approvals = new ArrayList<>();


    /*
     Constructs a {@code UserReview}.
     @param review   the base review; must not be null
     @param reviewer the review's author, must not be null
     @throws IllegalArgumentException if review or reviewer is null
     */
    public UserReview(final Review review, final Reviewer reviewer) {
        if (review == null || reviewer == null)
            throw new IllegalArgumentException("Review and Reviewer can't be null");
        this.review = review;
        this.reviewer = reviewer;
    }


    //Returns underlying review
     //@return non-null review
    public Review getReview() {
        return review;
    }


    //Returns the Reviewer
     //@return non-null reviewer
    public Reviewer getReviewer() {
        return reviewer;
    }

    //Returns immutable view of approvals
    public List<ReviewApproval> getApprovals() {
        return Collections.unmodifiableList(approvals);
    }

    //Add an approval to the user review
    void addApproval(final ReviewApproval a) {
        approvals.add(Objects.requireNonNull(a, "approval"));

    }
}