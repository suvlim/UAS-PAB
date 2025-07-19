package com.uph23.edu.sahabattani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.Lahan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Monitoring extends RecyclerView.Adapter<Monitoring.ViewHolder> {

    private Context context;
    private List<Lahan> lahanList;
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
        holder.tvKelembapan.setText(lahan.getKelembapanTanah() + "%");

        int kelembapan = 0;
        try {
            kelembapan = Integer.parseInt(lahan.getKelembapanTanah());
        } catch (NumberFormatException e) {
            kelembapan = 0;
        }

        holder.progressBar.setProgress(kelembapan);

        holder.tvDitanam.setText("Tanggal Tanam: " + lahan.getTanggalTanam());

        int sisaHari = hitungSisaHariPanen(lahan.getEstimasiPanen());
        holder.tvPanen.setText("Estimasi Panen : " + sisaHari + " hari");

        // Contoh status air
        holder.tvStatusAir.setText(kelembapan >= 50 ? "Optimal" : "Kurang");
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
}
