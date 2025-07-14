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
import com.uph23.edu.sahabattani.model.Sensor;

import java.util.ArrayList;

import io.realm.Realm;

public class SensorAdapter extends ArrayAdapter<Sensor> {
    public SensorAdapter(@NonNull Context context, ArrayList<Sensor> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_sensor, parent,
                    false);
        }
        // get the position of the view from the ArrayAdapter
        Sensor currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView numbersImage = currentItemView.findViewById(R.id.imgSensor);
        assert currentNumberPosition != null;

        TextView textView1 = currentItemView.findViewById(R.id.txvnamaSensor);
        textView1.setText(currentNumberPosition.getNamaSensor());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.txvLongitude);
        textView2.setText("Longitude : " + currentNumberPosition.getLongitude());

        TextView textView3 = currentItemView.findViewById(R.id.txvLatitude);
        textView3.setText("Latitude : " + currentNumberPosition.getLatitude());

        TextView textView4 = currentItemView.findViewById(R.id.txvKelembapan);
        textView4.setText("Kelembapan Air: " + currentNumberPosition.getKelembapan()+ "%");

        ImageView imgdelete = currentItemView.findViewById(R.id.imgDelete);
        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSensor(currentNumberPosition.getSensorID());
            }
        });

        // then return the recyclable view
        return currentItemView;
    }

    public void deleteSensor(final int sensorID){
        Realm realm = Realm.getDefaultInstance();
        Sensor sensor =  realm.where(Sensor.class).equalTo("sensorID", sensorID).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sensor.deleteFromRealm();
                remove(sensor);
                notifyDataSetChanged();
            }
   });
}
}
