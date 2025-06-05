package com.example.testapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapi.model.buku.bukuAdapter;
import com.example.testapi.model.buku.bukuModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements bukuAdapter.OnItemClickListener{

        SessionManager sessionManager;

        private static final String URL = "http://192.168.41.127/myapp_api/buku.php";
        List<bukuModel> listbuku;
        ListView listView;

        bukuAdapter adapter;
        TextView etNama;

        String Nama;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn() == false) {
            movetoLogin();
        }

        etNama = findViewById(R.id.nama);
        Nama = sessionManager.getUserDetail().get(SessionManager.NAMA);

        etNama.setText(Nama);


        listView = findViewById(R.id.lvbuku);
        listbuku = new ArrayList<>();
        adapter = new bukuAdapter(this, R.layout.listbuku, listbuku);
        listView.setAdapter(adapter);

        loadbuku();


        // Set listener pada adapter
        adapter.setOnItemClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.nav_koleksi);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_pinjam:
                        startActivity(new Intent(getApplicationContext(), daftarpinjam.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_koleksi:
                        return true;
                }
                return false;
            }
        });

    }



    private void loadbuku() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject buku = array.getJSONObject(i);

                        listbuku.add(new bukuModel(
                                buku.getString("id_buku"),
                                buku.getString("judul"),
                                buku.getString("pengarang"),
                                buku.getString("gambar"),
                                buku.getString("sinopsis"),
                                buku.getString("tahun"),
                                buku.getString("penerbit")
                        ));
                    }

                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void movetoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionlogout:
                sessionManager.logoutSession();
                movetoLogin();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnBukuClick(bukuModel buku) {
        Intent intent = new Intent(MainActivity.this, testingintent.class);
        intent.putExtra("buku", buku);
        startActivity(intent);
    }
}