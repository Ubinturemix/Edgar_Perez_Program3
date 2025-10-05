/*
Author: Edgar Perez
Assignemnt: Program 2
Class: CSC 2040
 */

package com.opinion.app;

import java.time.Instant;
import java.util.Objects;


/*
 Immutable event object passed to DataStoreObservers.
 Each event has a type, a descriptive message, a timestamp set at
 construction, and an optional payload object for additional context.
 */
 public final class Event {

    //Type of event, never null
    private final EventType type;

    //Human readable message describing event, never null
    private final String message;

    //Timestamp when the event was created, never null
    private final Instant timestamp;

    //Optional payload object providing additional context
    private final Object payload;



    /*
     Constructs an Event with current timestamp.

     @param type    event type (must not be, null})
     @param message descriptive message, must not be null
     @param payload optional context payload, may be null
     @throws NullPointerException if type or message is null}
     */
    public Event(EventType type, String message, Object payload) {

        this.type = Objects.requireNonNull(type, "type must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.timestamp = Instant.now();
        this.payload = payload;


    }


    //Returns event type
     //@return non-null type
    public EventType getType() {
        return type;
    }

    //Returns the descriptive message.
     //@return non-null message
    public String getMessage() {
        return message;
    }

    //Returns the event timestamp, creation time
     //@return non-null timestamp
    public Instant getTimestamp() {
        return timestamp;
    }

    //Returns optional payload object.
     //@return payload
    public Object getPayload() {
        return payload;
    }

    //Returns a string summary of the event including timestamp, type, and message.
    //@return formatted event summary
    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + " " + message;
    }






}
