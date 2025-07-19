package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class TambahLahan extends AppCompatActivity {

    EditText edtLokasiLahan, edtNamaLahan;
    ImageView imvtutup;
    Button btnTambahLahan;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lahan);

        SharedPreferences prefs = getSharedPreferences("MyApp",MODE_PRIVATE);
        userid = prefs.getInt("userid",-1);
        if(userid == -1){
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

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

        imvtutup = findViewById(R.id.imvtutup);
        edtLokasiLahan = findViewById(R.id.edtLokasiLahan);
        edtNamaLahan = findViewById(R.id.edtNamaLahan);
        btnTambahLahan = findViewById(R.id.btnTambahLahan);

        imvtutup.setOnClickListener(v -> {
            toPengaturanLahan();
        });
        //Pastikan Data Tidak Ada Yang Kosong Sebelum Melanjutkan Pengisian Data
        btnTambahLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ambil Data Nama dan Lokasi Lahan
                String namaLahan = edtNamaLahan.getText().toString();
                String lokasiLahan = edtLokasiLahan.getText().toString();
                //Validasi Keberadaan Data
                if (namaLahan.isEmpty() || lokasiLahan.isEmpty()) {
                    Toast.makeText(TambahLahan.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                toTambahDetailLahan();
            }
        });
    }


    public void toPengaturanLahan(){
        Intent intent = new Intent(this,PengaturanLahan.class);
        startActivity(intent);
    }
    public void toTambahDetailLahan(){
        Intent intent = new Intent(this,TambahDetailLahan.class);
        //Kirim Data Nama dan Lokasi Lahan ke Detail Lahan
        String namaLahan = edtNamaLahan.getText().toString();
        String lokasiLahan = edtLokasiLahan.getText().toString();
        intent.putExtra("namaLahan", namaLahan);
        intent.putExtra("lokasiLahan", lokasiLahan);
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

}

