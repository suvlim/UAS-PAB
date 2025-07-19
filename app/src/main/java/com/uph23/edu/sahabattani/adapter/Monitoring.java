package com.uph23.edu.sahabattani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.Lahan;
import com.uph23.edu.sahabattani.model.Sensor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import io.realm.Realm;

public class Monitoring extends RecyclerView.Adapter<Monitoring.ViewHolder> {

    private Context context;
    private List<Lahan> lahanList;
    private List<Sensor> sensorList;

    private LayoutInflater inflater;

    public Monitoring(Context context, List<Lahan> lahanList) {
        this.context = context;
        this.lahanList = lahanList;
        this.inflater = LayoutInflater.from(context);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvLokasi, tvKelembapan, tvDitanam, tvPanen, tvStatusAir;
        ProgressBar progressBar;
        ImageView imgAir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.txvNamaLahan);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            tvKelembapan = itemView.findViewById(R.id.txvOptimalKelembapan);
            progressBar = itemView.findViewById(R.id.persentaseKelembapan);
            tvDitanam = itemView.findViewById(R.id.txvTanggaTanam);
            tvPanen = itemView.findViewById(R.id.txvEstimasiPanen);
            tvStatusAir = itemView.findViewById(R.id.txvStatusAir);
            imgAir = itemView.findViewById(R.id.imgAir);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.monitoring, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lahan lahan = lahanList.get(position);

        holder.tvNama.setText("Lahan Sawah " + lahan.getNamaLahan());
        holder.tvLokasi.setText(lahan.getLokasiLahan());

        double optimal = 0;
        //Ambil rata rata data kelembapan tanah pada semua sensor di lahan tertentu
        double rataSensor = getRataRataSensorKelembapan(lahan.getId());
        //Ambil data kelembapan tanah dari lahan (Kelembapan tanah optimal)
        try {
            String kelembapanStr = lahan.getKelembapanTanah();
            //Pastikan kelembapan ada isi
            if (kelembapanStr != null && !kelembapanStr.trim().isEmpty()) {
                optimal = Double.parseDouble(kelembapanStr.trim());
            } else {
                optimal = 0;
            }
        } catch (NumberFormatException e) {
            optimal = 0;
        }

        int progress = 0;
        if (optimal > 0) {
            progress = (int) ((rataSensor / optimal) * 100);
            progress = Math.min(progress, 100);
        } else {
            progress = 0;
        }

        holder.progressBar.setProgress(Math.min(progress, 100));
        holder.tvKelembapan.setText(progress + " %");

        holder.tvDitanam.setText(lahan.getTanggalTanam() != null ? "Tanggal Tanam : " + lahan.getTanggalTanam(): "Tanggal Tanam : " + "-");

        int sisaHari = hitungSisaHariPanen(lahan.getEstimasiPanen());
        holder.tvPanen.setText("Estimasi Panen : " + sisaHari + " hari");

        // Contoh status air
        holder.tvStatusAir.setText(rataSensor >= optimal ? "Optimal" : "Kurang");
        holder.imgAir.setImageResource(R.drawable.water);
    }
    @Override
    public int getItemCount() {
        return lahanList != null ? lahanList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return lahanList.get(position).getId();
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
    // Hitung rata-rata kelembapan tanah yang didaptkan dari semua sensor di satu lahan
    private double getRataRataSensorKelembapan(long lahanId) {
        Realm realm = Realm.getDefaultInstance();
        double rata2 = 0.0;
        // Cari semua sensor dengan satu lahan id
        try {
            List<Sensor> sensors = realm.where(Sensor.class)
                    .equalTo("lahan.id", lahanId)
                    .findAll();

            if (!sensors.isEmpty()) {
                double total = 0;
                for (Sensor sensor : sensors) {
                    total += sensor.getKelembapan();
                }
                //Bagikan total dengan jumlah sensor
                rata2 = total / sensors.size();
            }
        } finally {
            realm.close();
        }
        return rata2;
    }

}
