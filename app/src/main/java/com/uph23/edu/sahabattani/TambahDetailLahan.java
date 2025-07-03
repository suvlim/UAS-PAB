package com.uph23.edu.sahabattani;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class TambahDetailLahan extends AppCompatActivity {
    EditText edtAwalPenanaman;
    Button btn_simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_detail_lahan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtAwalPenanaman = findViewById(R.id.edt_awal_penanaman);
        edtAwalPenanaman.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TambahDetailLahan.this,
                    (picker, selectedYear, selectedMonth, selectedDay) -> {
                        selectedMonth += 1;
                        String tanggal = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                        edtAwalPenanaman.setText(tanggal);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPengaturanLahan();
            }
        });
    }
    public void toPengaturanLahan(){
        Intent intent = new Intent(this, PengaturanLahan.class);
        startActivity(intent);
    }
}
