package com.dmitrytarianyk.dailytasks.objects;

import android.database.Cursor;

import java.util.Vector;

public class EventCollection {

    Vector<Event> events = new Vector<Event>();

    public void addEvent(Event event) {
        events.addElement(event);
    }

    public void addAll(EventCollection eventCollection) {
        this.events.addAll(eventCollection.asVector());
    }

    public Event getEvent(int location) {
        return events.get(location);
    }

    public Vector<Event> asVector() {
        return events;
    }

    public int size() {
        return events.size();
    }

    public static EventCollection fromCursor(Cursor cursor) {
        EventCollection collection = new EventCollection();

        while (cursor != null && cursor.moveToNext()) {
            Event event = Event.fromCursor(cursor);
            collection.addEvent(event);
        }

        return collection;
    }
}
