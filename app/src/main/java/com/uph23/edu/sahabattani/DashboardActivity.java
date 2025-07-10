package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout llyTambahLahan, llyAturSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        //Pengaturan Bottom NavBar
        BottomNavigationView btmNav = findViewById(R.id.bottom_nav);
        //Set Halaman Home
        btmNav.setSelectedItemId(R.id.navigation_home);
        btmNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
                //Pindah ke halaman lahan
            } else if (itemId == R.id.navigation_lahan) {
                startActivity(new Intent(getApplicationContext(), PengaturanLahan.class));
                overridePendingTransition(0, 0);
                return true;
                //Pindah ke halaman laporan statistik
            } else if (itemId == R.id.navigation_statistik) {
                startActivity(new Intent(getApplicationContext(), laporanActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        llyTambahLahan = findViewById(R.id.llyTambahLahan);
        llyTambahLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTambahLahan();
            }
        });

        llyAturSensor = findViewById(R.id.llyAturSensor);
        llyAturSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAturSensor();
            }
        });
    }
    public void toTambahLahan(){
        Intent intent = new Intent(this, TambahLahan.class);
        startActivity(intent);
    }

    public void toAturSensor(){
        Intent intent = new Intent(this, AturSensor.class);
        startActivity(intent);
    }
}