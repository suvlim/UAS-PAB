package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uph23.edu.sahabattani.adapter.IrigasiAdapter;
import com.uph23.edu.sahabattani.model.Irigasi;
import com.uph23.edu.sahabattani.model.Lahan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class laporanActivity extends AppCompatActivity {
    private Realm realm;
    private Spinner spinnerLahanIrigasi; // Spinner pertama
    private Spinner spinnerLahanChart;    // Spinner kedua
    private ListView lsvIrigasi;
    private String selectedLahan;
    private IrigasiAdapter irigasiAdapter;
    private List<Irigasi> irigasiList;
    private RealmResults<Lahan> lahanList;
    private LineChart lineChart;
    private TextView emptyIrigasiView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        // Initialize Realm
        realm = Realm.getDefaultInstance();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true) // sementara aktifkan untuk demo
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // Initialize UI components
        spinnerLahanIrigasi = findViewById(R.id.spinnernamalahan);
        spinnerLahanChart = findViewById(R.id.spinnerlahan);
        lsvIrigasi = findViewById(R.id.lsvIrigasi);
        lineChart = findViewById(R.id.chart_humidity);
        emptyIrigasiView = findViewById(R.id.empty_irigasi_view);

        lsvIrigasi.setEmptyView(emptyIrigasiView);

        // Setup Bottom Navigation
        BottomNavigationView btmNav = findViewById(R.id.bottom_nav);
        btmNav.setSelectedItemId(R.id.navigation_statistik);
        btmNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_statistik) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_lahan) {
                startActivity(new Intent(getApplicationContext(), PengaturanLahan.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        lahanList = realm.where(Lahan.class).findAll();
        irigasiList = generateIrigasiList();
        loadSpinnerLahan();
        loadListViewIrigasi();
        loadChart();

        // Setup Spinner listeners
        spinnerLahanIrigasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLahan = parent.getItemAtPosition(position).toString();
                updateListView(selectedLahan);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLahan = null;
            }
        });
        spinnerLahanChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLahan = parent.getItemAtPosition(position).toString();
                updateChart(lineChart,selectedLahan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLahan = null;
            }
        });
    }

    private void loadSpinnerLahan() {
        lahanList = realm.where(Lahan.class).findAll();
        List<String> lahanNames = new ArrayList<>();

            if (lahanList.isEmpty()) {
                Toast.makeText(this, "Tidak ada lahan yang terdaftar", Toast.LENGTH_SHORT).show();
            } else {
                for (Lahan l : lahanList) {
                    lahanNames.add(l.getNamaLahan());
                }
            }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lahanNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLahanIrigasi.setAdapter(adapter);
        spinnerLahanChart.setAdapter(adapter);
        // Set initial selection for both spinners based on whether lahan exist
        if (lahanList.isEmpty()) {
            spinnerLahanIrigasi.setSelection(0); // Select "Tidak ada lahan"
            spinnerLahanChart.setSelection(0);   // Select "Tidak ada lahan"
        } else {
            spinnerLahanIrigasi.setSelection(0); // Select the first actual lahan
            spinnerLahanChart.setSelection(0);   // Select the first actual lahan
        }
    }

    private void loadListViewIrigasi() {
        irigasiAdapter = new IrigasiAdapter(this, new ArrayList<>(),realm);
        lsvIrigasi.setAdapter(irigasiAdapter);
        // Update with initial selection or all data
        if (selectedLahan != null) {
            updateListView(selectedLahan);
        } else {
            updateListView(null); // Show all Irigasi if no Lahan selected
        }
    }

    private void updateListView(String selectedLahanName) {
        ArrayList<Irigasi> filteredList = new ArrayList<>();
        if (selectedLahanName != null) {
            Lahan selectedLahan = realm.where(Lahan.class).equalTo("namaLahan", selectedLahanName).findFirst();
            if (selectedLahan != null) {
                for (Irigasi irigasi : irigasiList) {
                    if (irigasi.getLahanId() == selectedLahan.getId()) {
                        filteredList.add(irigasi);
                    }
                }
            }
        } else {
            filteredList.addAll(irigasiList); // Show all Irigasi if no Lahan selected
        }

        irigasiAdapter.clear();
        irigasiAdapter.addAll(filteredList);
        irigasiAdapter.notifyDataSetChanged();

        // Show a message if the list is empty
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data irigasi untuk lahan ini", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Irigasi> generateIrigasiList() {
        ArrayList<Irigasi> irigasiList = new ArrayList<>();
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM",new Locale("id", "ID"));
        // Generate exactly 2 Irigasi records per Lahan
        for (Lahan lahan : lahanList) {
            for (int i = 0; i < 2; i++) {
                Irigasi irigasi = new Irigasi();
                irigasi.setLahanId(lahan.getId());
                long currentTime = System.currentTimeMillis();
                long randomOffset = random.nextInt(7) * 24 * 60 * 60 * 1000L; // Up to 7 days ago
                irigasi.setTanggalIrigasi(sdf.format(new Date(currentTime - randomOffset)));
                irigasi.setDurasiIrigasi(random.nextInt(56) + 5); // 5–60 minutes
                irigasi.setVolumeIrigasi(random.nextInt(91) + 10); // 10–100 liters
                irigasiList.add(irigasi);
            }
        }
        return irigasiList;
    }
    private void loadChart() {
        if (lahanList.isEmpty() || selectedLahan == null || selectedLahan.equals("Tidak ada lahan")) {
            displayNoChartDataMessage(); // Panggil metode untuk menampilkan pesan kosong
            return; // Hentikan eksekusi metode loadChart
        }
        // Data dummy untuk kelembapan tanah
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Random random = new Random();

        // Siapkan data dummy untuk 7 hari terakhir
        List<String> labels = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        int lahanId = 1; // Default lahanId, akan disesuaikan berdasarkan spinner

        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            String tanggal = sdf.format(cal.getTime());
            labels.add(tanggal);
            // Tambahkan variasi kecil berdasarkan lahanId untuk membedakan data
            int baseKelembapan = random.nextInt(71) + 30; // 30-100%
            int variasi = (lahanId % 3) * 5; // Variasi kecil berdasarkan lahanId
            int kelembapan = Math.min(100, baseKelembapan + variasi); // Pastikan tidak melebihi 100%
            entries.add(new Entry(i, kelembapan)); // i sebagai indeks x
        }

        // Konfigurasi LineChart
        LineDataSet dataSet = new LineDataSet(entries, "Kelembapan Tanah (%)");
        dataSet.setFillAlpha(110);
        dataSet.setColor(Color.BLUE); // Warna garis
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(false); // Opsional: isi area di bawah garis

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Kustomisasi chart
        lineChart.getDescription().setEnabled(false); // Nonaktifkan deskripsi
        lineChart.getLegend().setEnabled(true); // Aktifkan legenda
        lineChart.getAxisLeft().setAxisMaximum(100f); // Maksimum 100%
        lineChart.getAxisLeft().setAxisMinimum(0f); // Minimum 0%
        lineChart.getAxisRight().setEnabled(false); // Nonaktifkan sumbu kanan
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Set label tanggal
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7); // Tampilkan semua 7 hari

        // Tambahkan judul
        lineChart.setExtraOffsets(0f, 0f, 0f, 10f); // Tambah ruang untuk judul
        lineChart.getLegend().setTextSize(12f);
        lineChart.getLegend().setForm(Legend.LegendForm.LINE);

        // Refresh chart
        lineChart.invalidate(); // Perbarui tampilan chart
    }

    private void updateChart(LineChart chart, String lahan) {
        if (chart == null) {
            Log.e("laporanActivity", "Chart is null, cannot update.");
            return;
        }
        if (lahanList.isEmpty() || selectedLahan == null || selectedLahan.equals("Tidak ada lahan")) {
            displayNoChartDataMessage(); // Panggil metode untuk menampilkan pesan kosong
            return; // Hentikan eksekusi metode loadChart
        }


        ArrayList<Entry> entries = new ArrayList<>();
        String dataLabel;

        // Generate random humidity data for the chart
        Random random = new Random();
        if (lahanList.where().equalTo("namaLahan", lahan).findFirst() != null) {
            for (int i = 0; i < 5; i++) {
                entries.add(new Entry(i, random.nextInt(41) + 50)); // 50–90% humidity
            }
            dataLabel = "Kelembapan " + lahan + " (%)";
        } else {
            Log.w("laporanActivity", "Lahan tidak dikenal: " + lahan);
            return;
        }

        LineData lineData = createLineData(entries, dataLabel, lahan);
        if (lineData != null && lineData.getDataSetCount() > 0) {
            try {
                chart.clear();
                chart.setData(lineData);
                chart.invalidate();
                Log.d("laporanActivity", "Chart data set and invalidated.");
            } catch (Exception e) {
                Log.e("laporanActivity", "Error setting chart data: " + e.getMessage());
            }
        } else {
            Log.e("laporanActivity", "LineData is null or has no datasets!");
        }
    }

    private LineData createLineData(ArrayList<Entry> entries, String dataLabel, String lahan) {
        if (entries == null || entries.isEmpty()) {
            Log.e("laporanActivity", "Entries is null or empty!");
            return null;
        }

        try {
            LineDataSet dataSet = new LineDataSet(entries, dataLabel);
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setDrawValues(true);
            dataSet.setLineWidth(2f);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            Log.d("laporanActivity", "LineDataSet created with " + entries.size() + " entries.");
            return new LineData(dataSet);
        } catch (Exception e) {
            Log.e("laporanActivity", "Error creating LineDataSet: " + e.getMessage());
            return null;
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
    private void displayNoChartDataMessage(){
        lineChart.clear();
        lineChart.invalidate();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}