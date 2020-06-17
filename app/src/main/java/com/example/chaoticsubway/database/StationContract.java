package com.example.chaoticsubway.database;

import android.provider.BaseColumns;

//테이블 구조
public final class StationContract {
    StationContract(){};

    public static final class CreateDB implements BaseColumns{
        public static final String LINE = "line";
        public static final String STATION = "station";
        public static final String CODE = "station code";
        public static final String DISTANCE = "distance";
        public static final String TIME = "time";
        public static final String _TABLENAME0 = "stations";
        public static final String _CREATE0 = "create table if not exists"+_TABLENAME0 + "(" + _ID + " integer primary key autoincrement" + LINE +" text not null ,"+
                STATION + " text not null , "+ CODE + " text not null , " + DISTANCE + " text not null , " + TIME + " text not null ); ";
    }

}