package edu.uph.m23si3.sahabattani.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.uph.m23si3.sahabattani.R;
import edu.uph.m23si3.sahabattani.model.Lahan;

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

        return results;
    }
}
