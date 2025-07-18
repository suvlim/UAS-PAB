package com.uph23.edu.sahabattani;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class laporanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        // Deklarasi lokal
        LineChart chartHumidity = findViewById(R.id.chart_humidity);

        // Konfigurasi chart
        if (chartHumidity == null) {
            Log.e("laporanActivity", "LineChart is null! Check your activity_laporan.xml and ID. Time: 00:27 WIB, 19 Jul 2025");
        } else {
            Log.d("laporanActivity", "LineChart found successfully. Time: 00:27 WIB, 19 Jul 2025");
            chartHumidity.getDescription().setEnabled(false); // Hilangkan deskripsi
            chartHumidity.setTouchEnabled(true); // Aktifkan interaksi
            chartHumidity.setPinchZoom(true); // Aktifkan pinch zoom

            try {
                updateChart(chartHumidity, "Lahan A");
            } catch (Exception e) {
                Log.e("laporanActivity", "Error updating chart: " + e.getMessage() + ". Time: 00:27 WIB, 19 Jul 2025");
            }
        }
    }

    // Metode updateChart dengan parameter chart
    private void updateChart(LineChart chart, String lahan) {
        if (chart == null) {
            Log.e("laporanActivity", "Chart is null, cannot update. Time: 00:27 WIB, 19 Jul 2025");
            return;
        }

        ArrayList<Entry> entries = new ArrayList<>();
        String dataLabel;

        if ("Lahan A".equals(lahan)) {
            // Menambahkan lebih banyak data dummy untuk Lahan A
            entries.add(new Entry(0, 65));  // Hari 1: 65%
            entries.add(new Entry(1, 70));  // Hari 2: 70%
            entries.add(new Entry(2, 62));  // Hari 3: 62%
            entries.add(new Entry(3, 68));  // Hari 4: 68%
            entries.add(new Entry(4, 55));  // Hari 5: 55%
            dataLabel = "Kelembapan Lahan A (%)";
        } else if ("Lahan B".equals(lahan)) {
            // Menambahkan lebih banyak data dummy untuk Lahan B
            entries.add(new Entry(0, 55));  // Hari 1: 55%
            entries.add(new Entry(1, 60));  // Hari 2: 60%
            entries.add(new Entry(2, 58));  // Hari 3: 58%
            entries.add(new Entry(3, 63));  // Hari 4: 63%
            entries.add(new Entry(4, 50));  // Hari 5: 50%
            dataLabel = "Kelembapan Lahan B (%)";
        } else {
            Log.w("laporanActivity", "Lahan tidak dikenal: " + lahan + ". Time: 00:27 WIB, 19 Jul 2025");
            return;
        }

        Log.d("laporanActivity", "Entries created with size: " + entries.size() + ". Time: 00:27 WIB, 19 Jul 2025");
        for (Entry entry : entries) {
            Log.d("laporanActivity", "Entry - X: " + entry.getX() + ", Y: " + entry.getY() + ". Time: 00:27 WIB, 19 Jul 2025");
        }

        LineData lineData = createLineData(entries, dataLabel, lahan);
        if (lineData != null && lineData.getDataSetCount() > 0) {
            Log.d("laporanActivity", "LineData created with " + lineData.getDataSetCount() + " datasets. Time: 00:27 WIB, 19 Jul 2025");
            try {
                chart.clear(); // Hapus data lama
                chart.setData(lineData);
                chart.invalidate(); // Pastikan chart disegarkan
                Log.d("laporanActivity", "Chart data set and invalidated. Time: 00:27 WIB, 19 Jul 2025");
            } catch (Exception e) {
                Log.e("laporanActivity", "Error setting chart data: " + e.getMessage() + ". Time: 00:27 WIB, 19 Jul 2025");
            }
        } else {
            Log.e("laporanActivity", "LineData is null or has no datasets! Time: 00:27 WIB, 19 Jul 2025");
        }
    }

    // Metode terpisah untuk membuat LineData
    private LineData createLineData(ArrayList<Entry> entries, String dataLabel, String lahan) {
        if (entries == null || entries.isEmpty()) {
            Log.e("laporanActivity", "Entries is null or empty! Time: 00:27 WIB, 19 Jul 2025");
            return null;
        }

        Log.d("laporanActivity", "Creating LineDataSet with label: " + dataLabel + ". Time: 00:27 WIB, 19 Jul 2025");
        try {
            LineDataSet dataSet = new LineDataSet(entries, dataLabel);
            dataSet.setColor("Lahan A".equals(lahan) ? Color.BLUE : Color.RED);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setDrawValues(true); // Tampilkan nilai
            dataSet.setLineWidth(2f);    // Lebar garis untuk visibilitas lebih baik
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Mode kurva untuk tampilan yang lebih halus

            Log.d("laporanActivity", "LineDataSet created with " + entries.size() + " entries. Time: 00:27 WIB, 19 Jul 2025");
            return new LineData(dataSet);
        } catch (Exception e) {
            Log.e("laporanActivity", "Error creating LineDataSet: " + e.getMessage() + ". Time: 00:27 WIB, 19 Jul 2025");
            return null;
        }
    }
}