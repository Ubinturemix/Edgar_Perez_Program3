/*
 Author:     Edgar Perez
 Assignment: Program 3 - Persistent Reviews
 Class:      CSC 2040
*/

package com.opinion.app;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-backed DataStore implementation.
 * - No in-memory master lists (reads/writes files every call).
 * - Observers are in-memory only (allowed).
 */
public final class DataStoreFile implements DataStore {

    private static final DataStoreFile INSTANCE = new DataStoreFile();
    public static DataStoreFile getInstance() { return INSTANCE; }
    private DataStoreFile() {}

    // ---- Files ----
    private Path usersFile()     { return StoragePaths.baseDir().resolve("users.dat"); }
    private Path reviewsFile()   { return StoragePaths.baseDir().resolve("userReviews.dat"); }
    private Path approvalsFile() { return StoragePaths.baseDir().resolve("approvals.dat"); }

    // ---- Observers (in-memory) ----
    private final List<DataStoreObserver> observers = new ArrayList<>();

    // ===================== Users =====================
    @Override
    public Optional<User> findUserByEmail(String email) {
        Objects.requireNonNull(email, "email");
        try {
            var users = IoHelper.<User>readAll(usersFile());
            return users.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read users", e);
        }
    }

    @Override
    public User addUser(User user) {
        Objects.requireNonNull(user, "user");
        try {
            var users = new ArrayList<>(IoHelper.<User>readAll(usersFile()));
            boolean exists = users.stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
            if (exists) throw new IllegalArgumentException("User already exists: " + user.getEmail());
            users.add(user);
            IoHelper.writeAll(usersFile(), users);
            notifyObserversAdd(user);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Unable to write users", e);
        }
    }

    @Override
    public User updateUser(User user) {
        Objects.requireNonNull(user, "user");
        try {
            var users = new ArrayList<>(IoHelper.<User>readAll(usersFile()));
            int idx = indexOfUser(users, user.getEmail());
            if (idx < 0) throw new IllegalArgumentException("No user with email: " + user.getEmail());
            users.set(idx, user);
            IoHelper.writeAll(usersFile(), users);
            notifyObserversUpdate(user);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Unable to write users", e);
        }
    }

    private static int indexOfUser(List<User> users, String email) {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getEmail().equalsIgnoreCase(email)) return i;
        return -1;
    }

    // ===================== UserReviews =====================
    @Override
    public UserReview addUserReview(UserReview ur) {
        Objects.requireNonNull(ur, "user review");
        try {
            var reviews = new ArrayList<>(IoHelper.<UserReview>readAll(reviewsFile()));

            // Enforce: each reviewer may review a product only once
            String revEmail = reviewerEmailOf(ur);
            String product  = productNameOf(ur);

            boolean duplicate = reviews.stream().anyMatch(existing ->
                    reviewerEmailOf(existing).equalsIgnoreCase(revEmail) &&
                            productNameOf(existing).equalsIgnoreCase(product));

            if (duplicate) {
                throw new IllegalArgumentException(
                        "Reviewer already reviewed: " + revEmail + " → " + product);
            }

            reviews.add(ur);
            IoHelper.writeAll(reviewsFile(), reviews);
            notifyObserversAdd(ur);
            return ur;
        } catch (IOException e) {
            throw new RuntimeException("Unable to write user reviews", e);
        }
    }

    @Override
    public List<UserReview> searchUserReviews(String substring) {
        Objects.requireNonNull(substring, "substring");
        String needle = substring.toLowerCase();
        try {
            return IoHelper.<UserReview>readAll(reviewsFile()).stream()
                    .filter(ur -> containsIgnoreCase(productNameOf(ur), needle)
                            || containsIgnoreCase(commentOf(ur), needle))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read user reviews", e);
        }
    }

    private static boolean containsIgnoreCase(String hay, String needleLower) {
        return hay != null && hay.toLowerCase().contains(needleLower);
    }

    // ===================== Approvals =====================
    @Override
    public ReviewApproval addApproval(UserReview ur, Administrator admin, LocalDate date) {
        Objects.requireNonNull(ur, "user review");
        Objects.requireNonNull(admin, "admin");
        Objects.requireNonNull(date, "date");

        if (!Validators.isValidDate(date)) {
            throw new IllegalArgumentException("Invalid approval date");
        }

        try {
            var approvals = new ArrayList<>(IoHelper.<ReviewApproval>readAll(approvalsFile()));
            ReviewApproval approval = new ReviewApproval(ur, admin, date); // adjust ctor if needed
            approvals.add(approval);
            IoHelper.writeAll(approvalsFile(), approvals);
            notifyObserversAdd(approval);
            return approval;
        } catch (IOException e) {
            throw new RuntimeException("Unable to write approvals", e);
        }
    }

    // ===================== Observers =====================
    @Override
    public void addObserver(DataStoreObserver observer) {
        Objects.requireNonNull(observer, "observer");
        synchronized (observers) { observers.add(observer); }
    }

    private void notifyObserversAdd(Object entity) {
        synchronized (observers) {
            for (var o : observers) { try { o.onAdd(entity); } catch (Throwable ignored) {} }
        }
    }

    private void notifyObserversUpdate(Object entity) {
        synchronized (observers) {
            for (var o : observers) { try { o.onUpdate(entity); } catch (Throwable ignored) {} }
        }
    }

    // ===================== Accessor adapters =====================
    // Adjust these if your method names differ.
    private static String reviewerEmailOf(UserReview ur) {
        return ur.getReviewer().getEmail();     // e.g., or ur.reviewer().getEmail()
    }
    private static String productNameOf(UserReview ur) {
        return ur.getReview().name();           // e.g., or ur.getProductName()
    }
    private static String commentOf(UserReview ur) {
        return ur.getReview().comment();        // e.g., or ur.getComment()
    }
}
