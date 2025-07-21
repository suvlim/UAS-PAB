package com.uph23.edu.sahabattani;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uph23.edu.sahabattani.model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {
    TextView txvDaftar;
    EditText edtAkun,edtPassword;
    Button btnLogin;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtAkun = findViewById(R.id.edtAkun);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    loginUser();
                }
            }
        });
        txvDaftar = findViewById(R.id.txvDaftar);
        txvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegister();
            }
        });
    }
    public void toRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void toDashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
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
    private void loginUser(){
        String username = edtAkun.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        User user = realm.where(User.class).equalTo("username",username).findFirst();

        if(user != null){
            if(user.getPassword().equals(password)){
                SharedPreferences prefs = getSharedPreferences("MyApp",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("userid",user.getUserid());
                editor.putString("username", user.getUsername());
                editor.apply();
                toDashboard();
            }
            else{
                Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        super.onDestroy();
        // Close the Realm instance when the activity is destroyed
        if (realm != null) {
            realm.close();
        }
    }
}
