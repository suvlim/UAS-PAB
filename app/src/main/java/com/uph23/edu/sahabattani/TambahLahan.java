package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;




import java.util.Locale;

public class TambahLahan extends AppCompatActivity {


    EditText edtLokasiLahan, edtNamaLahan;
    ImageView imvtutup;
    Button btnTambahLahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lahan);
        imvtutup = findViewById(R.id.imvtutup);
        edtLokasiLahan = findViewById(R.id.edtLokasiLahan);
        edtNamaLahan = findViewById(R.id.edtNamaLahan);
        btnTambahLahan = findViewById(R.id.btnTambahLahan);

        imvtutup.setOnClickListener(v -> {
            toPengaturanLahan();
        });
        btnTambahLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTambahDetailLahan();
            }
        });



    }

    public void toPengaturanLahan(){
        Intent intent = new Intent(this,PengaturanLahan.class);
        startActivity(intent);
    }
    public void toTambahDetailLahan(){
        Intent intent = new Intent(this,TambahDetailLahan.class);
        startActivity(intent);
    }

}

