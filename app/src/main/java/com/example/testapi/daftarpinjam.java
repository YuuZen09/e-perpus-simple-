package com.example.testapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapi.model.buku.BorrowedBooksAdapter;
import com.example.testapi.model.buku.bukuModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class daftarpinjam extends AppCompatActivity {

    private ListView listView;
    private BorrowedBooksAdapter adapter;
    private ArrayList<bukuModel> borrowedBooksList;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarpinjam);

        listView = findViewById(R.id.listViewBorrowedBooks);
        borrowedBooksList = new ArrayList<>();
        adapter = new BorrowedBooksAdapter(this, borrowedBooksList);
        listView.setAdapter(adapter);

        sessionManager = new SessionManager(this);

        loadBorrowedBooks();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_pinjam);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_koleksi:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_pinjam:
                        return true;
                }
                return false;
            }
        });
    }

    private void loadBorrowedBooks() {
        String userId = sessionManager.getUserDetail().get(SessionManager.USER_ID);
        Log.d("daftarpinjam", "User ID: " + userId); // Add logging for userId
        String url = "http://192.168.41.127/MYAPP_API/terpinjam.php";

        JSONObject params = new JSONObject();
        try {
            params.put("id_user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("daftarpinjam", "JSON Exception: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("daftarpinjam", "Response: " + response.toString()); // Add logging for response
                        try {
                            JSONArray booksArray = response.getJSONArray("books");
                            borrowedBooksList.clear(); // Clear the list to avoid duplication
                            for (int i = 0; i < booksArray.length(); i++) {
                                JSONObject bookObject = booksArray.getJSONObject(i);

                                String id = bookObject.getString("id_buku");
                                String judul = bookObject.getString("judul");
                                String penulis = bookObject.getString("pengarang");
                                String cover = bookObject.getString("gambar");

                                bukuModel book = new bukuModel(id, judul, penulis, cover);
                                borrowedBooksList.add(book);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(daftarpinjam.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("daftarpinjam", "JSON Exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Log.e("API Error", errorMsg);
                        } else {
                            Log.e("API Error", error.toString());
                        }
                        Toast.makeText(daftarpinjam.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("daftarpinjam", "Volley Error: " + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
