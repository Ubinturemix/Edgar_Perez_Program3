/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */

package com.opinion.app;

/*Observer of Events emitted by a DataStore.
 Implementations of this interface define behavior that
 should occur whenever a datastore event is published.
 */
 public interface DataStoreObserver {


    //Called when an event occurs in the datastore
     //@param e the event, must not be null
    void onEvent(Event e);
}
