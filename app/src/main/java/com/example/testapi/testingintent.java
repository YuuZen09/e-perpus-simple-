package com.example.testapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.testapi.model.buku.bukuModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class testingintent extends AppCompatActivity {

    ImageView coverBK;
    Button btnPinjamDownload, btnBaca;
    SessionManager sessionManager;
    bukuModel buku;
    String URL = "http://192.168.41.127/MYAPP_API/";

    String userId;

    RequestQueue requestQueue;

    private ProgressDialog progressDialog;
    private String downloadedFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testingintent);

        buku = getIntent().getParcelableExtra("buku");

        SharedPreferences sharedPreferences = getSharedPreferences("book_downloads", Context.MODE_PRIVATE);
        downloadedFileName = sharedPreferences.getString(buku.getId(), null);

        sessionManager = new SessionManager(this);

        userId = sessionManager.getUserDetail().get(SessionManager.USER_ID);

        requestQueue = Volley.newRequestQueue(this);

        btnPinjamDownload = findViewById(R.id.btnpinjamdownload);

        btnBaca = findViewById(R.id.btnbaca);

        TextView judulTextView = findViewById(R.id.detailJudul);
        TextView penulisTextView = findViewById(R.id.detailPenulis);
        TextView sinopsisTextView = findViewById(R.id.sinopsis);
        TextView tahunTextView = findViewById(R.id.detailtahun);
        TextView penerbitTextView = findViewById(R.id.detailPenerbit);
        coverBK = findViewById(R.id.cover);

        judulTextView.setText(buku.getJudul());
        penulisTextView.setText(buku.getPenulis());
        sinopsisTextView.setText(buku.getSinopsis());
        tahunTextView.setText(buku.getTahun());
        penerbitTextView.setText(buku.getPenerbit());

        Glide.with(this)
                .load(buku.getCover())
                .into(coverBK);

        ubahtombol();

        btnPinjamDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = btnPinjamDownload.getText().toString();
                if (buttonText.equals("Pinjam")) {
                    pinjambuku(buku.getId());
                } else if (buttonText.equals("Download")) {
                    downloadbuku(buku.getId());
                }
            }
        });

        btnBaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadedFileName != null) {
                    String pdfFilePath = getFilesDir() + "/buku/" + downloadedFileName;
                    File pdfFile = new File(pdfFilePath);
                    if (pdfFile.exists()) {
                        Intent intent = new Intent(testingintent.this, BacaBuku.class);
                        intent.putExtra("pdf_path", pdfFilePath);
                        startActivity(intent);
                    } else {
                        Toast.makeText(testingintent.this, "PDF file not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(testingintent.this, "No PDF downloaded yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ubahtombol() {
        cekpinjam(buku.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(final boolean terpinjam) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("UbahTombol", "terpinjam: " + terpinjam);
                        Log.d("UbahTombol", "cekDownload: " + cekDownload(buku.getId()));

                        if (terpinjam) {
                            if (cekDownload(buku.getId())) {
                                btnPinjamDownload.setVisibility(View.GONE);
                                btnBaca.setVisibility(View.VISIBLE);
                                btnBaca.setEnabled(true);
                            } else {
                                btnPinjamDownload.setText("Download");
                                btnPinjamDownload.setVisibility(View.VISIBLE);
                                btnBaca.setVisibility(View.GONE);
                                btnBaca.setEnabled(false);
                            }
                        } else {
                            btnPinjamDownload.setText("Pinjam");
                            btnPinjamDownload.setVisibility(View.VISIBLE);
                            btnBaca.setVisibility(View.GONE);
                            btnBaca.setEnabled(false);
                        }

                        Log.d("UbahTombol", "btnPinjamDownload visibility: " + btnPinjamDownload.getVisibility());
                        Log.d("UbahTombol", "btnPinjamDownload text: " + btnPinjamDownload.getText());
                        Log.d("UbahTombol", "btnBaca visibility: " + btnBaca.getVisibility());
                        Log.d("UbahTombol", "btnBaca enabled: " + btnBaca.isEnabled());
                    }
                });
            }
        });
    }

    private boolean cekDownload(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("book_downloads", Context.MODE_PRIVATE);
        String fileName = sharedPreferences.getString(id, "");
        return !fileName.isEmpty();
    }

    private void cekpinjam(String bookId, final VolleyCallback callback) {
        String uri = URL + "cekpinjam.php?id_user=" + userId + "&id_buku=" + bookId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                callback.onSuccess(status.equals("borrowed"));

                Log.d("cekpinjam", "Response: " + response);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("cekpinjam", "Error parsing JSON: " + e.getMessage());
                callback.onSuccess(false);
            }
        }, error -> {
            Log.e("cekpinjam", "Network error: " + error.getMessage());
            callback.onSuccess(false);
        });
        requestQueue.add(stringRequest);
        Log.d("cekpinjam", "Request URL: " + uri);
    }

    private void pinjambuku(String id) {
        String uri = URL + "peminjaman.php";

        Map<String, String> params = new HashMap<>();
        params.put("id_user", sessionManager.getUserDetail().get(SessionManager.USER_ID));
        params.put("id_buku", id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                if (status.equals("success")) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    btnPinjamDownload.setText("Download");
                    btnPinjamDownload.setVisibility(View.VISIBLE);
                    btnBaca.setVisibility(View.GONE);
                    btnBaca.setEnabled(false);
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void downloadbuku(String id) {
        String uri = URL + "downloadbuku.php";

        Map<String, String> params = new HashMap<>();
        params.put("id_user", userId);
        params.put("id_buku", id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getString("status").equals("success")) {
                    String fileUrl = jsonResponse.getString("lokasi_file");
                    downloadFile(fileUrl, id); // Start the actual download
                    ubahtombol();
                } else {
                    Toast.makeText(this, "Server error: " + jsonResponse.optString("message", "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing server response", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, "Error downloading book: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void downloadFile(String fileUrl, String bookid) {
        progressDialog = new ProgressDialog(testingintent.this);
        progressDialog.setTitle("Downloading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(fileUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(testingintent.this, "Error downloading PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        long fileSize = body.contentLength();
                        byte[] pdfContent = new byte[(int) fileSize];

                        int bytesRead = 0;
                        int bufferSize = 4096;
                        byte[] buffer = new byte[bufferSize];

                        try {
                            while (bytesRead < fileSize) {
                                int read = body.byteStream().read(buffer);
                                if (read == -1) break;
                                System.arraycopy(buffer, 0, pdfContent, bytesRead, read);
                                bytesRead += read;
                                final int progress = (int) (((double) bytesRead / fileSize) * 100);
                                runOnUiThread(() -> progressDialog.setProgress(progress));
                            }

                            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                            runOnUiThread(() -> {
                                savePdfFile(pdfContent, fileName, bookid);
                                progressDialog.dismiss();
                                ubahtombol();
                            });
                            downloadedFileName = fileName;
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(testingintent.this, "Error saving downloaded book", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                } else {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(testingintent.this, "Download failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("book_downloads", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(buku.getId(), downloadedFileName);
        editor.clear();
        editor.apply();
    }

    private void savePdfFile(byte[] pdfContent, String fileName, String bookID) {
        File bukuDirectory = new File(getFilesDir(), "buku");
        if (!bukuDirectory.exists()) {
            bukuDirectory.mkdirs();
        }
        Log.d("PDF Download", "Buku directory exists: " + bukuDirectory.exists());

        File file = new File(bukuDirectory, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            Log.d("PDF Download", "Writing PDF content to file: " + file.getAbsolutePath());
            outputStream.write(pdfContent);
            outputStream.close();
            SharedPreferences sharedPreferences = getSharedPreferences("book_downloads", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(bookID, fileName);
            editor.apply();
            DownloadedBooksManager.getInstance().addDownloadedBook(bookID, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving downloaded book", Toast.LENGTH_SHORT).show();
            Log.e("PDF Download", "Error saving PDF: ", e);
        }
        Log.d("PDF Download", "Saved PDF to: " + file.getAbsolutePath());
        Data inputData = new Data.Builder()
                .putString("book_id", bookID)
                .build();

        OneTimeWorkRequest deleteRequest = new OneTimeWorkRequest.Builder(DeletePdfWorker.class)
                .setInitialDelay(3, TimeUnit.DAYS)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(deleteRequest);
    }

    private interface VolleyCallback {
        void onSuccess(boolean result);
    }
}
