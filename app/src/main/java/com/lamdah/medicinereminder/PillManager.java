package com.lamdah.medicinereminder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.List;

import com.lamdah.medicinereminder.models.Pill;

public class PillManager {
    private SQLiteDatabase db;

    public PillManager(Context context) {
        PillDbHelper dbHelper = new PillDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void addPill(String name, long datetime) {
        ContentValues values = new ContentValues();
        values.put(PillEntry.COLUMN_NAME_NAME, name);
        values.put(PillEntry.COLUMN_NAME_DATETIME, datetime);
        db.insert(PillEntry.TABLE_NAME, null, values);
    }

    public List<Pill> getPills() {
        String[] projection = {
                PillEntry._ID,
                PillEntry.COLUMN_NAME_NAME,
                PillEntry.COLUMN_NAME_DATETIME
        };

        Cursor cursor = db.query(
                PillEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Pill> pills = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(PillEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PillEntry.COLUMN_NAME_NAME));
            long datetime = cursor.getLong(cursor.getColumnIndexOrThrow(PillEntry.COLUMN_NAME_DATETIME));
            pills.add(new Pill(id, name, datetime));
        }
        cursor.close();

        return pills;
    }

    private static class PillDbHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Pill.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PillEntry.TABLE_NAME + " (" +
                        PillEntry._ID + " INTEGER PRIMARY KEY," +
                        PillEntry.COLUMN_NAME_NAME + " TEXT," +
                        PillEntry.COLUMN_NAME_DATETIME + " INTEGER)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PillEntry.TABLE_NAME;

        public PillDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    private static class PillEntry implements BaseColumns {
        public static final String TABLE_NAME = "pill";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DATETIME = "datetime";
    }
}