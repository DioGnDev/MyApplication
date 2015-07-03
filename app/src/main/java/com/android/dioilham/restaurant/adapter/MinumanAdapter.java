package com.android.dioilham.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.model.Minuman;

import java.util.List;

/**
 * Created by dioilham on 5/22/15.
 */
public class MinumanAdapter extends ArrayAdapter<Minuman> {


    Context context;
    int resource;
    List<Minuman> daftar;

    public MinumanAdapter(Context context, int resource, List<Minuman> daftar) {
        super(context, resource, daftar);
        this.context = context;
        this.resource = resource;
        this.daftar = daftar;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        MinumanHolder holder = null;
        Minuman minuman = getItem(position);

        if (rootView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rootView = inflater.inflate(resource, parent, false);

            holder = new MinumanHolder();
            holder.img = (ImageView) rootView.findViewById(R.id.gambar);
            holder.nama = (TextView) rootView.findViewById(R.id.nama);
            holder.keterangan = (TextView) rootView.findViewById(R.id.ket);
            holder.harga = (TextView) rootView.findViewById(R.id.harga);

            rootView.setTag(holder);

        } else {
            holder = (MinumanHolder) rootView.getTag();
        }

        holder.img.setImageResource(minuman.getImg());
        holder.nama.setText(minuman.getNama());
        holder.keterangan.setText(minuman.getKeterangan());
        holder.harga.setText(String.valueOf(minuman.getHarga()));

        return rootView;
    }

    static class MinumanHolder {
        ImageView img;
        TextView nama;
        TextView keterangan;
        TextView harga;
    }
}
