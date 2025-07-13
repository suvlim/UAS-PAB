package com.uph23.edu.sahabattani.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.HistoryTanam;

import java.util.ArrayList;

public class HistoryTanamAdapter extends ArrayAdapter<HistoryTanam> {

    private Context context;
    private ArrayList<HistoryTanam> historyList;

    public HistoryTanamAdapter(@NonNull Context context, @NonNull ArrayList<HistoryTanam> historyList) {
        super(context, R.layout.riwayat_tanam, historyList);
        this.context = context;
        this.historyList = historyList;
    }

    private static class ViewHolder {
        TextView txvTanggalTanam;
        TextView txvTanggalPanen;
        TextView txvJenisTanaman;
        ImageView imvGambarTanaman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HistoryTanam history = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.riwayat_tanam, parent, false);

            holder.txvTanggalTanam = convertView.findViewById(R.id.txvTanggalTanam);
            holder.txvTanggalPanen = convertView.findViewById(R.id.txvTanggalPanen);
            holder.txvJenisTanaman = convertView.findViewById(R.id.txvJenisTanaman);
            holder.imvGambarTanaman = convertView.findViewById(R.id.imvGambarTanaman);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (history != null) {
            holder.txvTanggalTanam.setText("Tanggal Tanam : " +
                    (history.getTanggalTanam() != null ? history.getTanggalTanam() : "-"));
            holder.txvTanggalPanen.setText("Tanggal Panen : " + history.getTanggalPanen());
            holder.txvJenisTanaman.setText(history.getJenisTanaman());

            // Ganti gambar berdasarkan jenis tanaman
            String jenis = history.getJenisTanaman() != null ? history.getJenisTanaman() : "";

            if (jenis != null) {
                if (jenis.equalsIgnoreCase("Padi")) {
                    holder.imvGambarTanaman.setImageResource(R.drawable.rice);
                } else if (jenis.equalsIgnoreCase("Jagung")) {
                    holder.imvGambarTanaman.setImageResource(R.drawable.corn);
                } else {
                    holder.imvGambarTanaman.setImageDrawable(null); // tidak tampilkan gambar
                }
            } else {
                holder.imvGambarTanaman.setImageDrawable(null); // tidak tampilkan gambar
            }

        }

        return convertView;
    }
}