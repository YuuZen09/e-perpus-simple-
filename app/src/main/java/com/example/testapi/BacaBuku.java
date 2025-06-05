package com.example.testapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class BacaBuku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca_buku);

        String pdfPath = getIntent().getStringExtra("pdf_path");

        // Tentukan lokasi file PDF
        File pdfFile = new File(pdfPath);

        // Inisialisasi PDFView dari layout
        PDFView pdfView = findViewById(R.id.bacabuku);

        // Tampilkan PDF
        if (pdfFile.exists()) {
            pdfView.fromFile(pdfFile)
                    .password(null) // Jika file PDF memiliki password
                    .defaultPage(0) // Halaman pertama yang ditampilkan saat membuka PDF
                    .enableSwipe(true) // Aktifkan swipe
                    .swipeHorizontal(false) // Swipe vertical
                    .enableDoubletap(true) // Aktifkan double tap
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .load();
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            finish(); // Tutup activity jika file tidak ditemukan
        }
    }
}
