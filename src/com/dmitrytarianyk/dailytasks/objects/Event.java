package com.dmitrytarianyk.dailytasks.objects;

import android.database.Cursor;

public class Event {

    private String title;
    private long startMillis;
    private long endMillis;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(long endMillis) {
        this.endMillis = endMillis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Event fromCursor(Cursor cursor) {
        Event event = new Event();
        event.setTitle(cursor.getString(1));
        event.setDescription(cursor.getString(2));
        event.setStartMillis(cursor.getLong(3));
        event.setEndMillis(cursor.getLong(4));

        return event;
    }
}
