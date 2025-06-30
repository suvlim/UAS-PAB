package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
//        bottomNav.setSelectedItemId(R.id.navigation_home); // tandai aktif
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    return true; // sudah di halaman ini
//
//                case R.id.navigation_lahan:
//                    startActivity(new Intent(getApplicationContext(), PengaturanLahan.class));
//                    overridePendingTransition(0, 0);
//                    return true;
//                case R.id.navigation_statistik:
//                    startActivity(new Intent(getApplicationContext(), StatistikActivity.class));
//                    overridePendingTransition(0, 0);
//                    return true;
//            }
//            return false;
//        });
    }
}