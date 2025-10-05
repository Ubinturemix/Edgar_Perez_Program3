/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
*/

package com.opinion.app;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/*
 Abstraction for storing and retrieving users, reviews, and approvals.

 Implementations may be in-memory or persistent. This interface also
 supports the observer pattern for reacting to add/update events.

 */
public interface DataStore {

    //Looks up a user by email, case-insensitive.
    //@param email email string to search for
    //@return optional containing the user if one exists, empty otherwise
    Optional<User> findUserByEmail(String email);

    /*
     Adds a new user to the store.

     @param user non-null user
     @return the added user
     @throws NullPointerException
     @throws IllegalArgumentException if a user with the same email already exists
     */
    User addUser(User user);




    /*
     Updates an existing user.

     @param user non-null user
     @return the updated user
     @throws NullPointerException
     @throws IllegalArgumentException if no user with the given email exists
     */
    User updateUser(User user);

    /*
     Adds a new user review. Each reviewer may only review a given product once.

     @param ur non-null user review
     @return the added review
     @throws NullPointerException
     @throws IllegalArgumentException if the reviewer has already reviewed the product
     */
     UserReview addUserReview(UserReview ur);





    /*
     Searches user reviews by substring in product name or comment (case-insensitive).

     @param substring substring to search for
     @return list of matching user reviews
     */
    List<UserReview> searchUserReviews(String substring);





    /*
     Creates and attaches an approval entry to a user review.

     @param ur    user review to approve
     @param admin approving administrator
     @param date  approval date
     @return created approval
     @throws NullPointerException
     @throws IllegalArgumentException if approval date is invalid
     */
    ReviewApproval addApproval(UserReview ur, Administrator admin, LocalDate date);



    /*
     Registers an observer that will be notified of all add/update events.

     @param observer observer to add
     @throws NullPointerException if observer is null
     */
    void addObserver(DataStoreObserver observer);
}
