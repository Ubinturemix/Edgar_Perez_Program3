/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */

package com.opinion.app;

import java.time.LocalDate;
import java.util.*;

import static java.util.Locale.ROOT;

/*
 In-memory, non-persistent DataStore implementation.

 Responsibilities:

 Holds users and user reviews in memory
 Creates a default administrator on first construction.
 Enforces uniqueness constraints
 Emits Events to registered

 This store performs no console I/O, all feedback is via return values and exceptions.
 */
public class DataStoreMemory implements DataStore {

    private static DataStoreMemory instance;

    /*
     Returns singleton instance of this datastore.
     @return non-null instance
     */
    public static synchronized DataStoreMemory getInstance() {
        if (instance == null) instance = new DataStoreMemory();
        return instance;
    }

    //Users indexed by lower-cased email
    private final Map<String, User> usersByEmail = new HashMap<>();

    //All user reviews
    private final List<UserReview> userReviews = new ArrayList<>();

    //Locale used for case-insensitive keys and searches.
    private final List<DataStoreObserver> observers = new ArrayList<>();


    //Locale used for case-insensitive keys and searches
    private static final Locale KEY_LOCALE = Locale.ROOT;


    //Private constructor initializes default admin and emits an event.
    private DataStoreMemory() {

        //default admin: email admin@review.com, regDate = today, no last access
        Administrator admin = new Administrator("admin@review.com", "Admin", LocalDate.now());
        usersByEmail.put(key(admin.getEmail()), admin);
        emit(EventType.ADD_USER, "Default admin created", admin);
    }



    /*
     Finds a user by email
     @param email email to look up
     @return Optional containing the user if found, otherwise empty
     */
    @Override
    public Optional<User> findUserByEmail(final String email) {
        return email == null ? Optional.empty() : Optional.ofNullable(usersByEmail.get(key(email)));
    }




    /*
     Adds a new user. The email must be unique.
     @param user non-null user
     @return the same user instance
     @throws NullPointerException
     @throws IllegalArgumentException if another user with the same email already exists
     */
    @Override
    public User addUser(final User user) {
        Objects.requireNonNull(user, "user");
        String k = key(user.getEmail());
        if (usersByEmail.containsKey(k)) throw new IllegalArgumentException("User already exists: " + user.getEmail());
        usersByEmail.put(k, user);
        emit(EventType.ADD_USER, "User added: " + user.getEmail(), user);
        return user;
    }




    /*
     Updates an existing user. A user with the same email must already exist.
     @param user non-null user
     @return the same user instance
     @throws NullPointerException
     @throws IllegalArgumentException if no user with this email exists
     */
    @Override
    public User updateUser(final User user) {
        Objects.requireNonNull(user, "user");
        String k = key(user.getEmail());
        if (!usersByEmail.containsKey(k)) throw new IllegalArgumentException("No such user: " + user.getEmail());
        usersByEmail.put(k, user);
        emit(EventType.UPDATE_USER, "User updated: " + user.getEmail(), user);
        return user;
    }


    //Adds a new UserReview. A reviewer may review a given product only once.
    //@param ur non-null user review
    //@return the same review instance
    //@throws NullPointerException
    //@throws IllegalArgumentException if the reviewer has already reviewed the same product

    @Override
    public UserReview addUserReview(final UserReview ur) {
        Objects.requireNonNull(ur, "userReview");
        String reviewerK = key(ur.getReviewer().getEmail());
        String product = ReviewBridge.getName(ur.getReview());

        boolean exists = userReviews.stream().anyMatch(x ->
                key(x.getReviewer().getEmail()).equals(reviewerK) &&
                        ReviewBridge.getName(x.getReview()).equalsIgnoreCase(product)
        );
        if (exists) throw new IllegalArgumentException("Reviewer already reviewed this product");

        userReviews.add(ur);
        emit(EventType.ADD_USER_REVIEW,
                "UserReview added for product: " + product + " by " + ur.getReviewer().getEmail(), ur);
        return ur;
    }


     //Searches user reviews by substring in product name or comment.
     //@param substring substring to search for
     //@return list of matching UserReviews

    @Override
    public List<UserReview> searchUserReviews(final String substring) {
        String needle = (substring == null ? "" : substring).toLowerCase(ROOT);
        List<UserReview> out = new ArrayList<>();
        for (UserReview ur : userReviews) {
            String name = ReviewBridge.getName(ur.getReview());
            String comment = ReviewBridge.getComment(ur.getReview());
            if ((name != null && name.toLowerCase(ROOT).contains(needle)) ||
                    (comment != null && comment.toLowerCase(ROOT).contains(needle))) {
                out.add(ur);
            }
        }
        return out;
    }



    //Adds an approval to a user review.
    @Override
    public ReviewApproval addApproval(final UserReview ur, final Administrator admin, final LocalDate date) {
        Objects.requireNonNull(ur, "userReview");
        Objects.requireNonNull(admin, "admin");
        ReviewApproval ra = new ReviewApproval(ur, admin, date);
        ur.addApproval(ra); //package-private; same package
        emit(EventType.ADD_REVIEW_APPROVAL,
                "Approval by " + admin.getEmail() + " for " + ReviewBridge.getName(ur.getReview()), ra);
        return ra;
    }

    //Registers an observer to receive datastore events.
    @Override
    public void addObserver(final DataStoreObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    //---- helpers ----
    private static String key(final String email) { return email.toLowerCase(ROOT); }

    private void emit(final EventType type, final String msg, final Object payload) {
        Event e = new Event(type, msg, payload);
        for (DataStoreObserver o : observers) o.onEvent(e);
    }
}
