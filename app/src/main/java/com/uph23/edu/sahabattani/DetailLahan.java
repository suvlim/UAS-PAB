package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uph23.edu.sahabattani.adapter.HistoryTanamAdapter;
import com.uph23.edu.sahabattani.model.HistoryTanam;
import com.uph23.edu.sahabattani.model.Lahan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DetailLahan extends AppCompatActivity {
    TextView txvDetailNamaLahan, txvDetailLokasiLahan, txvJenisTanaman, txvTanggalTanam, txvEstimasiPanen;
    Button btnTanam, btnPanen;
    ImageView imvDeleteLahan, imvtutup;
    ListView lsvHistoryTanam;

    Realm realm;
    int lahanId;
    Lahan lahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_lahan);

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

        realm = Realm.getDefaultInstance();

        // Ambil ID lahan dari intent
        lahanId = getIntent().getIntExtra("lahanId", -1);
        lahan = realm.where(Lahan.class).equalTo("id", lahanId).findFirst();



        txvDetailNamaLahan = findViewById(R.id.txvDetailNamaLahan);
        txvDetailLokasiLahan = findViewById(R.id.txvDetailLokasiLahan);
        txvJenisTanaman = findViewById(R.id.txvJenisTanaman);
        txvTanggalTanam = findViewById(R.id.txvTanggalTanam);
        txvEstimasiPanen = findViewById(R.id.txvEstimasiPanen);
        btnTanam = findViewById(R.id.btnTanam);
        btnPanen = findViewById(R.id.btnPanen);
        imvDeleteLahan = findViewById(R.id.imvDeleteLahan);
        imvtutup = findViewById(R.id.imvtutup);
        lsvHistoryTanam = findViewById(R.id.lsvHistoryTanam);

        // Tampilkan data Lahan dari Lahan
        if (lahan != null) {
            txvJenisTanaman.setText("Jenis Tanaman: " + (lahan.getJenisTanaman() != null ? lahan.getJenisTanaman() : "-"));
            txvTanggalTanam.setText("Tanggal Tanam: " + (lahan.getTanggalTanam() != null ? lahan.getTanggalTanam() : "-"));
            txvEstimasiPanen.setText("Estimasi Panen: " + (lahan.getEstimasiPanen() != null ? lahan.getEstimasiPanen() : "-"));

            txvDetailNamaLahan.setText("Lahan Sawah " + lahan.getNamaLahan());
            txvDetailLokasiLahan.setText(lahan.getLokasiLahan());
        }

        //Masuk Ke Halaman Tambah Tanam Lahan Hanya Jika Sudah Panen
        btnTanam.setOnClickListener(v -> {
            if (lahan.getJenisTanaman() == null &&
                    lahan.getTanggalTanam() == null &&
                    lahan.getEstimasiPanen() == null &&
                    lahan.getKelembapanTanah() == null) {
                // Jika boleh tanam, pindah ke halaman EditLahan
                Intent intent = new Intent(this, EditLahan.class);
                intent.putExtra("IdLahan", lahanId);
                startActivity(intent);
            } else {
                // Jika belum panen, tampilkan peringatan
                Toast.makeText(this, "Lahan masih dalam masa tanam. Panen dulu sebelum menanam ulang.", Toast.LENGTH_SHORT).show();
            }
        });

        // Hapus lahan
        imvDeleteLahan.setOnClickListener(v -> {
            // Munculkan pesan konfirmasi
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Lahan")
                    .setMessage("Apakah Anda yakin ingin menghapus lahan ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        realm.executeTransactionAsync(r -> {
                            Lahan lahanToDelete = r.where(Lahan.class).equalTo("id", lahanId).findFirst();
                            if (lahanToDelete != null && lahanToDelete.isValid()) {
                                lahanToDelete.deleteFromRealm();
                            }
                        }, () -> {

                            runOnUiThread(() -> {
                                Toast.makeText(this, "Lahan dihapus!", Toast.LENGTH_SHORT).show();
                                finish(); // Kembali ke halaman sebelumnya
                            });
                        });
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
        imvtutup.setOnClickListener(v -> {
            toPengaturanLahan();
        });


        //Apabila Sawah Dipanen Klik Tombol Panen
        btnPanen.setOnClickListener(v -> {
            //Validasi Apakah Data Lengkap
            if (lahan.getJenisTanaman() == null ||
                    lahan.getTanggalTanam() == null ||
                    lahan.getEstimasiPanen() == null ||
                    lahan.getKelembapanTanah() == null ||
                    lahan.getKelembapanTanah().isEmpty()) {
                Toast.makeText(this, "Data penanaman belum lengkap. Tidak bisa panen.", Toast.LENGTH_SHORT).show();
                return;
            }
            //Simpan Tanggal Button di-Klik sebagai Tanggal Dipanen
            String tanggalPanen = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(new Date());
            //Simpan Riwayat Tanam
            realm.executeTransactionAsync(r -> {
                Lahan lahanInTransaction = r.where(Lahan.class).equalTo("id", lahanId).findFirst();
                if (lahanInTransaction != null && lahanInTransaction.isValid()) {
                    HistoryTanam history = r.createObject(HistoryTanam.class, (int) System.currentTimeMillis());
                    history.setIdLahan(lahanId);
                    history.setJenisTanaman(lahanInTransaction.getJenisTanaman());
                    history.setTanggalTanam(lahanInTransaction.getTanggalTanam());
                    history.setTanggalPanen(tanggalPanen);

                    lahanInTransaction.setJenisTanaman(null);
                    lahanInTransaction.setTanggalTanam(null);
                    lahanInTransaction.setEstimasiPanen(null);
                    lahanInTransaction.setKelembapanTanah(null);
                }
            }, () -> {
                // onSuccess
                runOnUiThread(() -> {
                    tampilkanDataLahan();
                    loadRiwayatTanam();
                    Toast.makeText(this, "Panen berhasil disimpan ke riwayat!", Toast.LENGTH_SHORT).show();
                });
            }, error -> {
                runOnUiThread(() ->
                        Toast.makeText(this, "Gagal menyimpan panen: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );
            });
        });
        //Pastikan Riwayat Tanam Muncul Langsung
        loadRiwayatTanam();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void loadRiwayatTanam() {
        RealmResults<HistoryTanam> results = realm.where(HistoryTanam.class)
                .equalTo("idLahan", lahanId)
                .sort("idLahan", Sort.DESCENDING)
                .findAll();

        ArrayList<HistoryTanam> list = new ArrayList<>(realm.copyFromRealm(results));
        HistoryTanamAdapter adapter = new HistoryTanamAdapter(this, list);
        lsvHistoryTanam.setAdapter(adapter);
    }
    public void toPengaturanLahan(){
        Intent intent = new Intent(this,PengaturanLahan.class);
        startActivity(intent);
    }
//Digunakan untuk mengupdate data setelah panen
    private void tampilkanDataLahan() {
        if (lahan != null) {
            txvJenisTanaman.setText("Jenis Tanaman: " + (lahan.getJenisTanaman() != null ? lahan.getJenisTanaman() : "-"));
            txvTanggalTanam.setText("Tanggal Tanam: " + (lahan.getTanggalTanam() != null ? lahan.getTanggalTanam() : "-"));
            txvEstimasiPanen.setText("Estimasi Panen: " + (lahan.getEstimasiPanen() != null ? lahan.getEstimasiPanen() : "-"));

            txvDetailNamaLahan.setText("Lahan Sawah " + lahan.getNamaLahan());
            txvDetailLokasiLahan.setText(lahan.getLokasiLahan());
        }
    }

}
