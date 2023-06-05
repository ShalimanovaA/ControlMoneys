package com.example.controlmoneys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public  static final  int DATABASE_VERSION = 1;
    public  static final  String DATABASE_NAME = "contactDb";
    public  static final  String TABLE_CONTACTS = "contacts";

    public  static final  String KEY_ID = "_id";
    public  static final  String KEY_DATA = "data";
    public  static final  String KEY_KIND = "kind";
    public  static final  String KEY_CATEGORY = "category";
    public  static final  String KEY_SUM = "sum";

    // конструктор
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //обязатнльные методы
    // создание бд
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID +
                " integer primary key," + KEY_DATA + " text," + KEY_KIND +
                " text," + KEY_CATEGORY + " text," + KEY_SUM + " integer" + ")");
    }
    // обновление бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
}
