package com.uph23.edu.sahabattani.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uph23.edu.sahabattani.AturSensor;
import com.uph23.edu.sahabattani.R;
import com.uph23.edu.sahabattani.model.Lahan;
import com.uph23.edu.sahabattani.model.Sensor;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SensorAdapter extends ArrayAdapter<Object> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SENSOR = 1;
    private Context context;
    private List<Object> datalist; // local storage

    Realm realm;
    public SensorAdapter(@NonNull Context context, List<Object> items) {
        super(context, 0, items);
        this.context = context;
        this.datalist = new ArrayList<>(items);
    }
    @Override
    public int getViewTypeCount() {
        return 2; // Header dan Sensor
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof Lahan) return TYPE_HEADER;
        return TYPE_SENSOR;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int viewType = getItemViewType(position);
        View currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (viewType == TYPE_HEADER) {
                currentItemView = inflater.inflate(R.layout.item_header_sensor, parent, false);
            } else {
                currentItemView = inflater.inflate(R.layout.item_sensor, parent, false);
            }
        }

        Object item = getItem(position);

        if (viewType == TYPE_HEADER) {
            Lahan lahan = (Lahan) item;
            TextView txvnamaLahan = currentItemView.findViewById(R.id.txvNamaLahan); // Sesuaikan ID
            txvnamaLahan.setText("Lahan Sawah " + lahan.getNamaLahan());
            TextView txvlokasiLahan = currentItemView.findViewById(R.id.txvLokasiLahan);
            txvlokasiLahan.setText(lahan.getLokasiLahan());



        } else {
            Sensor sensor = (Sensor) item;
            TextView txvNamaSensor = currentItemView.findViewById(R.id.txvnamaSensor);
            TextView txvLongitude = currentItemView.findViewById(R.id.txvLongitude);
            TextView txvLatitude = currentItemView.findViewById(R.id.txvLatitude);
            TextView txvKelembapan = currentItemView.findViewById(R.id.txvKelembapan);
            ImageView imgSensor = currentItemView.findViewById(R.id.imgSensor);
            ImageView imgDelete = currentItemView.findViewById(R.id.imgDelete);

            txvNamaSensor.setText(sensor.getNamaSensor());
            txvLongitude.setText(": " + sensor.getLongitude());
            txvLatitude.setText(": " + sensor.getLatitude());
            txvKelembapan.setText(sensor.getKelembapan() + "%");
            imgDelete.setOnClickListener(v -> deleteSensor(sensor.getSensorID()));

        }
        // then return the recyclable view
        return currentItemView;
    }

    public void deleteSensor(final int sensorID){
        Realm realm = Realm.getDefaultInstance();
        try {
            Sensor sensor = realm.where(Sensor.class).equalTo("sensorID", sensorID).findFirst();
            if (sensor != null) {
                // Tampilkan AlertDialog untuk konfirmasi
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Apakah Anda yakin ingin menghapus sensor ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Panggil metode deleteSensor saat pengguna setuju
                                realm.executeTransaction(realm1 -> {
                                    sensor.deleteFromRealm();
                                    // Perbarui data di adapter (opsional, tergantung implementasi)
                                    updatedataList();
                                });
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Tutup dialog jika pengguna membatalkan
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true) // Memungkinkan pengguna membatalkan dengan back button
                        .show();
            };
        } finally {
            if (realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }
    private void updatedataList() {
        if (realm != null && !realm.isClosed()) {
            final ArrayList<Object> newDataList = new ArrayList<>();
            RealmResults<Lahan> lahanResults = realm.where(Lahan.class).findAll();
            for (Lahan lahan : lahanResults) {
                newDataList.add(lahan);
                RealmResults<Sensor> sensorResults = realm.where(Sensor.class).equalTo("lahan.id", lahan.getId()).findAll();
                newDataList.addAll(realm.copyFromRealm(sensorResults));
            }
            datalist.clear();
            datalist.addAll(newDataList);
            clear();
            addAll(datalist);
            notifyDataSetChanged();
        }
    }

}

