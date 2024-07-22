package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "contacts_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "contactdata";
    public static final String C_ID = "id";
    public static final String C_NAME = "name";
    public static final String C_PHONE = "phone";
    public static final String C_EMAIL = "email";
    public static final String C_ADDED_TIME = "added";
    public static final String C_UPDATED_TIME = "updated";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_NAME + " TEXT,"
                + C_PHONE + " TEXT,"
                + C_EMAIL + " TEXT,"
                + C_ADDED_TIME + " TEXT DEFAULT CURRENT_TIMESTAMP,"
                + C_UPDATED_TIME + " TEXT DEFAULT CURRENT_TIMESTAMP"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertData(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NAME, name);
        values.put(C_PHONE, phone);
        values.put(C_EMAIL, email);
        return db.insert(TABLE_NAME, null, values);
    }

    public long updateData(String id, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NAME, name);
        values.put(C_PHONE, phone);
        values.put(C_EMAIL, email);
        String selection = C_ID + " = ?";
        String[] selectionArgs = { id };
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, C_ID + " = ?", new String[]{id});
    }

    public void deleteallcontacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public modelclass getContactById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                C_ID,
                C_NAME,
                C_PHONE,
                C_EMAIL,
                C_ADDED_TIME,
                C_UPDATED_TIME
        };
        String selection = C_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                TABLE_NAME,         // The table to query
                projection,         // The array of columns to return (pass null to get all)
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                null                // The sort order
        );

        modelclass contact = null;
        if (cursor != null && cursor.moveToFirst()) {
            contact = new modelclass();
            contact.setId(cursor.getString(cursor.getColumnIndexOrThrow(C_ID)));
            contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(C_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(C_PHONE)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(C_EMAIL)));
            contact.setAddedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_ADDED_TIME)));
            contact.setUpdatedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_UPDATED_TIME)));
            cursor.close();
        }
        return contact;
    }

    public ArrayList<modelclass> getcontacts() {
        ArrayList<modelclass> arrayList = new ArrayList<>();
        String selectqry = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectqry, null);
        while (cursor.moveToNext()) {
            modelclass mc = new modelclass();
            mc.setId(cursor.getString(cursor.getColumnIndexOrThrow(C_ID)));
            mc.setName(cursor.getString(cursor.getColumnIndexOrThrow(C_NAME)));
            mc.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(C_PHONE)));
            mc.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(C_EMAIL)));
            mc.setAddedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_ADDED_TIME)));
            mc.setUpdatedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_UPDATED_TIME)));
            arrayList.add(mc);
        }
        cursor.close();
        return arrayList;
    }
    public ArrayList<modelclass> getsearchcontact(String query) {
        ArrayList<modelclass> arrayList = new ArrayList<>();
        String selectqry = "SELECT * FROM " + TABLE_NAME + " WHERE " + C_NAME + " LIKE ?";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectqry, new String[]{"%" + query + "%"});
        while (cursor.moveToNext()) {
            modelclass mc = new modelclass();
            mc.setId(cursor.getString(cursor.getColumnIndexOrThrow(C_ID)));
            mc.setName(cursor.getString(cursor.getColumnIndexOrThrow(C_NAME)));
            mc.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(C_PHONE)));
            mc.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(C_EMAIL)));
            mc.setAddedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_ADDED_TIME)));
            mc.setUpdatedtime(cursor.getString(cursor.getColumnIndexOrThrow(C_UPDATED_TIME)));
            arrayList.add(mc);
        }
        cursor.close();
        return arrayList;
    }
}
