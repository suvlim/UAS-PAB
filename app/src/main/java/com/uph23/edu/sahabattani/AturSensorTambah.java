package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uph23.edu.sahabattani.model.Sensor;
import com.uph23.edu.sahabattani.model.User;

import io.realm.Realm;

public class AturSensorTambah extends AppCompatActivity {

    EditText edtnamaSensor, longitudeInput, latitudeInput, msknnamaLahan;
    Button btnAddSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atur_sensor_tambah);
        //Pengaturan Bottom NavBar
        BottomNavigationView btmNav = findViewById(R.id.bottom_nav);
        //Set Halaman Lahan
        btmNav.setSelectedItemId(R.id.navigation_lahan);
        btmNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_lahan) {
                return true;
                //Pindah ke halaman Dashboard
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
                //Pindah ke halaman laporan statistik
            } else if (itemId == R.id.navigation_statistik) {
                startActivity(new Intent(getApplicationContext(), laporanActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtnamaSensor = findViewById(R.id.edtnamaSensor);
        longitudeInput = findViewById(R.id.longitudeInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        msknnamaLahan = findViewById(R.id.msknNamaLahan);
        btnAddSensor = findViewById(R.id.btnAddSensor);

        btnAddSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    simpanData();
                    toAturSensor();
                }
            }
        });
    }
    public void simpanData(){
        String namaSensor = edtnamaSensor.getText().toString().trim();
        int longitude = Integer.parseInt(longitudeInput.getText().toString().trim());
        int latitude = Integer.parseInt( latitudeInput.getText().toString().trim());
        String namaLahan = msknnamaLahan.getText().toString().trim();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            Number maxId = r.where(Sensor.class).max("sensorID");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            Sensor sensor = r.createObject(Sensor.class, nextId);
            sensor.setNamaSensor(namaSensor);
            sensor.setLongitude(longitude);
            sensor.setLatitude(latitude);
            sensor.setNamaLahan(namaLahan);
            sensor.setKelembapan(sensor.generaterandomHumidity(30,80));
        });
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
    }
    public boolean validateInput(){
        boolean isValid = true;
        String namaSensor = edtnamaSensor.getText().toString().trim();
        String longitudeText = longitudeInput.getText().toString().trim();
        String latitudeText = latitudeInput.getText().toString().trim();
        String namaLahan = msknnamaLahan.getText().toString().trim();

        if(namaSensor.isEmpty()){
            isValid = false;
            Toast.makeText(this, "Nama sensor tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        int longitude = 0;
        if (longitudeText.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Longitude tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                longitude = Integer.parseInt(longitudeText);
            } catch (NumberFormatException e) {
                isValid = false;
                Toast.makeText(this, "Longitude harus berupa angka ", Toast.LENGTH_SHORT).show();
            }
        }
        int latitude = 0;
        if(latitudeText.isEmpty()){
            isValid = false;
            Toast.makeText(this, "Latitude tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                latitude = Integer.parseInt(latitudeText);
            }
            catch (NumberFormatException e){
                isValid = false;
                Toast.makeText(this, "Latitude harus berupa angka", Toast.LENGTH_SHORT).show();
            }
        }
        if(namaLahan.isEmpty()){
            isValid = false;
            Toast.makeText(this, "Nama Lahan tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }
    public void toAturSensor(){
        Intent intent = new Intent(this, AturSensor.class);
        startActivity(intent);
    }
}
