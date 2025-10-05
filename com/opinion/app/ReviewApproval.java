/*
Author: Edgar Perez
Assignment: Program 2
Class : CSC 2040
 */

package com.opinion.app;

import java.time.LocalDate;


/*
 Represents an approval of a {@link UserReview} by an {@link Administrator}
 on a specific date.
 All arguments must be non-null, and the approval date must be
 on or after 2010-01-01
 */
public class ReviewApproval {

    //The user review being approved
    private final UserReview userReview;

    //The administrator who granted approval
    private final Administrator administrator;

    //the date of approval, must be ≥ 2010-01-01
    private final LocalDate approvalDate;


    /*
     Construct a ReviewApproval
     @param userReview   the review being approved, must not be null
     @param administrator the approving administrator, must not be null
     @param approvalDate date of approval, must not be null and must be valid
     @throws IllegalArgumentException if any argument is null
     or if {@code approvalDate} is before 2010-01-01
     */
    public ReviewApproval(final UserReview userReview, final Administrator administrator, final LocalDate approvalDate) {

        if (userReview == null || administrator == null)
            throw new IllegalArgumentException("UserReview and Administrator are required");
        if (!Validators.isValidDate(approvalDate))
            throw new IllegalArgumentException("Invalid approval date");
        this.userReview = userReview;
        this.administrator = administrator;
        this.approvalDate = approvalDate;

    }

    //Returns the approved UserReview
    //@return non-null user review
    public UserReview getUserReview() {
        return userReview;
    }

    //Returns the approving Administrator
    //@return non-null administrator
    public Administrator getAdministrator() {
        return administrator;
    }

    //Returns the date of approval
    //@return non-null approval date
    public LocalDate getApprovalDate() {
        return approvalDate;
    }

}