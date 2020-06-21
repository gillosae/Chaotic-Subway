package com.example.chaoticsubway.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "station.db";
    private static final int DATABASE_VERSION = 1;
    private static final  String DB_PATH = "../../../../../assets/stations";
    public static SQLiteDatabase mDB;

    private static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS " + StationContract.CreateDB._TABLENAME0;

    public StationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StationContract.CreateDB._CREATE0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ENTRIES);
        onCreate(db);
    }

}



