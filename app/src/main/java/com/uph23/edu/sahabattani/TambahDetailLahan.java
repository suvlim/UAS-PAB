package com.uph23.edu.sahabattani;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.uph23.edu.sahabattani.model.Lahan;

import java.util.Calendar;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import io.realm.Realm;

public class TambahDetailLahan extends AppCompatActivity {
    Realm realm;

    String  jenisTanaman, tanggalMulaiTanam, tanggalEstimasiPanen, kelembapanTanah, estimasiPanen;
    Calendar tanggalTanam;
    EditText edtAwalPenanaman, edt_kelembapan_tanah;
    Button btn_simpan;
    MaterialButton btn_padi, btn_jagung;
    TextView txvNamaLahan, txvLokasiLahan, txvEstimasiPanen, txv_prediksi_panen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_detail_lahan);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtAwalPenanaman = findViewById(R.id.edt_awal_penanaman);
        edt_kelembapan_tanah = findViewById(R.id.edt_kelembapan_tanah);
        btn_padi = findViewById(R.id.btn_padi);
        btn_jagung = findViewById(R.id.btn_jagung);
        btn_simpan = findViewById(R.id.btn_simpan);
        txvNamaLahan = findViewById(R.id.txvNamaLahan);
        txvLokasiLahan = findViewById(R.id.txvLokasiLahan);
        txvEstimasiPanen = findViewById(R.id.txvEstimasiPanen);
        txv_prediksi_panen = findViewById(R.id.txv_prediksi_panen);

        //Masukkan Intent Nama dan Lokasi dari Tambah Lahan dan Set Sebagai Keterangan Di Atas
        String namaLahan = getIntent().getStringExtra("namaLahan");
        txvNamaLahan.setText("Lahan Sawah " + namaLahan);
        String lokasiLahan = getIntent().getStringExtra("lokasiLahan");
        txvLokasiLahan.setText(lokasiLahan);

        //Buat Warna Khusus Untuk Menandakan Saat Tombol Ditekan
        int warnaAktif = getResources().getColor(R.color.forest_green);
        int warnaDefault = getResources().getColor(android.R.color.white);
        int warnaTextAktif = getResources().getColor(android.R.color.white);
        int warnaTextDefault = getResources().getColor(R.color.black);

        //Set String Jenis tanaman berdasarkan tombol yang diklik
        btn_padi.setOnClickListener(v -> {
            jenisTanaman = "Padi";
            //Ganti Warna Tombol Padi
            btn_padi.setBackgroundTintList(ColorStateList.valueOf(warnaAktif));
            btn_jagung.setBackgroundTintList(ColorStateList.valueOf(warnaDefault));
            btn_padi.setTextColor(warnaTextAktif);
            btn_jagung.setTextColor(warnaTextDefault);
            btn_padi.setIconTint(ColorStateList.valueOf(Color.WHITE));
            btn_jagung.setIconTint(ColorStateList.valueOf(Color.BLACK));
        });
        btn_jagung.setOnClickListener(v -> {
            jenisTanaman = "Jagung";
            //Ganti Warna Tombol Jagung
            btn_jagung.setBackgroundTintList(ColorStateList.valueOf(warnaAktif));
            btn_padi.setBackgroundTintList(ColorStateList.valueOf(warnaDefault));
            btn_jagung.setTextColor(warnaTextAktif);
            btn_padi.setTextColor(warnaTextDefault);
            btn_jagung.setIconTint(ColorStateList.valueOf(Color.WHITE));
            btn_padi.setIconTint(ColorStateList.valueOf(Color.BLACK));
        });

        //Set Kelembapan Tanah
        kelembapanTanah = edt_kelembapan_tanah.getText().toString();

        //Calonder (date Picker)
        edtAwalPenanaman.setOnClickListener(view -> {
            //Dapatkan tanggal hari ini
            Calendar now = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, y, m, d) -> {
                //Gunakan tanggal hari ini sebelum tanggal dipilih user
                tanggalTanam = Calendar.getInstance();
                tanggalTanam.set(y, m, d);
                //Bulan +1 karena Java Mulai dari 0
                tanggalMulaiTanam = d + "/" + (m + 1) + "/" + y;

                edtAwalPenanaman.setText(tanggalMulaiTanam);

                //Hitung Estimasi Panen (Padi 120 hari, jagung 90 hari)
                Calendar panen = (Calendar) tanggalTanam.clone();
                if ("Padi".equals(jenisTanaman)) panen.add(Calendar.DAY_OF_YEAR, 120);
                else if ("Jagung".equals(jenisTanaman)) panen.add(Calendar.DAY_OF_YEAR, 90);

                tanggalEstimasiPanen = panen.get(Calendar.DAY_OF_MONTH) + "/" +
                        (panen.get(Calendar.MONTH) + 1) + "/" + panen.get(Calendar.YEAR);
                txv_prediksi_panen.setText(tanggalEstimasiPanen);

                //Hitung Jumlah Hari Menuju Panen
                int sisaHariPanen = hitungSisaHariPanen(tanggalEstimasiPanen);
                txvEstimasiPanen.setText("Estimasi Panen: " + sisaHariPanen + "hari");
                //Set Tanggal Estimasi Panen

            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
        //Simpan Data
        btn_simpan.setOnClickListener(view -> {
            kelembapanTanah = edt_kelembapan_tanah.getText().toString();
            //Validasi apakah semua data terisi dan kelembapan tanah 1-100
            if (!validasiData() || !validasiKelembapan(kelembapanTanah)) {
                return;
            }

            // Simpan Data ke Realm
            realm.executeTransactionAsync(r -> {
                Lahan lahan = r.createObject(Lahan.class, new Random().nextInt(999999));
                lahan.setNamaLahan(namaLahan);
                lahan.setLokasiLahan(lokasiLahan);
                lahan.setJenisTanaman(jenisTanaman);
                lahan.setTanggalTanam(tanggalMulaiTanam);
                lahan.setEstimasiPanen(tanggalEstimasiPanen);
                lahan.setKelembapanTanah(kelembapanTanah);
            }, () -> {
                // onSuccess
                runOnUiThread(() -> {
                    Toast.makeText(this, "Data Lahan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    toPengaturanLahan();
                });
            }, error -> {
                // onError
                runOnUiThread(() -> {
                    Toast.makeText(this, "Gagal menyimpan data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    public void toPengaturanLahan() {
    Intent intent = new Intent(this, PengaturanLahan.class);
    startActivity(intent);
}

private boolean validasiData() {
    if (jenisTanaman == null || tanggalMulaiTanam == null || tanggalEstimasiPanen == null || kelembapanTanah.isEmpty()) {
        Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show();
        return false;
    }
    return true;
}
//Hitung Berapa Hari Menuju Panen
public int hitungSisaHariPanen(String tanggalEstimasiPanen) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    //Ganti String ke Format Local Date
    LocalDate panenDate = LocalDate.parse(tanggalEstimasiPanen, formatter);
    LocalDate today = LocalDate.now();

    long daysBetween = ChronoUnit.DAYS.between(today, panenDate);
    //Apabila Melewati Batas Waktu Estimasi Panen Muncul ANgka 0)
    return (int) Math.max(daysBetween, 0);
}
    private boolean validasiKelembapan(String kelembapanTanah) {
        int nilaiKelembapan = Integer.parseInt(kelembapanTanah);
            if (nilaiKelembapan >= 1 && nilaiKelembapan <= 100) {
                return true; // Valid
            } else {
                Toast.makeText(this, "Nilai kelembapan harus antara 1 sampai 100%", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
}




