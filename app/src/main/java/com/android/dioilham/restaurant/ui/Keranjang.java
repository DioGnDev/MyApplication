package com.android.dioilham.restaurant.ui;

/**
 * Created by Danielnimafa on 27/06/2015.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.adapter.CartAdapter;
import com.android.dioilham.restaurant.helper.DatabaseHandler;
import com.android.dioilham.restaurant.helper.TampilToast;
import com.android.dioilham.restaurant.model.ItemCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielnimafa on 19/06/2015.
 */
public class Keranjang extends AppCompatActivity {

    TextView subtotal, total;
    ListView listItem;
    CartAdapter adapter;
    ArrayList<ItemCart> items;
    DatabaseHandler db;
    ProgressDialog pDialog;
    TampilToast tos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        getRefRes(); // get resource ID
        callNewObjects();
        int jum = db.countCart();
        if(jum>0){
            adapter = new CartAdapter(Keranjang.this, R.layout.row_shopcart, items);
            new LoadAllItemsCart().execute();

            hitungTotal();
        } else {
            tos.tosShort("Tidak ada Tagihan");
            total.setText("Rp. 0");
        }
    }

    private void hitungTotal() {
        int totalCart = 0;
        db = new DatabaseHandler(Keranjang.this);
        ArrayList<String> subtotal = db.getSubtotalCart();
        for (int i = 0; i < subtotal.size(); i++) {
            int subtots = Integer.parseInt(subtotal.get(i));
            totalCart += subtots;
        }
        total.setText("Rp. "+String.valueOf(totalCart));
    }

    private void callNewObjects() {
        db = new DatabaseHandler(Keranjang.this);
        tos = new TampilToast(Keranjang.this);
        items = new ArrayList<ItemCart>();
    }

    class LoadAllItemsCart extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Keranjang.this);
            pDialog.setMessage("Sedang Memuat...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<ItemCart> haha = new ArrayList<ItemCart>();
            haha = db.getListCartData();
            for(int i=0; i<haha.size(); i++){
                ItemCart data = haha.get(i);
                ItemCart ic = new ItemCart();

                ic.setNamaCart(data.getNamaCart());
                ic.setHargaCart(data.getHargaCart());
                ic.setQtyCart(data.getQtyCart());
                ic.setSubtotalCart(data.getSubtotalCart());

                items.add(ic);
                Log.d("IIIISIIII", ic.toString());
            }
//            Log.d("Semua ItemCart: ", items.toArray().toString());
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                    listItem.setAdapter(adapter);
                }
            }, 1500);

        }
    }

    private void getRefRes() {
        subtotal = (TextView) findViewById(R.id.subtot);
        total = (TextView) findViewById(R.id.total);
        listItem = (ListView) findViewById(R.id.listCart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_checkout) {
            checkout();
            Intent in = new Intent(Keranjang.this, MainActivity.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkout() {

    }
}
