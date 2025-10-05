/*
Author: Edgar Perez
Assignment: Program 2
Class: CSC 2040
 */

package com.opinion.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
 Observer that collects all Events into an in-memory list.
 This simulates a moderator tracking changes. Events are accumulated
 as they arrive and can be retrieved as an immutable snapshot.
 */
public class ModeratorObserver implements DataStoreObserver{

    //Internal list of all events observed so far
    private final List<Event> events = new ArrayList<>();

     //Called by the {@link DataStore} when an event occurs
     //@param e event to record, must not be null
    @Override
    public void onEvent(Event e) {
        events.add(e);
    }


     //Returns an immutable snapshot of all events observed so far.
     //@return unmodifiable list of events, never null
     public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

}
