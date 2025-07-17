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

            holder.tvNama = convertView.findViewById(R.id.tvNamaLahan);
            holder.tvLokasi = convertView.findViewById(R.id.tvLokasi);
            holder.tvKelembapan = convertView.findViewById(R.id.tvKelembapan);
            holder.progressBar = convertView.findViewById(R.id.progressKelembapan);
            holder.tvDitanam = convertView.findViewById(R.id.tvDitanam);
            holder.tvPanen = convertView.findViewById(R.id.tvEstimasiPanen);
            holder.tvStatusAir = convertView.findViewById(R.id.tvStatusAir);
            holder.imgAir = convertView.findViewById(R.id.imgAir);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lahan lahan = lahanList.get(position);

        holder.tvNama.setText(lahan.getNamaLahan());
        holder.tvLokasi.setText(lahan.getLokasiLahan());
        holder.tvKelembapan.setText(lahan.getKelembapanTanah() + "%");

        int kelembapan = 0;
        try {
            kelembapan = Integer.parseInt(lahan.getKelembapanTanah());
        } catch (NumberFormatException e) {
            kelembapan = 0;
        }

        holder.progressBar.setProgress(kelembapan);

        holder.tvDitanam.setText("Ditanam: " + lahan.getTanggalTanam());
        holder.tvPanen.setText("Estimasi Panen: " + lahan.getEstimasiPanen());

        // Contoh status air
        holder.tvStatusAir.setText(kelembapan >= 50 ? "Optimal" : "Kurang");
        holder.imgAir.setImageResource(R.drawable.water);

        return convertView;
    }

    static class ViewHolder {
        TextView tvNama, tvLokasi, tvKelembapan, tvDitanam, tvPanen, tvStatusAir;
        ProgressBar progressBar;
        ImageView imgAir;
    }
}

