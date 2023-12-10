package com.course.mobile_software_project_7;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class FoodInputActivity extends AppCompatActivity{
    private EditText editTextFoodName;
    private EditText editTextTime;
    private EditText editTextPrice;
    private EditText editTextReview;
    private ImageView foodImage;
    private SQLiteDatabase database;
    private String ImagePath = "";
    private TextView textViewRestaurantName;

    private static final int GALLERY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);

        editTextFoodName = findViewById(R.id.editTextFoodName);
        editTextTime = findViewById(R.id.editTextTime);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextReview = findViewById(R.id.editTextReview);
        foodImage = findViewById(R.id.foodImage);
        textViewRestaurantName = findViewById(R.id.restaurantName);

        if(getIntent().hasExtra(MainActivity.RESTAURANT_NAME)){     //식당 이름 받아와서 textView에 전달
            String restaurantName = getIntent().getStringExtra(MainActivity.RESTAURANT_NAME);
            textViewRestaurantName.setText(restaurantName);
        }

        RestaurantDBHelper dbHelper = new RestaurantDBHelper(this);
        database = dbHelper.getWritableDatabase();

        foodImage.setOnClickListener(new View.OnClickListener() {       //foodImage 클릭 시 갤러리 열기
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery(){         //갤러리 열기
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                foodImage.setImageURI(selectedImageUri); // foodImage에 선택한 이미지 표시
                ImagePath = getPathFromUri(selectedImageUri); // 이미지의 경로 가져오기
            }
        }
    }
    private String getPathFromUri(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return "";
    }
    public void onSaveButtonClick(View view) {
        String foodName = editTextFoodName.getText().toString().trim();
        String foodTime = editTextTime.getText().toString().trim();
        String foodPrice = editTextPrice.getText().toString().trim();
        String foodReview = editTextReview.getText().toString().trim();
        String restaurantLocation = textViewRestaurantName.getText().toString().trim();
        double calories = (new Random().nextInt(3001) + 2000);

        // 데이터베이스에 음식 정보를 저장하는 코드
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME, foodName);
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME, foodTime);
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_PRICE, foodPrice);
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_REVIEW, foodReview);
        if (ImagePath != "")
            values.put(RestaurantContract.MenuEntry.COLUMN_NAME_IMAGE_URL, ImagePath);
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_RESTAURANT_LOCATION, restaurantLocation);
        values.put(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_CALORIES, calories);

        // 실제 데이터베이스에 데이터 추가
        long newRowId = database.insert(RestaurantContract.MenuEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "음식 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "음식 정보 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }

}
