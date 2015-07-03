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
import com.android.dioilham.restaurant.model.Makanan;

import java.util.List;

/**
 * Created by dioilham on 5/22/15.
 */
public class MakananAdapter extends ArrayAdapter<Makanan> {

    Context context;
    int resource;
    List<Makanan> daftar;

    public MakananAdapter(Context context, int resource, List<Makanan> daftar) {
        super(context, resource, daftar);
        this.context = context;
        this.resource = resource;
        this.daftar = daftar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        MakananHolder holder = null;
        Makanan makanan = getItem(position);

        if(rootView==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            rootView = inflater.inflate(resource,parent,false);

            holder = new MakananHolder();
            holder.img = (ImageView) rootView.findViewById(R.id.gambar);
            holder.nama = (TextView) rootView.findViewById(R.id.nama);
            holder.keterangan = (TextView) rootView.findViewById(R.id.ket);
            holder.harga = (TextView) rootView.findViewById(R.id.harga);

            rootView.setTag(holder);

        }
        else {
            holder = (MakananHolder)rootView.getTag();
        }

        holder.img.setImageResource(makanan.getImg());
        holder.nama.setText(makanan.getNama());
        holder.keterangan.setText(makanan.getKeterangan());
        holder.harga.setText(String.valueOf(makanan.getHarga()));

        return rootView;
    }

    static class MakananHolder {
        ImageView img;
        TextView nama;
        TextView keterangan;
        TextView harga;
    }
}
