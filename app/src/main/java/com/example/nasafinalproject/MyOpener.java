package com.example.nasafinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "NASAImagesDB";
    protected final static int VERSION_NUM = 2;
    public final static String TABLE_NAME = "NASA_IMAGES";
    public final static String COL_DATE = "DATE";
    public final static String COL_EXPLANATION = "EXPLANATION";
    public final static String COL_URL = "URL";
    public final static String COL_HDURL = "HDURL";
    public final static String COL_IMG_FILE = "IMG_FILE";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_ID = "_id";

    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE  + " text,"
                + COL_DATE  + " text,"
                + COL_EXPLANATION  + " text,"
                + COL_URL  + " text,"
                + COL_HDURL  + " text,"
                + COL_IMG_FILE  + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}