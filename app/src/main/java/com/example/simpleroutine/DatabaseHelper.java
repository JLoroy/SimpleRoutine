package com.example.simpleroutine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simple_routine.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STEPS = "steps";
    private static final String TABLE_HISTORY = "history";

    private static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_ORDER = "order";
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_STEP_ID = "stepID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEXT + " TEXT,"
                + KEY_ORDER + " INTEGER UNIQUE)";
        db.execSQL(CREATE_STEPS_TABLE);

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_DATETIME + " TEXT,"
                + KEY_STEP_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_STEP_ID + ") REFERENCES " + TABLE_STEPS + "(" + KEY_ID + "))";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void addStep(Step step) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, step.getText());
        values.put(KEY_ORDER, step.getOrder());

        db.insert(TABLE_STEPS, null, values);
        db.close();
    }

    public void updateStep(Step step) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, step.getText());
        values.put(KEY_ORDER, step.getOrder());

        db.update(TABLE_STEPS, values, KEY_ID + " = ?", new String[]{String.valueOf(step.getId())});
        db.close();
    }

    public void deleteStep(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEPS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Step> getAllSteps() {
        List<Step> stepsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STEPS + " ORDER BY " + KEY_ORDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Step step = new Step();
                step.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                step.setText(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
                step.setOrder(cursor.getInt(cursor.getColumnIndex(KEY_ORDER)));

                stepsList.add(step);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stepsList;
    }

    public void addHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATETIME, history.getDatetime());
        values.put(KEY_STEP_ID, history.getStepID());

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public List<History> getHistory(String startDate, String endDate) {
        List<History> historyList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY
                + " WHERE " + KEY_DATETIME + " BETWEEN ? AND ?"
                + " ORDER BY " + KEY_DATETIME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setDatetime(cursor.getString(cursor.getColumnIndex(KEY_DATETIME)));
                history.setStepID(cursor.getInt(cursor.getColumnIndex(KEY_STEP_ID)));

                historyList.add(history);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return historyList;
    }
}