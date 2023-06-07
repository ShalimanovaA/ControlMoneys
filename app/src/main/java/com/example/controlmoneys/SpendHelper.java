package com.example.controlmoneys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SpendHelper extends SQLiteOpenHelper {
    public  static final  int DATABASE_VERSION = 2;
    public  static final  String DATABASE_NAME = "spendDb";
    public  static final  String TABLE_CONTACTS = "spends";

    public  static final  String KEY_ID = "_id";
    public  static final  String KEY_CATEGORY = "category";
    public  static final  String KEY_NAME = "name";
    public  static final  String KEY_USERNAME = "username";

    public  static final  String KEY_SUM = "sum";

    // конструктор
    public SpendHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //обязатнльные методы
    // создание бд
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID +
                " integer primary key," +  KEY_USERNAME + " text,"+  KEY_CATEGORY + " text," +
                KEY_NAME + " text," +KEY_SUM + " integer" + ")");
    }
    // обновление бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
}
