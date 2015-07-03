package com.android.dioilham.restaurant.controll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.adapter.MakananAdapter;
import com.android.dioilham.restaurant.helper.DatabaseHandler;
import com.android.dioilham.restaurant.helper.Koneksi;
import com.android.dioilham.restaurant.helper.TampilToast;
import com.android.dioilham.restaurant.model.Makanan;
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

//import com.android.dioilham.restaurant.ui.Keranjang;

public class MenuMakanan extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    //list
    List<Makanan> food;
    SwipeMenuListView listfood;
    private static String TAG_FOOD = "food";
    Koneksi koneksi;
    DatabaseHandler db;

    // url to get all products list
//    private static String url_menu_makanan = "http://192.168.56.1/restaurant_server/getfood.php";
    private static String url_menu_makanan;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FD = "item_restoran";
    private static final String TAG_KODE = "kode_item";
    private static final String TAG_NAMA = "nama_item";
    private static final String TAG_HARGA = "harga_item";
    private static final String TAG_JENIS = "jenis_item";
    private static final String TAG_KETERANGAN = "keterangan_item";

    // products JSONArray
    JSONArray makanan = null;
    MakananAdapter adapter;
    TampilToast tos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_daftar_makanan);
        // Get listview
        listfood = (SwipeMenuListView) findViewById(R.id.list_makanan);
        callNewObjects();

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                createMenu(swipeMenu);

            }
        };
        listfood.setMenuCreator(creator);

        // Loading products in Background Thread
        new LoadAllProducts().execute();

        listfood.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
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
        Makanan maem = food.get(position);

        switch (index){
            case 0:
                bukaDetail(maem);
                break;

            case 1:
                beliItem(maem);
                break;
        }
    }

    private void beliItem(Makanan maem) {

        String kode = maem.getKode();
        String nama = maem.getNama();
        String harga = maem.getHarga();
        String jenis = maem.getJenis();
        String keterangan = maem.getKeterangan();
        String pesan = "Tambahkan " + "'" + nama + "'" + " ke Daftar Item Transaksi?";

        tampilDialog(pesan, kode, nama, String.valueOf(harga), jenis, keterangan);
    }

    private void bukaDetail(Makanan maem) {

        String kode = maem.getKode();
        String nama = maem.getNama();
        String harga = maem.getHarga();
        String jenis = maem.getJenis();
        String keterangan = maem.getKeterangan();
        String pesan = "Tambahkan " + "'" + nama + "'" + " ke Daftar Item Transaksi?";

        tampilDialog(pesan, kode, nama, String.valueOf(harga), jenis, keterangan);
    }


    private void callNewObjects() {
        food = new ArrayList<Makanan>();
        adapter = new MakananAdapter(MenuMakanan.this, R.layout.row_food, food);
        koneksi = new Koneksi(MenuMakanan.this);
        tos = new TampilToast(MenuMakanan.this);
        db = new DatabaseHandler(MenuMakanan.this);
    }

    private void tampilDialog(final String pesan, final String kode, final String nama, final String harga, final String jenis, final String keterangan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MenuMakanan.this);
        builder.setTitle("Daftar Item");
        builder.setMessage(pesan);
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        db = new DatabaseHandler(MenuMakanan.this);
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

    class LoadAllProducts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuMakanan.this);
            pDialog.setMessage("Tunggu Sebentar...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            url_menu_makanan = koneksi.urlItem() + "getitem.php";
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", TAG_FOOD));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_menu_makanan, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("Semua Makanan: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    makanan = json.getJSONArray(TAG_FD);

                    // looping through All Products
                    for (int i = 0; i < makanan.length(); i++) {
                        JSONObject object = makanan.getJSONObject(i);

//                        int kode = object.getInt(TAG_KODE);
                        String kode = object.getString(TAG_KODE);
                        String nama = object.getString(TAG_NAMA);
                        String harga = object.getString(TAG_HARGA);
                        String jenis = object.getString(TAG_JENIS);
                        String keterangan = object.getString(TAG_KETERANGAN);

                        Makanan makanan = new Makanan();
                        makanan.setKode(kode);
                        makanan.setNama(nama);
                        makanan.setHarga(harga);
                        makanan.setJenis(jenis);
                        makanan.setKeterangan(keterangan);

                        food.add(makanan);
//                        Log.d("food : ", food.toString());
                    }
                } else if (success == 0) {
                    Toast.makeText(getApplicationContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *N
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            listfood.setAdapter(adapter);
        }

    }

    protected Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuMakanan.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View DialogView = inflater.inflate(R.layout.activity_food_detail, null);

        return builder.create();
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
            Intent in = new Intent(MenuMakanan.this, Keranjang.class);
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