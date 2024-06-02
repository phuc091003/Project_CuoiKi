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
    private static final String ORDER_ITEM_TABLE = "order_item_table";
    private static final String ITEM_TABLE = "item_table";
    private static final String COL_ORDER_ID = "ORDER_ID";
    private static final String COL_ITEM_ID = "ITEM_ID";
    private static final String COL_ITEM_NAME = "NAME";
    private static final String COL_ITEM_PRICE = "PRICE";
    private static final String COL_ORDER_DESCRIPTION = "DESCRIPTION";
    private static final String COL_ORDER_ADDRESS = "ADDRESS";
    private static final String COL_ORDER_USER_ID = "USER_ID";
    private static final String COL_ORDER_ITEM_ORDER_ID = "ORDER_ID";
    private static final String COL_ORDER_ITEM_ITEM_ID = "ITEM_ID";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + ORDER_TABLE + "(ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,DESCRIPTION TEXT,ADDRESS TEXT,USER_ID INTEGER,FOREIGN KEY(USER_ID) REFERENCES " + USER_TABLE + "(ID))");
        db.execSQL("CREATE TABLE " + ITEM_TABLE + " (ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PRICE REAL)");
        db.execSQL("CREATE TABLE " + ORDER_ITEM_TABLE + " (ORDER_ID INTEGER, ITEM_ID INTEGER, FOREIGN KEY(ORDER_ID) REFERENCES " + ORDER_TABLE + "(ORDER_ID), FOREIGN KEY(ITEM_ID) REFERENCES " + ITEM_TABLE + "(ITEM_ID))");

        // them 1 vai item
        insertItem(db, "Do an", 10000);
        insertItem(db, "nuoc uong", 5000);
        insertItem(db, "do an vat", 2000);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_ITEM_TABLE);
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

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM " + USER_TABLE + " WHERE USERNAME=?", new String[]{username});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("ID"));
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1;
    }

    public boolean insertOrder(String description, String address, int userId, int[] itemIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ORDER_DESCRIPTION, description);
            contentValues.put(COL_ORDER_ADDRESS, address);
            contentValues.put(COL_ORDER_USER_ID, userId);
            long orderId = db.insert(ORDER_TABLE, null, contentValues);
            if (orderId == -1) return false;
            for (int itemId : itemIds) {
                ContentValues orderItemValues = new ContentValues();
                orderItemValues.put(COL_ORDER_ITEM_ORDER_ID, orderId);
                orderItemValues.put(COL_ORDER_ITEM_ITEM_ID, itemId);
                long result = db.insert(ORDER_ITEM_TABLE, null, orderItemValues);
                if (result == -1) return false;
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }
    public Cursor getOrdersByUser(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + ORDER_TABLE + " WHERE USER_ID=?", new String[]{String.valueOf(userId)});
    }
    public Cursor getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + ITEM_TABLE, null);
    }
    public void insertItem (SQLiteDatabase db, String name, double price){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ITEM_NAME, name);
        contentValues.put(COL_ITEM_PRICE, price);
        db.insert(ITEM_TABLE, null, contentValues);
    }
}