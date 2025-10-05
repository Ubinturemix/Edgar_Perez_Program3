/*
 Author: Edgar Perez
 Assignment: Program 2
 Class: CSC 2040
 */

package com.opinion.app;

import java.time.LocalDate;
import java.util.Objects;
import java.io.Serializable;

//Abstract base for authenticable users
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;


    protected final String email;
    protected final String name;
    protected final LocalDate registrationDate;

    //Validating constructor.

     //@param email unique email
     //@param name display name
     //@param registrationDate >= 2010-01-01
     //@throws IllegalArgumentException if validation fails
    protected User(final String email,
                   final String name,
                   final LocalDate registrationDate) {
        if (!Validators.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!Validators.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (!Validators.isValidDate(registrationDate)) {
            throw new IllegalArgumentException("Invalid registration date");
        }
        this.email = email;
        this.name = name;
        this.registrationDate = registrationDate;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    @Override public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return email.equalsIgnoreCase(((User) o).email);
    }
    @Override public final int hashCode() { return Objects.hash(email.toLowerCase()); }
}
