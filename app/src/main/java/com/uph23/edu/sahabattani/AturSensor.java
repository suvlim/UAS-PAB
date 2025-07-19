package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uph23.edu.sahabattani.adapter.SensorAdapter;
import com.uph23.edu.sahabattani.model.Lahan;
import com.uph23.edu.sahabattani.model.Sensor;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AturSensor extends AppCompatActivity {
    FloatingActionButton fabTambahSensor;
    ImageView imvtutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atur_sensor);

        //Pengaturan Bottom NavBar
        BottomNavigationView btmNav = findViewById(R.id.bottom_nav);
        //Set Halaman Lahan
        btmNav.setSelectedItemId(R.id.navigation_lahan);
        btmNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_lahan) {
                return true;
                //Pindah ke halaman Dashboard
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
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
        //Tambah dan Pindah Ke Halaman Atur Sensor Tambah
        fabTambahSensor = findViewById(R.id.fabTambahSensorLahan);
        fabTambahSensor.setOnClickListener(view -> {
            toTambahSensor();

        });

        //Balik ke Halaman Pengaturan Lahan
        imvtutup = findViewById(R.id.imvtutup);
        imvtutup.setOnClickListener(v -> {
            toPengaturanLahan();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true) // sementara aktifkan untuk demo
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();
        // Buat daftar gabungan untuk header dan sensor
        final ArrayList<Object> dataList = new ArrayList<>();
        RealmResults<Lahan> lahanResults = realm.where(Lahan.class).findAll();
        for (Lahan lahan : lahanResults) {
            dataList.add(lahan); // Tambah header lahan
            RealmResults<Sensor> sensorResults = realm.where(Sensor.class).equalTo("lahan.id", lahan.getId()).findAll();
            dataList.addAll(realm.copyFromRealm(sensorResults)); // Tambah daftar sensor
        }

        // Inisialisasi Adapter dengan data gabungan
        SensorAdapter sensorAdapter = new SensorAdapter(this, dataList); // Asumsikan SensorAdapter sudah mendukung dua tipe view
        ListView numbersListView = findViewById(R.id.lsvSensor);
        numbersListView.setAdapter(sensorAdapter);

        // Update adapter saat data berubah
        realm.addChangeListener(realm1 -> sensorAdapter.notifyDataSetChanged());

    }

    public void toTambahSensor() {
        Intent intent = new Intent(this, AturSensorTambah.class);
        startActivity(intent);
    }
    public void toPengaturanLahan(){
        Intent intent = new Intent(this,PengaturanLahan.class);
        startActivity(intent);
    }
}