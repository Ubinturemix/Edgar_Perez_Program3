/*
 Author: Edgar Perez
 Assignment: Program 2
 Class: CSC 2040
 */

package com.opinion.app;

import java.time.LocalDate;
import java.util.regex.Pattern;

//Validation utilities for email, name, specialization length, and date, >= 2010-01-01
public final class Validators {

    private Validators() { }

    //Accepts alnum and dot on both sides of @
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9.]+@[A-Za-z0-9.]+$");

    //Letters and spaces only
    private static final Pattern NAME  = Pattern.compile("^[A-Za-z ]+$");

    private static final LocalDate COMPANY_START = LocalDate.of(2010, 1, 1);

    //@return true if the string matches the course email regex
    public static boolean isValidEmail(final String s) {
        return s != null && EMAIL.matcher(s).matches();
    }

    //@return true if the string contains only letters and spaces
    public static boolean isValidName(String s) {
        if (s == null) return false;
        if (s.length() < 1 || s.length() > 50) return false;
        return s.chars().allMatch(ch -> ch == ' ' || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'));
    }

    //@return true for 10..300 chars
    public static boolean isValidSpecialization(final String s) {
        if (s == null) return false;
        final int len = s.length();
        return len >= 10 && len <= 300;
    }

    //@return true if date is not null and not before company start
    public static boolean isValidDate(final LocalDate d) {
        return d != null && !d.isBefore(COMPANY_START);
    }
}
