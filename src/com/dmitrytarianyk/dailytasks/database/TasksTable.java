package com.dmitrytarianyk.dailytasks.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.dmitrytarianyk.dailytasks.Constants;

public class TasksTable {

    public static final String TABLE_NAME = "tasks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE_ID = "dateid";
    public static final String COLUMN_DONE = "done";

    public static final String[] PROJECTION = {
            COLUMN_ID,
            COLUMN_TITLE,
            COLUMN_DATE_TIME,
            COLUMN_DESCRIPTION
    };

    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_DATE_TIME + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_DATE_ID + " INTEGER NOT NULL, "
            + COLUMN_DONE + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_DATE_ID + ") REFERENCES "
            + DatesTable.TABLE_NAME + "(" + DatesTable.COLUMN_ID + "))";

    public static void onCreate(SQLiteDatabase db) {
        Log.i(Constants.LOG_TAG, "TasksTable.onCreate: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
