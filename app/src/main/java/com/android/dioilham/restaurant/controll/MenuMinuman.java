package com.android.dioilham.restaurant.controll;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.adapter.MinumanAdapter;
import com.android.dioilham.restaurant.helper.DatabaseHandler;
import com.android.dioilham.restaurant.helper.Koneksi;
import com.android.dioilham.restaurant.helper.TampilToast;
import com.android.dioilham.restaurant.model.Minuman;
import com.android.dioilham.restaurant.parser.JSONParser;
import com.android.dioilham.restaurant.ui.Keranjang;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dioilham on 5/22/15.
 */
public class MenuMinuman extends AppCompatActivity {

    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    //list
    private List<Minuman> drink;
    private SwipeMenuListView listdrink;
    private MinumanAdapter adapter;
    private TampilToast tos;
    private DatabaseHandler db;

    // url to get all products list
    private static String url_menu_minuman;
//    private static String url_menu_minuman = "http://192.168.56.1/restaurant_server/getdrink.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FD = "item_restoran";
    private static final String TAG_KODE = "kode_item";
    private static final String TAG_NAMA = "nama_item";
    private static final String TAG_HARGA = "harga_item";
    private static final String TAG_JENIS = "jenis_item";
    private static final String TAG_KETERANGAN = "keterangan_item";
    private static String TAG_FOOD = "food";
    private static String TAG_DRINK = "drink";
    Koneksi koneksi;

    // products JSONArray
    JSONArray minuman = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_daftar_minuman);
        listdrink = (SwipeMenuListView) findViewById(R.id.list_minuman);
        callNewObjects();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                createMenu(swipeMenu);

            }
        };

        listdrink.setMenuCreator(creator);


        // Loading products in Background Thread
        new LoadAllProducts().execute();

        listdrink.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                pilihMenuItem(position, menu, index);
                return false;
            }
        });

    }

    private void createMenu(SwipeMenu swipeMenu) {
        SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
        openItem.setBackground(R.color.md_pink_400);
        openItem.setWidth(dp2px(90));
        openItem.setTitle("Buka");
        openItem.setTitleSize(18);
        openItem.setTitleColor(getResources().getColor(R.color.white));
        swipeMenu.addMenuItem(openItem);

        SwipeMenuItem buyItem = new SwipeMenuItem(getApplicationContext());
        buyItem.setBackground(R.color.md_cyan_300);
        buyItem.setWidth(dp2px(90));
        buyItem.setTitle("Beli");
        buyItem.setTitleColor(getResources().getColor(R.color.white));
        buyItem.setTitleSize(18);
        swipeMenu.addMenuItem(buyItem);
    }

    private void pilihMenuItem(int position, SwipeMenu menu, int index) {
        Minuman minum = drink.get(position);

        switch (index){
            case 0:
                bukaDetail(minum);
                break;

            case 1:
                beliItem(minum);
                break;
        }
    }

    private void beliItem(Minuman minum) {

        String kode = minum.getKode();
        String nama = minum.getNama();
        String harga = minum.getHarga();
        String jenis = minum.getJenis();
        String keterangan = minum.getKeterangan();
        String pesan = "Tambahkan " + "'" + nama + "'" + " ke Daftar Item Transaksi?";

        tampilDialog(pesan, kode, nama, String.valueOf(harga), jenis, keterangan);
    }

    private void bukaDetail(Minuman minum) {

        String kode = minum.getKode();
        String nama = minum.getNama();
        String harga = minum.getHarga();
        String jenis = minum.getJenis();
        String keterangan = minum.getKeterangan();
        String pesan = "Tambahkan " + "'" + nama + "'" + " ke Daftar Item Transaksi?";

        tampilDialog(pesan, kode, nama, String.valueOf(harga), jenis, keterangan);
    }


    private void callNewObjects() {
        drink = new ArrayList<Minuman>();
        db = new DatabaseHandler(MenuMinuman.this);
        tos = new TampilToast(MenuMinuman.this);
        adapter = new MinumanAdapter(MenuMinuman.this,R.layout.row_drink,drink);
        koneksi = new Koneksi(MenuMinuman.this);
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuMinuman.this);
            pDialog.setMessage("Tunggu Sebentar...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            url_menu_minuman = koneksi.urlItem() + "getitem.php";
            Log.d("URL", url_menu_minuman);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag",TAG_DRINK));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_menu_minuman, "POST", params);
            // Check your log cat for JSON reponse
            Log.d("Semua Minuman: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found Getting Array of Products
                    minuman = json.getJSONArray(TAG_FD);
                    // looping through All Products
                    for (int i = 0; i < minuman.length(); i++) {
                        JSONObject object = minuman.getJSONObject(i);
                        /* Storing each json item in variable*/
                        //int kode = object.getInt(TAG_KODE);
                        String nama = object.getString(TAG_NAMA);
                        String harga = object.getString(TAG_HARGA);
                        String keterangan = object.getString(TAG_KETERANGAN);

                        Minuman minuman = new Minuman();
                        minuman.setKode(object.getString(TAG_KODE));
                        minuman.setJenis(object.getString(TAG_JENIS));
                        minuman.setNama(nama);
                        minuman.setHarga(harga);
                        minuman.setKeterangan(keterangan);

                        //masukkan dalam list
                        drink.add(minuman);
                    }
                } else {
                    tos.tosShort("Data Tidak Ditemukan");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            listdrink.setAdapter(adapter);
        }

    }

    private void tampilDialog(final String pesan, final String kode, final String nama, final String harga, final String jenis, final String keterangan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MenuMinuman.this);
        builder.setTitle("Daftar Item");
        builder.setMessage(pesan);
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        db = new DatabaseHandler(MenuMinuman.this);
                        int countItem = db.cekSameItemCart(kode);
                        if (countItem > 0) {
                            String jumlah = db.getQtyItemCart(kode);
                            int jum = Integer.parseInt(jumlah) + 1;
                            String qty = String.valueOf(jum);
                            int rSub = calcSubtotal(qty, harga);
                            String subtotal = String.valueOf(rSub);
                            Log.d("HASIL PARSE ", subtotal);
//                            db.addToCart(kode, nama, harga, jenis, keterangan, qty, harga);
                            db.updateItemCart(kode, qty, subtotal);
                        } else {
                            db.addToCart(kode, nama, harga, jenis, keterangan, "1", harga);
                        }
                        tos.tosShort("Item " + nama + " telah ditambahkan.");
                    }
                });
        builder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog ad = builder.create();
        ad.show();
    }

    private int calcSubtotal(String qty, String harga) {
        int price = Integer.parseInt(harga);
        int quant = Integer.parseInt(qty);
        int result = price * quant;
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent in = new Intent(MenuMinuman.this, Keranjang.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
