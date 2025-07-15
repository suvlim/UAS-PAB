package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uph23.edu.sahabattani.adapter.LahanAdapter;
import com.uph23.edu.sahabattani.model.Lahan;

import java.util.ArrayList;

import com.uph23.edu.sahabattani.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PengaturanLahan extends AppCompatActivity {
    Realm realm;
    LahanAdapter adapter;
    ListView listView;
    FloatingActionButton fabTambahLahan;
    ArrayList<Lahan> lahanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan_lahan);



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

        // Inisialisasi Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();


        // Inisialisasi ListView
        listView = findViewById(R.id.lsvLahan);

        // Ambil data dari Realm
        RealmResults<Lahan> results = realm.where(Lahan.class).findAll();
        lahanArrayList = new ArrayList<>(realm.copyFromRealm(results));

        // Inisialisasi dan set Adapter
        adapter = new LahanAdapter(this, lahanArrayList);
        listView.setAdapter(adapter);


        //Fungsi Tambah Lahan
        fabTambahLahan = findViewById(R.id.fabTambahLahan);
        fabTambahLahan.setOnClickListener(view -> {
            toTambahLahan();

        });

        //Mengarahkan User Ke Detail Lahan
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Lahan selectedLahan = lahanArrayList.get(position);
            //Kirim Id Lahan ke Halaman detail Lahan
            Intent intent = new Intent(PengaturanLahan.this, DetailLahan.class);
            intent.putExtra("lahanId", selectedLahan.getId());
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
    //Refresh Data untuk menampilkan listview terbaru
    private void loadDataLahan() {
        RealmResults<Lahan> results = realm.where(Lahan.class).findAll();
        lahanArrayList.clear();
        lahanArrayList.addAll(realm.copyFromRealm(results));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataLahan();
    }

    public void toTambahLahan(){
        Intent intent = new Intent(this,TambahLahan.class);
        startActivity(intent);
    }

}