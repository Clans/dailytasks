package com.dmitrytarianyk.dailytasks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dmitrytarianyk.dailytasks.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dailytasks.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatesTable.onCreate(db);
        TasksTable.onCreate(db);
        String query1 = "INSERT INTO " + DatesTable.TABLE_NAME + "(" + DatesTable.COLUMN_DATE
                + ") VALUES ('10.10.2013');";
        String query2 = "INSERT INTO " + TasksTable.TABLE_NAME + "(" + TasksTable.COLUMN_TITLE + ", "
                + TasksTable.COLUMN_DATE_TIME + ", " + TasksTable.COLUMN_DESCRIPTION + ", "
                + TasksTable.COLUMN_DATE_ID + ") VALUES ('Mr. Bojangles', '10.10.2013', 'Sample description', 1, 0);";
        Log.i(Constants.LOG_TAG, "DatabaseHelper.onCreate: " + query1);
        Log.i(Constants.LOG_TAG, "DatabaseHelper.onCreate: " + query2);
//        db.execSQL(query1);
//        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatesTable.onUpgrade(db, oldVersion, newVersion);
        TasksTable.onUpgrade(db, oldVersion, newVersion);
    }
}
