package com.example.d308_vacation_planner.UI.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.d308_vacation_planner.UI.Users;

public class LinkUsers extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "vacationPlanner.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_USER_EMAIL = "userEmail";
    private static final String COLUMN_USER_PASSWORD = "userPassword";

    public LinkUsers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT UNIQUE, "
                + COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate table if schema changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


     // Add a new user to the database.

    public void addUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getUserPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }


     // Check if a user already exists.

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID}, // We just need the ID to check existence
                COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst(); // true if at least one result
        cursor.close();
        db.close();
        return exists;
    }


     // Validate login credentials.
     // return true if email and password match.

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID}, // We just need the ID to confirm login
                COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password},
                null,
                null,
                null
        );

        boolean isValid = cursor.moveToFirst(); // true if credentials match
        cursor.close();
        db.close();
        return isValid;
    }
}



























