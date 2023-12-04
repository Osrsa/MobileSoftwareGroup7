package com.course.mobile_software_project_7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "Restaurant.db";

    public RestaurantDBHelper(Context context){
        super(context, DATABASE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MENU_TABLE = "CREATE TABLE " +
                RestaurantContract.MenuEntry.TABLE_NAME + " (" +
                RestaurantContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME + " TEXT NOT NULL, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME + " TEXT NOT NULL, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_PRICE + " TEXT NOT NULL, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_REVIEW + " TEXT, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_IMAGE_URL + " TEXT, " +
                RestaurantContract.MenuEntry.COLUMN_NAME_RESTAURANT_LOCATION + " TEXT);";

        db.execSQL(SQL_CREATE_MENU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RestaurantContract.MenuEntry.TABLE_NAME);
        onCreate(db);
    }
}
