package com.course.mobile_software_project_7;

import android.provider.BaseColumns;

public class RestaurantContract {
    private RestaurantContract() {
    }

    public static class MenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "menu";
        public static final String COLUMN_NAME_FOOD_NAME = "food_name";
        public static final String COLUMN_NAME_FOOD_TIME = "food_time";
        public static final String COLUMN_NAME_FOOD_PRICE = "food_price";
        public static final String COLUMN_NAME_FOOD_REVIEW = "food_review";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_RESTAURANT_LOCATION = "restaurant_location";
        //public static final String COLUMN_NAME_FOOD_CALORIES = "food_calories";
    }
}
