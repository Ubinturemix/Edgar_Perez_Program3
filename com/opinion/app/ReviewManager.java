/*
Edgar Perez
Program 2
CSC 2040
 */
package com.opinion.app;

import com.opinion.Review;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 Console UI for the Review Manager application.

 Responsibilities:

 Render role-aware menus.
 Route user choices to actions (search, login, create users, add/approve reviews).
 Seed demo data when backing store is empty.

 This class is the UI entry point and is the only place that prints to the console.
 */
public final class ReviewManager {

    private final DataStore ds = DataStoreMemory.getInstance();
    private final Scanner in = new Scanner(System.in);
    private User currentUser = null;

    //A record to link menu text with corresponding action
    private record MenuItem(String text, Runnable action) {}

    public static void main(String[] args) {
        ReviewManager app = new ReviewManager();
        app.ensureSeedData();
        app.wireObservers();
        app.loop();
    }

    private void wireObservers() {
        ds.addObserver(new ModeratorObserver());
        ds.addObserver(new EventLoggerObserver());
    }

    private void loop() {
        while (true) {
            List<Runnable> actions = renderMenuAndGetActions();
            int choice = promptInt("Choice> ", 0, actions.size() - 1);
            if (choice == -1) { // Invalid input
                System.out.println("Invalid selection");
                continue;
            }
            actions.get(choice).run();
        }
    }

    //Renders a role-appropriate menu and returns the list of actions
    private List<Runnable> renderMenuAndGetActions() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Search Reviews", this::actionSearch));

        if (currentUser instanceof Administrator) {
            menuItems.add(new MenuItem("Login", this::actionAuthenticate)); // was Authenticate
            menuItems.add(new MenuItem("New User", this::actionNewUser));
            menuItems.add(new MenuItem("Approve Review", this::actionApprove));
        } else if (currentUser instanceof Reviewer) {
            menuItems.add(new MenuItem("Login", this::actionAuthenticate));       // add this
            menuItems.add(new MenuItem("Add User Review", this::actionAddUserReview));
        } else { // Viewer
            menuItems.add(new MenuItem("Login", this::actionAuthenticate));       // was Authenticate
        }

        menuItems.add(new MenuItem("Exit", () -> System.exit(0)));


        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println(i + ") " + menuItems.get(i).text());
        }

        return menuItems.stream().map(MenuItem::action).toList();
    }

    //Actions

    private void actionSearch() {
        String needle = promptLine("Search> ");
        var matches = ds.searchUserReviews(needle);
        if (matches.isEmpty()) {
            System.out.println("No matches found");
        } else {
            matches.forEach(ur -> System.out.println(formatReviewLine(ur)));
        }
    }


    private void actionAuthenticate() {
        String email = promptLine("Username> ");
        ds.findUserByEmail(email).ifPresentOrElse(user -> {
            currentUser = user;
            if (user instanceof Administrator admin) {
                admin.touchAccessNow();
                ds.updateUser(admin);
            }
        }, () -> System.out.println("Invalid login"));
    }

    //Information for testing to ensure functionality
    private void ensureSeedData() {

        //Admin account for login demonstration
        if (ds.findUserByEmail("admin@review.com").isEmpty()) {
            ds.addUser(new Administrator("admin@review.com", "Admin", java.time.LocalDate.now()));
        }

        //Seed Bob + 3 reviews if none exist
        //IMPORTANT: "" should return all
        if (ds.searchUserReviews("").isEmpty()) {
            Reviewer bob = new Reviewer("bob@example.com", "Bob", LocalDate.now(), "Clothing and stuff");

            ds.addUser(bob);

            ds.addUserReview(new UserReview(ReviewBridge.buildReview("Toy", 2, "Lumpy"), bob));
            ds.addUserReview(new UserReview(ReviewBridge.buildReview("Bike", 3, "Smooth ride"), bob));
            ds.addUserReview(new UserReview(ReviewBridge.buildReview("Broken Glass", 1, "Sharp edges"), bob));
        }
    }



    private void actionAddUserReview() {
        if (!(currentUser instanceof Reviewer reviewer)) return;

        String product = promptLine("Name> ");             // was Product name>
        int stars = promptInt("Rating> ", 1, 3);           // was Rating (1-3)>
        if (stars == -1) return;
        String comment = promptLine("Comment> ");

        try {
            Review rv = ReviewBridge.buildReview(product, stars, comment);
            ds.addUserReview(new UserReview(rv, reviewer));
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid review: " + ex.getMessage());
        }
    }


    private void actionNewUser() {
        if (!(currentUser instanceof Administrator)) return;

        String email = promptLine("Email> ");
        if (!Validators.isValidEmail(email) || ds.findUserByEmail(email).isPresent()) {
            System.out.println("Invalid user: invalid or existing email");
            return;
        }
        String name = promptLine("Name> ");
        if (!Validators.isValidName(name)) {
            System.out.println("Invalid user: invalid name");
            return;
        }

        String type = promptLine("R (Reviewer) or A (Administrator)> ").trim();
        if (type.equalsIgnoreCase("R")) {
            String spec = promptLine("Specialization> ");
            if (!Validators.isValidSpecialization(spec)) {
                System.out.println("Invalid user: Invalid specialization");
                return;
            }
            ds.addUser(new Reviewer(email, name, LocalDate.now(), spec));
        } else if (type.equalsIgnoreCase("A")) {
            ds.addUser(new Administrator(email, name, LocalDate.now()));
        } else {
            System.out.println("Invalid type: " + type);
            //return;
        }
    }


    private void actionApprove() {
        if (!(currentUser instanceof Administrator admin)) return;

        String needle = promptLine("Search> ");
        var matches = ds.searchUserReviews(needle);
        if (matches.isEmpty()) {
            System.out.println("No matches found");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(i + ") " + formatReviewLine(matches.get(i)));
        }
        int idx = promptInt("Choice> ", 0, matches.size() - 1);   // was Select index>
        if (idx == -1) return;

        try {
            ds.addApproval(matches.get(idx), admin, LocalDate.now());
        } catch (IllegalArgumentException | NullPointerException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


    //Helpers

    private static String formatReviewLine(UserReview ur) {
        Review rv = ur.getReview();
        String approvers = ur.getApprovals().stream()
                .map(approval -> approval.getAdministrator().getEmail())
                .collect(Collectors.joining(","));

        return String.format("%s, %d star(s), %s by %s: Approver(s) %s",
                ReviewBridge.getName(rv), ReviewBridge.getStars(rv),
                ReviewBridge.getComment(rv), ur.getReviewer().getName(), approvers);
    }

    private String promptLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    //Returns a valid int in the range [min, max], or -1 on failure.
    private int promptInt(String prompt, int min, int max) {
        System.out.print(prompt);
        try {
            int val = Integer.parseInt(in.nextLine().trim());
            if (val >= min && val <= max) {
                return val;
            }
        } catch (NumberFormatException ignored) {
            //Fall through to error message
        }
        System.out.println("Error: please enter a number between " + min + " and " + max);
        return -1;
    }
}