/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */


package com.opinion.app;

 //Enumeration of the types of Events that observers may receive
 //from DataStore.
public enum EventType {

     //A new user was added
     ADD_USER,

     // An existing user was update
     UPDATE_USER,

     //A new user review was added
     ADD_USER_REVIEW,

     //A new review approval was added
     ADD_REVIEW_APPROVAL

}
