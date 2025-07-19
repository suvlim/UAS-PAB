package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uph23.edu.sahabattani.adapter.Monitoring;
import com.uph23.edu.sahabattani.model.Lahan;

import io.realm.Realm;
import io.realm.RealmResults;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uph23.edu.sahabattani.model.Sensor;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout llyTambahLahan, llyAturSensor;
    Realm realm;
    RecyclerView rvMonitoring;
    Monitoring adapter;

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


        // Pindah ke halaman TambahLahan saat tombol ditekan
        llyTambahLahan = findViewById(R.id.llyTambahLahan);
        llyTambahLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTambahLahan();
            }
        });

        // Pindah ke halaman AturSensor saat tombol ditekan
        llyAturSensor = findViewById(R.id.llyAturSensor);
        llyAturSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAturSensor();
            }
        });

        // Inisialisasi Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        // Temukan RecyclerView
        rvMonitoring = findViewById(R.id.rvMonitoring);

        // Ambil data lahan dari Realm
        RealmResults<Lahan> lahanList = realm.where(Lahan.class).findAll();

        // Set Adapter ke ListView
        rvMonitoring.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Monitoring(this, lahanList);
        rvMonitoring.setAdapter(adapter);


    }

    // Fungsi untuk pindah ke halaman TambahLahan
    public void toTambahLahan(){
        Intent intent = new Intent(this, TambahLahan.class);
        startActivity(intent);
    }

    // Fungsi untuk pindah ke halaman AturSensor
    public void toAturSensor(){
        RealmResults<Sensor> sensorResults = realm.where(Sensor.class).findAll();
        if(sensorResults != null) {
            Intent intent = new Intent(this, AturSensor.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, AturSensorTambah.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        // Untuk close realm
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

}