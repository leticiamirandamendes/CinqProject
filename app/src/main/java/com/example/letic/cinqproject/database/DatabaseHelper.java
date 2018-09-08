package com.example.letic.cinqproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.letic.cinqproject.models.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";
    private static final String TABLE_NAME = "cinqusers";
    private static final String ID = "_id";
    private static final String NAME = "nome";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private static final String CREATE_DB = "create table "
            + TABLE_NAME + "(" + ID
            + " integer primary key autoincrement, " + NAME
            + " varchar(50), " + EMAIL + " varchar(50), "
            + PASSWORD + " varchar(50));";

    private String[] columns = {ID, NAME, EMAIL, PASSWORD};

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void createUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, user.getName());
        values.put(EMAIL, user.getEmail());
        values.put(PASSWORD, user.getPassword());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String sort = NAME + " ASC";
        List users = new ArrayList();

        Cursor cursor = db.query(TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sort);

        if (cursor.moveToFirst()) {
            do {

                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
                users.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return users;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, user.getName());
        values.put(PASSWORD, user.getPassword());
        db.update(TABLE_NAME, values, EMAIL + " = ?",
                new String[]{String.valueOf(user.getEmail())});

        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.ID};
        String selection = DatabaseHelper.EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DatabaseHelper.ID};
        String selection = DatabaseHelper.EMAIL + " = ?" + " AND " + DatabaseHelper.PASSWORD + " = ?";

        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        return (cursorCount > 0);
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        User user = new User();

        if (cursor.moveToFirst()) {

            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));

        }
        cursor.close();
        db.close();
        return user;
    }
}
