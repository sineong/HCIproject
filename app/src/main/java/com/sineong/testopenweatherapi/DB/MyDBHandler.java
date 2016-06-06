package com.sineong.testopenweatherapi.DB;


/**
 * Created by SEOYOMI on 6/5/16.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.*;


public class MyDBHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CriteriaDB.db";
    public static final String TABLE_CRITERIA = "criteria";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_INNER_MAX = "inner_max";
    public static final String COLUMN_BOTTOM_MAX = "bottom_max";
    public static final String COLUMN_OUTER_MIN1 = "outer_min1";
    public static final String COLUMN_OUTER_MIN2 = "outer_min2";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_CRITERIA_TABLE = "CREATE TABLE " + TABLE_CRITERIA + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_INNER_MAX + " INTEGER, "
                + COLUMN_BOTTOM_MAX + " INTEGER, "
                + COLUMN_OUTER_MIN1 + " INTEGER, "
                + COLUMN_OUTER_MIN2 + " INTEGER); Insert into criteria (inner_max, bottom_max, outer_min1, outer_min2) values (21, 23, 20, 14);";
        db.execSQL(CREATE_CRITERIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRITERIA);
        onCreate(db);
    }

    public void addCriteria(Criteria criteria){

        ContentValues values = new ContentValues();
        values.put(COLUMN_INNER_MAX, criteria.getInner_max());
        values.put(COLUMN_BOTTOM_MAX, criteria.getBottom_max());
        values.put(COLUMN_OUTER_MIN1, criteria.getOuter_min1());
        values.put(COLUMN_OUTER_MIN2, criteria.getOuter_min2());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CRITERIA, null, values);
        db.close();
    }

    public Criteria findLatestCriteria(){
        String query = "Select inner_max, bottom_max, outer_min1, outer_min2 from " + TABLE_CRITERIA + " order by id desc limit 1;";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        Criteria criteria = new Criteria();

        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            criteria.setInner_max(Integer.parseInt(cursor.getString(0)));
            criteria.setBottom_max(Integer.parseInt(cursor.getString(1)));
            criteria.setOuter_min1(Integer.parseInt(cursor.getString(2)));
            criteria.setOuter_min2(Integer.parseInt(cursor.getString(3)));
        }
        else {
            criteria.setInner_max(21);
            criteria.setBottom_max(23);
            criteria.setOuter_min1(20);
            criteria.setOuter_min2(14);
        }

        db.close();
        return criteria;
    }
}
