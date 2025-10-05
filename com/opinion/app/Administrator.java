/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */

package com.opinion.app;

import java.time.LocalDate;
import java.time.LocalDateTime;


//Administrator can create users and approve reviews
//Each administrator tracks the timestamp of their most recent successful login
public class Administrator extends User {

    //Most recent successful login timestamp, null until first login
    private LocalDateTime lastAccessDate;


    /*

     Constructs an Administrator.
     @param email   unique administrator email
     @param name  administrator display name
     @param registrationDate registration date
     @throws NullPointerException
     @throws IllegalArgumentException if validation rules are violated
     */
    public Administrator(final String email, final String name, final LocalDate registrationDate){

        super(email, name, registrationDate);

    }


    /*
     Returns the last access timestamp.
     @return timestamp of most recent login, or null if never logged in
     */
    public LocalDateTime getLastAccessDate() {

        return lastAccessDate;
    }


    /*
     Updates lastAccessDate to current time.
     Call this after a successful authentication.
     */
    public void touchAccessNow(){

        this.lastAccessDate = LocalDateTime.now();
    }






}
