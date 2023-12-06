package com.course.mobile_software_project_7;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MealCursorAdapter extends CursorAdapter {

    public MealCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView FoodName = view.findViewById(R.id.FoodName);
        //TextView FoodPrice = view.findViewById(R.id.FoodPrice);
        TextView FoodTime = view.findViewById(R.id.FoodTime);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME));
        //String price = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_PRICE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME));

        FoodName.setText(name);
        //FoodPrice.setText(price);
        FoodTime.setText(time);
    }
}
