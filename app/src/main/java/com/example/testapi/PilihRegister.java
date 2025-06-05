package com.example.testapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapi.Register.ActivityBelumAnggota;
import com.example.testapi.Register.ActivitySudahAnggota;

public class PilihRegister extends AppCompatActivity {

    ImageView btnSudahAnggota, btnBelumAnggota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pilih_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSudahAnggota = findViewById(R.id.btnSudahAnggota);
        btnBelumAnggota = findViewById(R.id.btnBelumAnggota);

        btnSudahAnggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mulai Activity untuk pengguna yang sudah memiliki nomor anggota
                Intent intent = new Intent(PilihRegister.this, ActivitySudahAnggota.class); // Ganti ActivitySudahAnggota.class dengan Activity yang sesuai
                startActivity(intent);
            }
        });

        btnBelumAnggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mulai Activity untuk pengguna yang belum memiliki nomor anggota
                Intent intent = new Intent(PilihRegister.this, ActivityBelumAnggota.class); // Ganti ActivityBelumAnggota.class dengan Activity yang sesuai
                startActivity(intent);
            }
        });
    }
}