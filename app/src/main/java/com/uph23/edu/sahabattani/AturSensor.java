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
    private Realm realm;
    ImageView imvtutup;
    private SensorAdapter sensorAdapter;
    private ArrayList<Object> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atur_sensor);

        BottomNavigationView btmNav = findViewById(R.id.bottom_nav);
        btmNav.setSelectedItemId(R.id.navigation_lahan);
        btmNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_lahan) return true;
            else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_statistik) {
                startActivity(new Intent(getApplicationContext(), laporanActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        imvtutup = findViewById(R.id.imvtutup);
        imvtutup.setOnClickListener(v -> {
            toPengaturanLahan();
        });

        fabTambahSensor = findViewById(R.id.fabTambahSensorLahan);
        fabTambahSensor.setOnClickListener(view -> toTambahSensor());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance(); // pakai field class

        // Atur adapter
        sensorAdapter = new SensorAdapter(this, dataList);
        ListView numbersListView = findViewById(R.id.lsvSensor);
        numbersListView.setAdapter(sensorAdapter);

        // Load awal dan pantau perubahan
        loadData();
        realm.addChangeListener(r -> loadData());
    }

    private void loadData() {
        dataList.clear();
        RealmResults<Lahan> lahanResults = realm.where(Lahan.class).findAll();
        for (Lahan lahan : lahanResults) {
            dataList.add(lahan);
            RealmResults<Sensor> sensorResults = realm.where(Sensor.class)
                    .equalTo("lahan.id", lahan.getId())
                    .findAll();
            dataList.addAll(realm.copyFromRealm(sensorResults));
        }
        sensorAdapter.notifyDataSetChanged(); // panggil milik field class
    }

    private void toTambahSensor() {
        Intent intent = new Intent(this, AturSensorTambah.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
    public void toPengaturanLahan(){
        Intent intent = new Intent(this,PengaturanLahan.class);
        startActivity(intent);
    }
}
