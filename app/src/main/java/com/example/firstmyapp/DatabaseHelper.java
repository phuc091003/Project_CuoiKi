package com.example.firstmyapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "User.db";
    private static final String USER_TABLE = "user_table";
    private static final String ORDER_TABLE = "order_table";
    private static final String COL_ORDER_ID = "ORDER_ID";
    private static final String COL_ORDER_DESCRIPTION = "DESCRIPTION";
    private static final String COL_ORDER_ADDRESS = "ADDRESS";
    private static final String COL_ORDER_USER_ID = "USER_ID";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE "+ ORDER_TABLE +"(ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,DESCRIPTION TEXT,ADDRESS TEXT,USER_ID INTEGER,FOREIGN KEY(USER_ID) REFERENCES "+ USER_TABLE + "(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        onCreate(db);
    }

    public boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        long result = db.insert(USER_TABLE, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public int getUserIdByUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM" + USER_TABLE + " WHERE USERNAME=?", new String[]{username});
        if (cursor.moveToFirst()){
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("ID"));
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1;
    }
    public boolean insertOrder(String description, String address, int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ORDER_DESCRIPTION, description);
        contentValues.put(COL_ORDER_ADDRESS, address);
        contentValues.put(COL_ORDER_USER_ID,userId);
        long result = db.insert(ORDER_TABLE,null, contentValues);
        return result != -1;
    }
    public Cursor getOrdersByUser(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + ORDER_TABLE + " WHERE USER_ID=?", new String[]{String.valueOf(userId)});
    }
}