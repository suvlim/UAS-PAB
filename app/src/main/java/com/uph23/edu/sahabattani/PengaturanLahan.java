package com.uph23.edu.sahabattani;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uph23.edu.sahabattani.adapter.LahanAdapter;
import com.uph23.edu.sahabattani.model.Lahan;

import java.util.ArrayList;

import com.uph23.edu.sahabattani.R;

public class PengaturanLahan extends AppCompatActivity {
    ListView listview;
    FloatingActionButton fabTambahLahan;
    ArrayList<Lahan> lahanArrayList;
    private static ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pengaturan_lahan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        listview = (ListView) findViewById(R.id.lsvLahan);
        fabTambahLahan =findViewById(R.id.fabTambahLahan);
        lahanArrayList = new ArrayList<>();
        fabTambahLahan.setOnClickListener(v -> {
            toTambahLahan();
        });


//        Lahan lahan1 = new Lahan("A", "Desa X, Medan", 500, 30);
//        Lahan lahan2 = new Lahan("B", "Desa Y, Medan", 800, 150);
//        Lahan lahan3 = new Lahan("C", "Desa Z, Medan", 700, 20);
//
//        lahanArrayList.add(lahan1);
//        lahanArrayList.add(lahan2);
//        lahanArrayList.add(lahan3);

        adapter = new LahanAdapter(lahanArrayList, getApplicationContext());
        listview.setAdapter(adapter);

    }
    public void toTambahLahan(){
        Intent intent = new Intent(this,TambahLahan.class);
        startActivity(intent);
    }
}