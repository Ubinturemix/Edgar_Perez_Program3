package com.opinion.test;

import com.opinion.app.Event;
import com.opinion.app.EventLoggerObserver;
import com.opinion.app.EventType;
import com.opinion.app.ModeratorObserver;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    @Test
    void moderatorCollectsEvents() {
        ModeratorObserver mod = new ModeratorObserver();
        mod.onEvent(new Event(EventType.ADD_USER, "User created", "u1"));
        mod.onEvent(new Event(EventType.ADD_USER_REVIEW, "Review added", "r1"));

        List<Event> snapshot = mod.getEvents();
        assertEquals(2, snapshot.size());
        assertEquals(EventType.ADD_USER, snapshot.get(0).getType());
        assertEquals("User created", snapshot.get(0).getMessage());
        assertEquals(EventType.ADD_USER_REVIEW, snapshot.get(1).getType());
    }

    @Test
    void loggerWritesToEventsDotLog() throws Exception {
        String uniqueMsg = "Logger smoke " + System.nanoTime();
        EventLoggerObserver logger = new EventLoggerObserver();
        logger.onEvent(new Event(EventType.UPDATE_USER, uniqueMsg, "payload"));

        Path log = Path.of("events.log");
        assertTrue(Files.exists(log), "events.log should exist");
        String all = Files.readString(log, StandardCharsets.UTF_8);
        assertTrue(all.contains(uniqueMsg), "events.log should contain the unique message");
    }
}
