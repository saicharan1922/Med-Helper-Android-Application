package com.lamdah.medicinereminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.lamdah.medicinereminder.models.Pill;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medicine_reminder.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PILLS = "pills";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_MONDAY = "monday";
    public static final String COLUMN_TUESDAY = "tuesday";
    public static final String COLUMN_WEDNESDAY = "wednesday";
    public static final String COLUMN_THURSDAY = "thursday";
    public static final String COLUMN_FRIDAY = "friday";
    public static final String COLUMN_SATURDAY = "saturday";
    public static final String COLUMN_SUNDAY = "sunday";
    private Context cxt;
    private String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cxt = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_PILLS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_DATETIME + " INTEGER NOT NULL, " +
                COLUMN_MONDAY + " INTEGER, " +
                COLUMN_TUESDAY + " INTEGER, " +
                COLUMN_WEDNESDAY + " INTEGER, " +
                COLUMN_THURSDAY + " INTEGER, " +
                COLUMN_FRIDAY + " INTEGER, " +
                COLUMN_SATURDAY + " INTEGER, " +
                COLUMN_SUNDAY + " INTEGER);";
        db.execSQL(createTableSql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILLS);
        onCreate(db);
    }

    public boolean addPill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, pill.getName());
        contentValues.put(COLUMN_DATETIME, pill.getDatetime());
        boolean[] days = pill.getDays();
        contentValues.put(COLUMN_MONDAY, days[0] ? 1 : 0);
        contentValues.put(COLUMN_TUESDAY, days[1] ? 1 : 0);
        contentValues.put(COLUMN_WEDNESDAY, days[2] ? 1 : 0);
        contentValues.put(COLUMN_THURSDAY, days[3] ? 1 : 0);
        contentValues.put(COLUMN_FRIDAY, days[4] ? 1 : 0);
        contentValues.put(COLUMN_SATURDAY, days[5] ? 1 : 0);
        contentValues.put(COLUMN_SUNDAY, days[6] ? 1 : 0);
        long result = db.insert(TABLE_PILLS, null, contentValues);
        db.close();
        return result != -1;
    }


    public List<Pill> getPillsForTab(int tabPosition) {
        List<Pill> pills = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PILLS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Pill pill = new Pill();
                int index;

                index = cursor.getColumnIndex(COLUMN_ID);
                if (index != -1) {
                    pill.setId(cursor.getInt(index));
                }

                index = cursor.getColumnIndex(COLUMN_NAME);
                if (index != -1) {
                    pill.setName(cursor.getString(index));
                }

                index = cursor.getColumnIndex(COLUMN_DATETIME);
                if (index != -1) {
                    pill.setDatetime(cursor.getLong(index));
                }

                boolean[] days = new boolean[7];
                index = cursor.getColumnIndex(COLUMN_MONDAY);
                if (index != -1) {
                    days[0] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_TUESDAY);
                if (index != -1) {
                    days[1] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_WEDNESDAY);
                if (index != -1) {
                    days[2] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_THURSDAY);
                if (index != -1) {
                    days[3] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_FRIDAY);
                if (index != -1) {
                    days[4] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_SATURDAY);
                if (index != -1) {
                    days[5] = cursor.getInt(index) == 1;
                }

                index = cursor.getColumnIndex(COLUMN_SUNDAY);
                if (index != -1) {
                    days[6] = cursor.getInt(index) == 1;
                }

                pill.setDays(days);

                Calendar calendar = Calendar.getInstance();
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                Log.d(TAG, "getPillsForTab: "+dayOfWeek);

                if (tabPosition == 0) { // History
                    if (isInPast(pill.getDatetime())) {
                        pills.add(pill);
                    }
                } else if (tabPosition == 1) { // Today
                    if (days[dayOfWeek - 1]) {
                        pills.add(pill);
                    }
                } else if (tabPosition == 2) { // Tomorrow
                    if (days[(dayOfWeek) % 7]) {
                        pills.add(pill);
                    }
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pills;
    }

    public boolean deletePill(int pillId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PILLS, COLUMN_ID + " = ?", new String[]{String.valueOf(pillId)});
        db.close();
        return result > 0;
    }



    private boolean isInPast(long pillTimestamp) {
        Calendar now = Calendar.getInstance();
        Calendar pillTime = Calendar.getInstance();
        pillTime.setTimeInMillis(pillTimestamp);

        return pillTime.before(now);
    }
}
