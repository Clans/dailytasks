package com.dmitrytarianyk.dailytasks.objects;

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
}
