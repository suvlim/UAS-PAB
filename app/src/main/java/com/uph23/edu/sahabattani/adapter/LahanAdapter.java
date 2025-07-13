package com.uph23.edu.sahabattani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.Lahan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class LahanAdapter extends ArrayAdapter<Lahan> {

    private Context context;
    private ArrayList<Lahan> lahanList;

    public LahanAdapter(@NonNull Context context, @NonNull ArrayList<Lahan> lahanList) {
        super(context, R.layout.item_lahan, lahanList);
        this.context = context;
        this.lahanList = lahanList;
    }

    private static class ViewHolder {
        TextView txvNamaLahan;
        TextView txvLokasiLahan;
        TextView txvKelembapan;
        TextView txvPanen;
        TextView txvJenisTanaman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Lahan lahan = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_lahan, parent, false);

            holder.txvNamaLahan = convertView.findViewById(R.id.txvNamaLahan);
            holder.txvLokasiLahan = convertView.findViewById(R.id.txvLokasiLahan);
            holder.txvKelembapan = convertView.findViewById(R.id.txvKelembapan);
            holder.txvPanen = convertView.findViewById(R.id.txvPanen);
            holder.txvJenisTanaman = convertView.findViewById(R.id.txvJenisTanaman);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (lahan != null) {
            holder.txvNamaLahan.setText("Lahan Sawah " + lahan.getNamaLahan());
            holder.txvLokasiLahan.setText(lahan.getLokasiLahan());
            holder.txvKelembapan.setText(
                    lahan.getKelembapanTanah() != null ? lahan.getKelembapanTanah() + " mL" : "-"
            );

            int sisaHari = hitungSisaHariPanen(lahan.getEstimasiPanen());
            holder.txvPanen.setText("Estimasi Panen : " + sisaHari + " hari");
            holder.txvJenisTanaman.setText(lahan.getJenisTanaman());


        }

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
}
