package com.dmitrytarianyk.dailytasks.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.dmitrytarianyk.dailytasks.Constants;

public class DatesTable {

    public static final String TABLE_NAME = "dates";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";

    public static final String[] PROJECTION = {
            COLUMN_ID,
            COLUMN_DATE
    };

    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL" + ")";

    public static void onCreate(SQLiteDatabase db) {
        Log.i(Constants.LOG_TAG, "DatesTable.onCreate: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
