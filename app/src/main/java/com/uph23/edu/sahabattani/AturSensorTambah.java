package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uph23.edu.sahabattani.model.Lahan;
import com.uph23.edu.sahabattani.model.Sensor;
import com.uph23.edu.sahabattani.model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AturSensorTambah extends AppCompatActivity {
    EditText edtnamaSensor, longitudeInput, latitudeInput;
    Button btnAddSensor;
    Spinner spinnerLahan;
    ImageView imvtutup;

    private String selectedLahan;
    private RealmResults<Lahan> lahanList;
    private List<String> lahanNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atur_sensor_tambah);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // gunakan versi yang lebih tinggi atau sama
                .deleteRealmIfMigrationNeeded() // aman untuk development, hapus data jika ada perubahan skema
                .build();

        Realm.setDefaultConfiguration(config);
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

        imvtutup = findViewById(R.id.imvtutup);
        imvtutup.setOnClickListener(v -> {
            toAturSensor();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtnamaSensor = findViewById(R.id.edtnamaSensor);
        longitudeInput = findViewById(R.id.longitudeInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        spinnerLahan = findViewById(R.id.spinner_lahan);
        btnAddSensor = findViewById(R.id.btnAddSensor);

        loadDataSpinner();
        spinnerLahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLahan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLahan = null;
            }
        });
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
    private void loadDataSpinner() {
        Realm realm = Realm.getDefaultInstance();
        lahanList = realm.where(Lahan.class).findAll();
        lahanNames = new ArrayList<>();
        realm.executeTransaction(r -> {
            if(lahanList.isEmpty()){
                Toast.makeText(this, "Tidak ada lahan yang terdaftar", Toast.LENGTH_SHORT).show();
            }
            else{
                for(Lahan l : lahanList){
                    lahanNames.add(l.getNamaLahan());
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lahanNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLahan.setAdapter(adapter);
    }
    public void simpanData(){

        List lahanlist = new ArrayList();
        String namaSensor = edtnamaSensor.getText().toString().trim();
        String longitude = longitudeInput.getText().toString();
        String latitude =  latitudeInput.getText().toString();
        int selectedPosition = spinnerLahan.getSelectedItemPosition();
        Lahan selectedLahan = lahanList.get(selectedPosition);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            Number maxId = r.where(Sensor.class).max("sensorID");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            Sensor sensor = r.createObject(Sensor.class, nextId);
            sensor.setNamaSensor(namaSensor);
            sensor.setLongitude(longitude);
            sensor.setLatitude(latitude);
            sensor.setKelembapan(sensor.generaterandomHumidity(30,80));
            sensor.setLahan(selectedLahan);
        });
        Toast.makeText(this, "Sensor berhasil ditambahkan !", Toast.LENGTH_SHORT).show();
    }
    public boolean validateInput(){
        boolean isValid = true;
        String namaSensor = edtnamaSensor.getText().toString().trim();
        String longitudeText = longitudeInput.getText().toString().trim();
        String latitudeText = latitudeInput.getText().toString().trim();

        if (selectedLahan == null || selectedLahan.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Nama Lahan tidak boleh kosong. Harap pilih lahan.", Toast.LENGTH_SHORT).show();
            return isValid;
        }

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
        return isValid;
    }
    public void toAturSensor(){
        Intent intent = new Intent(this, AturSensor.class);
        startActivity(intent);
    }


}
