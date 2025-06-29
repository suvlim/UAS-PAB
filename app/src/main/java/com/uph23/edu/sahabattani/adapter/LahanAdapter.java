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

import java.util.ArrayList;

public class LahanAdapter extends ArrayAdapter<Lahan> {
    private ArrayList<Lahan> lahanArrayList;
    Context context;

    public LahanAdapter(ArrayList<Lahan> lahanArrayList, Context context) {
        super(context, R.layout.item_lahan, lahanArrayList);
        this.lahanArrayList = lahanArrayList;
        this.context = context;
    }
    private static class MyViewHolder{
        static TextView txvNamaLahan;
        static TextView txvLokasiLahan;
        static TextView txvKelembapan;
        static TextView txvPanen;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Lahan lahan = getItem(position);
        final View results;
        MyViewHolder myViewHolder;

        if (convertView==null) {
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lahan, parent, false);

            MyViewHolder.txvNamaLahan = (TextView) convertView.findViewById(R.id.txvNamaLahan);
            MyViewHolder.txvLokasiLahan = (TextView) convertView.findViewById(R.id.txvLokasiLahan);
            MyViewHolder.txvKelembapan = (TextView) convertView.findViewById(R.id.txvKelembapan);
            MyViewHolder.txvPanen = (TextView) convertView.findViewById(R.id.txvPanen);
            convertView.setTag(myViewHolder);
        }
        else{
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        results = convertView;
        myViewHolder.txvNamaLahan.setText("Lahan Sawah " + lahan.getNamaLahan());
        myViewHolder.txvLokasiLahan .setText(lahan.getLokasiLahan());
        myViewHolder.txvKelembapan.setText(lahan.getKelembapan() + " ml");
        myViewHolder.txvPanen.setText("Estimasi Panen : " + lahan.getPanen() + "hari");
        return results;
    }
}
