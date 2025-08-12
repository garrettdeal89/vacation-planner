package com.example.d308_vacation_planner.UI.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.d308_vacation_planner.UI.Users;

public class LinkUsers extends SQLiteOpenHelper {

    private static String dbUser = "userDatabase";
    private static String dbTable = "userTable";
    private static int dbVersion = 1;

    private static String ID = "id";
    private static String name = "name";
    private static String email = "email";
    private static String password = "password";

    //constructor
    public LinkUsers(@Nullable Context context) {
        super(context, dbUser, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + dbTable + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + name + " TEXT, "
                + email + " TEXT, "
                + password + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + dbTable);
        onCreate(db);
    }

    public void addUser(Users users){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(name, users.getUserName());
        values.put(email, users.getUserEmail());
        values.put(password, users.getUserPassword());
        db.insert(dbTable, null, values);


    }

}



























