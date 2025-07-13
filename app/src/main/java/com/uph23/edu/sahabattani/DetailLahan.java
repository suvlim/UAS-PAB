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

public class DetailLahan extends AppCompatActivity {
    TextView txvDetailNamaLahan, txvDetailLokasiLahan, txvJenisTanaman, txvTanggalTanam, txvEstimasiPanen;
    Button btnEdit, btnPanen;
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

        realm = Realm.getDefaultInstance();

        // Ambil ID lahan dari intent
        lahanId = getIntent().getIntExtra("lahanId", -1);
        lahan = realm.where(Lahan.class).equalTo("id", lahanId).findFirst();


        txvDetailNamaLahan = findViewById(R.id.txvDetailNamaLahan);
        txvDetailLokasiLahan = findViewById(R.id.txvDetailLokasiLahan);
        txvJenisTanaman = findViewById(R.id.txvJenisTanaman);
        txvTanggalTanam = findViewById(R.id.txvTanggalTanam);
        txvEstimasiPanen = findViewById(R.id.txvEstimasiPanen);
        btnEdit = findViewById(R.id.btnEdit);
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

        //Masuk Ke Halaman Edit Lahan
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditLahan.class);
            intent.putExtra("lahanId", lahanId);
            startActivity(intent);
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
            //Validasi Apakah Ada Data
            if (lahan.getJenisTanaman() == null || lahan.getTanggalTanam() == null || lahan.getEstimasiPanen() == null) {
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

            });
            //Refresh Riwayat Tanam dan Data
            tampilkanDataLahan();
            loadRiwayatTanam();
        });
        //Isi Riwayat Tanam Pertama Kali
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
