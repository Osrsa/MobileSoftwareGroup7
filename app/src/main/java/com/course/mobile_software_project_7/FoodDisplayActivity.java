package com.course.mobile_software_project_7;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FoodDisplayActivity extends AppCompatActivity {
    private ListView listViewMeals;
    private MealCursorAdapter mealAdapter;
    private RestaurantDBHelper dbHelper;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_display);

        checkAndRequestPermissions();

        listViewMeals = findViewById(R.id.listViewFood);
        dbHelper = new RestaurantDBHelper(this);

        Cursor cursor = dbHelper.readAllMeals();
        mealAdapter = new MealCursorAdapter(this, cursor);
        listViewMeals.setAdapter(mealAdapter);

        listViewMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 권한 확인
                if (ContextCompat.checkSelfPermission(FoodDisplayActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 있는 경우, 이미지 로드 로직
                    loadMealDetails(position);
                } else {
                    Toast.makeText(FoodDisplayActivity.this, "저장소 읽기 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMealDetails(int position) {
        Cursor cursor = (Cursor) mealAdapter.getItem(position);

        String mealName = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_NAME));
        String mealDate = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_TIME));
        String mealDetails = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_FOOD_REVIEW));
        String mealImagePath = cursor.getString(cursor.getColumnIndexOrThrow(RestaurantContract.MenuEntry.COLUMN_NAME_IMAGE_URL));

// ImageView를 생성하고 이미지 Uri를 설정합니다
        ImageView imageView = new ImageView(FoodDisplayActivity.this);
        try {
            Uri imageUri = Uri.parse(mealImagePath);
            imageView.setImageURI(imageUri);
        } catch (Exception e) {
            Toast.makeText(FoodDisplayActivity.this, "이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

// AlertDialog 빌더 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDisplayActivity.this);
        builder.setTitle(mealName); // 다이얼로그 제목을 식사 이름으로 설정
        builder.setMessage("날짜: " + mealDate + "\n세부사항: " + mealDetails); // 식사 상세 정보 표시
        builder.setView(imageView); // ImageView를 AlertDialog에 추가

// "닫기" 버튼 추가
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

// AlertDialog 표시
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여된 경우
                Toast.makeText(this, "권한이 부여되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면, 사용자에게 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
