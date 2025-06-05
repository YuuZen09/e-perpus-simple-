package com.example.testapi.model.buku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.testapi.R;

import java.util.ArrayList;
import java.util.List;

public class bukuAdapter extends ArrayAdapter<bukuModel> {


    private LayoutInflater inflater;
    private int layoutResource;
    private List<bukuModel> listbuku;
    private OnItemClickListener listener;


    public bukuAdapter(Context context, int resource, List<bukuModel> listbuku) {
        super(context, resource, listbuku);
        this.inflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.listbuku = listbuku;
    }

    public void filter(String text) {
        List<bukuModel> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            // If search text is empty, show all books
            filteredList.addAll(listbuku); // Still use the original list here
        } else {
            text = text.toLowerCase();
            for (bukuModel item : listbuku) { // Iterate over the original list
                if (item.getJudul().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }

        // Update the adapter with the filtered list (without modifying the original list)
        clear();
        addAll(filteredList); // Add to the adapter's internal list
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView textJudul;
        TextView textPenulis;
        ImageView imageViewCover;
    }

    public interface OnItemClickListener {
        void OnBukuClick(bukuModel buku);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);

            holder = new ViewHolder();
            holder.textJudul = convertView.findViewById(R.id.judul);
            holder.textPenulis = convertView.findViewById(R.id.penulis);
            holder.imageViewCover = convertView.findViewById(R.id.cover);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bukuModel buku = listbuku.get(position);

        holder.textJudul.setText(buku.getJudul());
        holder.textPenulis.setText(buku.getPenulis());

        // Clear the ImageView before loading a new image
        holder.imageViewCover.setImageResource(0);

        // Menampilkan gambar menggunakan Glide
        Glide.with(getContext())
                .load(buku.getCover())
                .into(holder.imageViewCover);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnBukuClick(buku);
                }
            }
        });

        return convertView;
    }
}
