/*
Edgar Perez
Program 2
CSC 2040
 */

package com.opinion.app;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 Observer that logs {@link Event}s to {@code events.log} using
 @link java.util.logging.
 Ensures that only one ileHandler is attached to logger,
 even if multiple EventLoggerObserver instances are created.
 Logging failures never crash the application or tests.
 */
 public class EventLoggerObserver implements DataStoreObserver {

    //Logger instance for review events.
    private final Logger log;


    /*
     Constructs an EventLoggerObserver and attaches a file handler
     to events.log if one does not already exist.
     The logger is configured to suppress parent handlers so that output
     goes only to the file.
     */
    public EventLoggerObserver() {
        this.log = Logger.getLogger("ReviewEvents");
        try {
            //Avoid duplicate FileHandlers if constructed multiple times
            boolean hasFileHandler = false;
            for (Handler h : log.getHandlers()) {
                if (h instanceof FileHandler) {
                    hasFileHandler = true;
                    break;
                }
            }
            if (!hasFileHandler) {
                FileHandler fh = new FileHandler("events.log", true);
                fh.setFormatter(new SimpleFormatter());
                log.addHandler(fh);
                log.setUseParentHandlers(false);
            }
        } catch (IOException ignored) {
            //Logging must not crash app or tests.
        }
    }


    /*
     Called when a datastore event occurs.
     Logs the event’s Object#toString() to events.log.
     @param e event to log, must not be null
     */
    @Override
    public void onEvent(final Event e) {
        log.info(e.toString());
    }
}

