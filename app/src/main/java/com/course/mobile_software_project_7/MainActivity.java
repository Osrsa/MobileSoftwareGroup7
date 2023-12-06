package com.course.mobile_software_project_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String RESTAURANT_NAME = "restaurant_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void restaurant1ButtonClick(View view){
        Intent intent = new Intent(this, FoodInputActivity.class);
        intent.putExtra(RESTAURANT_NAME, "상록원 1층");
        startActivity(intent);
    }
    public void restaurant2ButtonClick(View view){
        Intent intent = new Intent(this, FoodInputActivity.class);
        intent.putExtra(RESTAURANT_NAME, "상록원 2층");
        startActivity(intent);
    }
    public void restaurant3ButtonClick(View view){
        Intent intent = new Intent(this, FoodInputActivity.class);
        intent.putExtra(RESTAURANT_NAME, "남산학사 카페");
        startActivity(intent);
    }

    public void DisplayButtonClick(View view){
        Intent intent = new Intent(this, FoodDisplayActivity.class);
        startActivity(intent);
    }
}