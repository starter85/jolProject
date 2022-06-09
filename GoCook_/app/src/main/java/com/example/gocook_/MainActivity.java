package com.example.gocook_;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        bottomNavigationView = findViewById(R.id.bottemNavi);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainFragment()).commit(); //FrameLayout에 fragment.xml 띄우기

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.item_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MainFragment()).commit();
                        break;
                    case R.id.item_recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ReadFragment()).commit();
                        break;
                    case R.id.item_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit();
                        break;              }
                return true;
            }
        });
    }
}

