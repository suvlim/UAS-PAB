package com.uph23.edu.sahabattani;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputLayout;
import com.uph23.edu.sahabattani.model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RegisterActivity extends AppCompatActivity {
    EditText edtAkun,edtPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtAkun = findViewById(R.id.edtAkun);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()) {
                    simpanData();
                }
            }
        });
    }
    public void simpanData(){
        Realm realm = Realm.getDefaultInstance();

        String username = edtAkun.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        User user_terdaftar = realm.where(User.class).equalTo("username",username).findFirst();

        if(user_terdaftar != null) {
            Toast.makeText(this, "Akun sudah terdaftar", Toast.LENGTH_SHORT).show();
        }
        else{
            realm.executeTransaction(r -> {
                Number maxId = r.where(User.class).max("userid");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                User user = r.createObject(User.class, nextId);
                user.setUsername(username);
                user.setPassword(password);
            });
            Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
            toLogin();
        }
    }

    public boolean validateInput() {
        boolean isValid = true;
        String username = edtAkun.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        if (password.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }
    public void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}