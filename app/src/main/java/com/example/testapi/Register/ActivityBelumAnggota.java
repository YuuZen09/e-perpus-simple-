package com.example.testapi.Register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ActivityBelumAnggota extends AppCompatActivity {

    private EditText noIndukEditText, nameEditText, tempatLahirEditText, tanggalLahirEditText, alamatEditText, noTelpEditText, emailEditText, namaIbuEditText, noTelpDaruratEditText;
    private RadioGroup statusRadioGroup, pendidikanRadioGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_belum_anggota);
        noIndukEditText = findViewById(R.id.noIndukEditText);
        nameEditText = findViewById(R.id.nameEditText);
        tempatLahirEditText = findViewById(R.id.tempatLahirEditText);
        tanggalLahirEditText = findViewById(R.id.tanggalLahirEditText);
        alamatEditText = findViewById(R.id.alamatEditText);
        noTelpEditText = findViewById(R.id.noTelpEditText);
        emailEditText = findViewById(R.id.emailEditText);
        statusRadioGroup = findViewById(R.id.statusRadioGroup);
        pendidikanRadioGroup = findViewById(R.id.pendidikanRadioGroup);
        namaIbuEditText = findViewById(R.id.namaIbuEditText);
        noTelpDaruratEditText = findViewById(R.id.noTelpDaruratEditText);
        submitButton = findViewById(R.id.submitButton);

        tanggalLahirEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                        tanggalLahirEditText.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void registerUser() {
        String noInduk = noIndukEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String tempatLahir = tempatLahirEditText.getText().toString();
        String tanggalLahir = tanggalLahirEditText.getText().toString();
        String alamat = alamatEditText.getText().toString();
        String noTelp = noTelpEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String namaIbu = namaIbuEditText.getText().toString();
        String noTelpDarurat = noTelpDaruratEditText.getText().toString();

        int selectedStatusId = statusRadioGroup.getCheckedRadioButtonId();
        String status = ((RadioButton) findViewById(selectedStatusId)).getText().toString();

        int selectedPendidikanId = pendidikanRadioGroup.getCheckedRadioButtonId();
        String pendidikan = ((RadioButton) findViewById(selectedPendidikanId)).getText().toString();

        String url = "http://192.168.1.7/MYAPP_API/register.php";

        JSONObject params = new JSONObject();
        try {
            params.put("no_induk", noInduk);
            params.put("nama", name);
            params.put("tempat_lahir", tempatLahir);
            params.put("tanggal_lahir", tanggalLahir);
            params.put("alamat", alamat);
            params.put("no_telp", noTelp);
            params.put("email", email);
            params.put("pendidikan", pendidikan);
            params.put("status", status);
            params.put("nama_ibu", namaIbu);
            params.put("no_telp_darurat", noTelpDarurat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(ActivityBelumAnggota.this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                                finish(); // Kembali ke halaman sebelumnya
                            } else {
                                Toast.makeText(ActivityBelumAnggota.this, "Pendaftaran gagal: " + response.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityBelumAnggota.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityBelumAnggota.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}