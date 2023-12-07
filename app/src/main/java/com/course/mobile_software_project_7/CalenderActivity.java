package com.course.mobile_software_project_7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity {

    private ListView listViewMealsForDate;
    private ArrayAdapter<String> adapter;
    private RestaurantDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        CalendarView calendarView = findViewById(R.id.calendarView);
        listViewMealsForDate = findViewById(R.id.listViewMealsForDate);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewMealsForDate.setAdapter(adapter);
        dbHelper = new RestaurantDBHelper(this);

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
}