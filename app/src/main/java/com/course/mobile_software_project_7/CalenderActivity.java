package com.course.mobile_software_project_7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    private ListView listViewMealsForDate;
    private ArrayAdapter<String> adapter;
    private RestaurantDBHelper dbHelper;
    private Button analysisButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        CalendarView calendarView = findViewById(R.id.calendarView);
        listViewMealsForDate = findViewById(R.id.listViewMealsForDate);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewMealsForDate.setAdapter(adapter);
        analysisButton = findViewById(R.id.AnalysisButton);
        dbHelper = new RestaurantDBHelper(this);

        analysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnalysisDialog();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 선택한 날짜를 데이터베이스에 저장된 형식과 비슷한 형식으로 변환
                String selectedDate = String.format(Locale.getDefault(), "%04d/%02d/%02d", year, month + 1, dayOfMonth);

                // 해당 날짜의 음식 정보 가져오기
                List<String> mealList = dbHelper.getMealsForDate(selectedDate);

                if (mealList != null && !mealList.isEmpty()) {
                    // 가져온 음식 정보가 있을 경우 ListView에 표시
                    adapter.clear();
                    adapter.addAll(mealList);
                    listViewMealsForDate.setVisibility(View.VISIBLE); // ListView 보이도록 설정
                } else {
                    // 가져온 음식 정보가 없을 경우 ListView 숨기기 및 Toast 메시지 표시
                    listViewMealsForDate.setVisibility(View.GONE); // ListView 숨기기
                    Toast.makeText(CalenderActivity.this, "해당 날짜에 기록된 음식이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showAnalysisDialog() {
        // 마지막 한 달간의 데이터를 분석
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String oneMonthAgo = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(food_price) as TotalPrice, " +
                        "SUM(CASE WHEN calories IS NOT NULL THEN calories ELSE (RANDOM() % 3001 + 2000) / 100.0 END) as TotalCalories, " +
                        "CASE " +
                        "WHEN substr(food_time, 1, 2) < '10' THEN '조식' " +
                        "WHEN substr(food_time, 1, 2) >= '10' AND substr(food_time, 1, 2) < '15' THEN '중식' " +
                        "WHEN substr(food_time, 1, 2) >= '15' THEN '석식' " +
                        "END as MealType " +
                        "FROM menu " +
                        "WHERE food_time >= ? " +
                        "GROUP BY MealType",
                new String[]{oneMonthAgo}
        );

        StringBuilder analysisResult = new StringBuilder();
        int totalPrice = 0;
        double totalCalories = 0;

        while (cursor.moveToNext()) {
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("TotalPrice"));
            double calories = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalCalories"));
            String mealType = cursor.getString(cursor.getColumnIndexOrThrow("MealType"));

            totalPrice += price;
            totalCalories += calories;

            analysisResult.append(String.format(Locale.getDefault(),
                    "식사 유형: %s\n비용: %d원\n칼로리: %.2f kcal\n\n", mealType, price, calories));
        }

        cursor.close();
        db.close();

        // AlertDialog를 생성하고 결과를 보여줌
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("최근 1달 간의 식사 분석");
        builder.setMessage("총 비용: " + totalPrice + "원\n총 칼로리: " + String.format("%.2f", totalCalories) + " kcal\n\n" + analysisResult);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}