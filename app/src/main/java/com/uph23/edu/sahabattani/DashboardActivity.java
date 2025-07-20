package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout llyTambahLahan, llyAturSensor;
    Realm realm;
    ArrayList<Lahan> lahanList;
    Button btnlahanpanenterdekat, btndetailcuaca;
    TextView txvnamaPanenLahan, txvNamaUser;
    RecyclerView rvMonitoring;
    Monitoring adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        btnlahanpanenterdekat = findViewById(R.id.btnlahanpanenterdekat);
        btndetailcuaca = findViewById(R.id.btndetailcuaca);
        txvnamaPanenLahan = findViewById(R.id.txvnamaPanenLahan);
        txvNamaUser = findViewById(R.id.txvNamaUser);





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
        realm.where(Lahan.class).findAllAsync().addChangeListener(lahans -> {
            lahanList = new ArrayList<>(lahans);
            muatDataDanPerhitungan();

            // Perbarui adapter agar RecyclerView ikut refresh
            adapter = new Monitoring(this, lahanList);
            rvMonitoring.setAdapter(adapter);
        });

        // Tambahkan juga agar dijalankan saat awal
        lahanList = new ArrayList<>(realm.where(Lahan.class).findAll());
        muatDataDanPerhitungan();

        // Set Adapter ke ListView
        rvMonitoring.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Monitoring(this, lahanList);
        rvMonitoring.setAdapter(adapter);

        btndetailcuaca.setOnClickListener(v -> {
            toStatistik();
        });



        //Ambil data Username dari sharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        String username = prefs.getString("username", "Pengguna");

        TextView tvNamaUser = findViewById(R.id.txvNamaUser);
        tvNamaUser.setText("Halo, " + username + "!");


    }

    // Fungsi untuk pindah ke halaman TambahLahan
    public void toTambahLahan(){
        Intent intent = new Intent(this, TambahLahan.class);
        startActivity(intent);
    }
    public void toStatistik(){
        Intent intent = new Intent(this, laporanActivity.class);
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
    private int hitungSisaHariPanen(String tanggalEstimasiPanen) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate panenDate = LocalDate.parse(tanggalEstimasiPanen, formatter);
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, panenDate);
            return (int) Math.max(days, 0);
        } catch (Exception e) {
            return 0;
        }
    }
    public void  muatDataDanPerhitungan() {
        //Logika Hitung lahan dengan sisa hari paling kecil
        Lahan lahanTerdekat = null;
        int sisaHariTerdekat = Integer.MAX_VALUE;
        if (lahanList != null && !lahanList.isEmpty()) {
            for (Lahan lahan : lahanList) {
                int sisaHari = hitungSisaHariPanen(lahan.getEstimasiPanen());
                if (sisaHari >= 0 && sisaHari < sisaHariTerdekat) {
                    sisaHariTerdekat = sisaHari;
                    lahanTerdekat = lahan;
                }
            }
        }

        //Bila Ada Lahan, Set nAma Lahan dengan estimasi panen terkecil
        if (lahanTerdekat != null) {
            txvnamaPanenLahan.setText("Lahan " + lahanTerdekat.getNamaLahan());
            final Lahan finalLahan = lahanTerdekat;
            //Kirim id lahan ke detail lahan agar bisa ditampilkan
            btnlahanpanenterdekat.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, DetailLahan.class);
                intent.putExtra("lahanId", finalLahan.getId());
                startActivity(intent);
            });
        }
        //Apabila belum ada lahan button tidak bisa diakses
        else {
            txvnamaPanenLahan.setText("Belum ada estimasi panen");
            btnlahanpanenterdekat.setEnabled(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.logout) {
            DialogKonfirmasi dialog = new DialogKonfirmasi(this, () -> {
                Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}