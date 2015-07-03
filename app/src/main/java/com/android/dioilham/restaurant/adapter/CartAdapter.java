package com.android.dioilham.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.model.ItemCart;

import java.util.List;

/**
 * Created by danielnimafa on 19/06/2015.
 */
public class CartAdapter extends ArrayAdapter<ItemCart> {

    Context context;
    int resource;
    List<ItemCart> daftar;

    public CartAdapter(Context context, int resource, List<ItemCart> haha) {
        super(context, resource, haha);
        this.context = context;
        this.resource = resource;
        this.daftar = haha;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        ItemCartHolder holder = null;
        ItemCart dataItemCart = getItem(position);
        Log.d("ItemsBro1", dataItemCart.getNamaCart());

        if (rootView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rootView = inflater.inflate(resource, parent, false);

            holder = new ItemCartHolder();
            holder.qty = (TextView) rootView.findViewById(R.id.qty);
            holder.nama = (TextView) rootView.findViewById(R.id.nama);
            holder.subtotal = (TextView) rootView.findViewById(R.id.subtotal);
            holder.harga = (TextView) rootView.findViewById(R.id.harga);

            rootView.setTag(holder);

        } else {
            holder = (ItemCartHolder) rootView.getTag();
        }

        holder.qty.setText(dataItemCart.getQtyCart() + " item");
        holder.nama.setText(dataItemCart.getNamaCart());
        holder.subtotal.setText("Subtotal: Rp. "+dataItemCart.getSubtotalCart());
        holder.harga.setText("Rp. "+String.valueOf(dataItemCart.getHargaCart()));

        return rootView;
    }

    static class ItemCartHolder {
        TextView nama;
        TextView harga;
        TextView qty;
        TextView subtotal;
    }
}
