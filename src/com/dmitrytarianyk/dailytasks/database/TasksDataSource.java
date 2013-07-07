package com.dmitrytarianyk.dailytasks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dmitrytarianyk.dailytasks.objects.Event;
import com.dmitrytarianyk.dailytasks.objects.EventCollection;

public class TasksDataSource {

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public TasksDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public EventCollection getAllTasks() {
        EventCollection events = new EventCollection();

        Cursor cursor = database.query(TasksTable.TABLE_NAME, TasksTable.PROJECTION, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.addEvent(event);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();
        return events;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setTitle(cursor.getString(cursor.getColumnIndex(TasksTable.COLUMN_TITLE)));

        return event;
    }
}
