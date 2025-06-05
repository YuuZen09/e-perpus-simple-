package com.example.testapi.model.buku;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.testapi.DownloadedBooksManager;
import com.example.testapi.R;
import com.example.testapi.testingintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BorrowedBooksAdapter extends ArrayAdapter<bukuModel> {

    private ArrayList<bukuModel> borrowedBooksList;
    private Context context;
    private HashMap<String, String> downloadedFileNames;

    public BorrowedBooksAdapter(Context context, ArrayList<bukuModel> borrowedBooksList) {
        super(context, 0, borrowedBooksList);
        this.borrowedBooksList = borrowedBooksList;
        this.context = context;
        this.downloadedFileNames = new HashMap<>();
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_borrowed_book, parent, false);
        }

        bukuModel book = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.judulTextView);
        TextView authorTextView = convertView.findViewById(R.id.penulisTextView);
        ImageView coverImageView = convertView.findViewById(R.id.coverImageView);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        if (book != null) {
            titleTextView.setText(book.getJudul());
            authorTextView.setText(book.getPenulis());
            Glide.with(context).load(book.getCover()).into(coverImageView);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book.getId_buku(), position);
            }
        });

        return convertView;
    }

    private void deleteBook(String bookId, int position) {
        String url = "http://192.168.41.127/MYAPP_API/hapuspinjam.php";

        Log.d("DeleteBook", "Deleting book with ID: " + bookId);

        JSONObject params = new JSONObject();
        try {
            params.put("id_buku", bookId);  // Ensure this key matches what the PHP expects
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                // Remove the item from the list and notify adapter
                                borrowedBooksList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                                deletePdfFileForBook(bookId);

                                if (context instanceof testingintent) { // Check if context is testingintent
                                    ((testingintent) context).ubahtombol(); // Update buttons after deletion
                                    Log.d("DeleteBook", "Called ubahtombol after deletion");
                                }
                            } else {
                                Toast.makeText(context, "Failed to delete book: " + response.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void deletePdfFileForBook(String bookId) {
        File filesDir = context.getFilesDir();
        Log.d("PDF Deletion", "Files Directory: " + filesDir.getAbsolutePath());
        String fileName = DownloadedBooksManager.getInstance().getFileNameForBook(bookId);

        if (fileName != null) {
            File file = new File(filesDir, "buku/" + fileName);
            Log.d("PDF Deletion", "Attempting to delete: " + file.getAbsolutePath());
            if (file.exists()) {
                Log.d("PDF Deletion", "File exists");
                if (file.canWrite()) {
                    if (file.delete()) {
                        Log.d("PDF Deletion", "Deleted PDF: " + file.getAbsolutePath());
                        // Remove the file name from the HashMap after successful deletion
                        DownloadedBooksManager.getInstance().removeDownloadedBook(bookId);
                    } else {
                        Log.e("PDF Deletion", "Failed to delete PDF: " + file.getAbsolutePath());
                    }
                } else {
                    Log.e("PDF Deletion", "File is not writable: " + file.getAbsolutePath());
                }
            } else {
                Log.e("PDF Deletion", "File does not exist: " + file.getAbsolutePath());
            }
        } else {
            Log.w("PDF Deletion", "No downloaded file name found for book ID: " + bookId);
        }
    }
}
