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
import com.uph23.edu.sahabattani.model.Irigasi;
import com.uph23.edu.sahabattani.model.Lahan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;


public class IrigasiAdapter extends ArrayAdapter<Irigasi> {
    private Realm realm;

    public IrigasiAdapter(@NonNull Context context, List<Irigasi> irigasiList, Realm realm) {
        super(context, 0, irigasiList);
        this.realm = realm;
    }
    private static class ViewHolder {
        TextView txvNamaLahan;
        TextView txvTanggalIrigasi;
        TextView txvDurasiIrigasi;
        TextView txvJumlahIrigasi;
        ImageView imgTanaman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Irigasi irigasi = getItem(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_statistik,parent,false);
            holder = new ViewHolder();

            holder.txvNamaLahan = convertView.findViewById(R.id.txvnamaLahan);
            holder.txvTanggalIrigasi = convertView.findViewById(R.id.txvtanggalPenyiraman);
            holder.txvDurasiIrigasi = convertView.findViewById(R.id.txvlamaPenyiraman);
            holder.txvJumlahIrigasi = convertView.findViewById(R.id.txvjumlahPenyiraman);
            holder.imgTanaman = convertView.findViewById(R.id.imgTanaman);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(irigasi != null){
            String namaLahan = "";
            String jenisTanaman = null;
            Lahan lahan = realm.where(Lahan.class).equalTo("id",irigasi.getLahanId()).findFirst();
            if (lahan != null) {
                namaLahan = lahan.getNamaLahan();
                jenisTanaman = lahan.getJenisTanaman();
            }
            holder.txvNamaLahan.setText(namaLahan + "-" + jenisTanaman);

            String tanggalIrigasi = irigasi.getTanggalIrigasi();
            if(tanggalIrigasi!=null){
                holder.txvTanggalIrigasi.setText(tanggalIrigasi);
            }

            holder.txvDurasiIrigasi.setText(irigasi.getDurasiIrigasi() + "menit");

            holder.txvJumlahIrigasi.setText(irigasi.getVolumeIrigasi()+ "liter");

            // Atur gambar tanaman berdasarkan jenis tanaman dari Lahan
            if (jenisTanaman != null) {
                if (jenisTanaman.equalsIgnoreCase("Padi")) {
                    holder.imgTanaman.setImageResource(R.drawable.rice_dashboard);
                }
                else{
                    holder.imgTanaman.setImageResource(R.drawable.corn_yellow);
                }
            }
        }
        return convertView;
    }
}
