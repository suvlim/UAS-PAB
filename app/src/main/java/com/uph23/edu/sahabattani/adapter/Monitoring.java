package com.uph23.edu.sahabattani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.Lahan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Monitoring extends BaseAdapter {

    private Context context;
    private List<Lahan> lahanList;
    private LayoutInflater inflater;

    public Monitoring(Context context, List<Lahan> lahanList) {
        this.context = context;
        this.lahanList = lahanList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lahanList.size();
    }

    @Override
    public Object getItem(int position) {
        return lahanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lahanList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.monitoring, parent, false);
            holder = new ViewHolder();

            holder.tvNama = convertView.findViewById(R.id.txvNamaLahan);
            holder.tvLokasi = convertView.findViewById(R.id.tvLokasi);
            holder.tvKelembapan = convertView.findViewById(R.id.txvOptimalKelembapan);
            holder.progressBar = convertView.findViewById(R.id.persentaseKelembapan);
            holder.tvDitanam = convertView.findViewById(R.id.txvTanggaTanam);
            holder.tvPanen = convertView.findViewById(R.id.txvEstimasiPanen);
            holder.tvStatusAir = convertView.findViewById(R.id.txvStatusAir);
            holder.imgAir = convertView.findViewById(R.id.imgAir);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

        return convertView;
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

    static class ViewHolder {
        TextView tvNama, tvLokasi, tvKelembapan, tvDitanam, tvPanen, tvStatusAir;
        ProgressBar progressBar;
        ImageView imgAir;
    }

}
