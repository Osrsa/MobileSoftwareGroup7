package com.course.mobile_software_project_7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    public Cursor readAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                RestaurantContract.MenuEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public List<String> getMealsForDate(String selectedDate) {
        List<String> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME,
                RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME
        };

        String selection = RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME + " LIKE ?";
        String[] selectionArgs = { selectedDate + "%" };

        Cursor cursor = db.query(
                RestaurantContract.MenuEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String mealName = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME));
                String mealTime = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME));

                // Format the meal information
                String mealInfo = "메뉴: " + mealName + ", 날짜: " + mealTime;
                mealList.add(mealInfo);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return mealList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RestaurantContract.MenuEntry.TABLE_NAME);
        onCreate(db);
    }
}
