/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */


package com.opinion.app;

import java.time.LocalDate;


import java.io.Serializable;


/*
 A User who can author UserReviews.

 Reviewers are required to declare a specialization string that
 describes their domain expertise. The specialization must be
 between 10 and 300 characters long (inclusive).
 */
public class Reviewer extends User implements Serializable {

    private static final long serialVersionUID = 1L;


    private final String specialization;


    //@param email unique reviewer email
    //@param name reviewer name
    //@param registrationDate >= 2010-01-01
    //@param specialization 10..300 characters

    public Reviewer(final String email,
                    final String name,
                    final LocalDate registrationDate,
                    final String specialization) {
        super(email, name, registrationDate);
        if (!Validators.isValidSpecialization(specialization)) {
            throw new IllegalArgumentException("Invalid specialization length");
        }
        this.specialization = specialization;
    }


     //Returns this reviewer's specialization string.
     //@return specialization, never null

    public String getSpecialization() { return specialization; }
}