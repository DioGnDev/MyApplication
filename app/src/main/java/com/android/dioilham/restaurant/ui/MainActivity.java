package com.android.dioilham.restaurant.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.controll.MenuMakanan;
import com.android.dioilham.restaurant.controll.MenuMinuman;
import com.android.dioilham.restaurant.helper.DatabaseHandler;
import com.android.dioilham.restaurant.helper.TampilToast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton food;
    ImageButton drink;
    private ProgressDialog pDialog;
    DatabaseHandler db;
    TampilToast tos;
    int countTableUrl;
    HashMap<String, String> dataURL;
    String urlCustom;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gettingRefRes();
        newObjects();
        countTableUrl = db.getRowCountAlamatServer();
        String alamatURL = db.getAlamatServer();
        if (countTableUrl == 0) {
            tos.tosShort("Silakan mengkonfigurasi alamat server.");
        } else {
            getAlamatServer(alamatURL);
        }
        food.setOnClickListener(this);
        drink.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.resetCart();
    }

    @Override
    public void onBackPressed() {
        db.resetCart();
        MainActivity.this.finish();
    }

    private void getAlamatServer(String alamatURL) {
        if (alamatURL.length() != 0) {
            try {
                urlCustom = java.net.URLDecoder.decode(alamatURL, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            urlCustom = null;
        }
        // ngetes url sekarang
        Log.d("urlSekarang", urlCustom);
    }

    private void newObjects() {
        tos = new TampilToast(MainActivity.this);
        db = new DatabaseHandler(MainActivity.this);    /* call dbhandler*/
    }

    private void gettingRefRes() {
        food = (ImageButton) findViewById(R.id.btnfood);
        drink = (ImageButton) findViewById(R.id.btndrink);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_login).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setserver) {
            dialogSetting(urlCustom, MainActivity.this);
            return true;
        } else if (id == R.id.action_cart) {
            Intent in = new Intent(MainActivity.this, Keranjang.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogSetting(String isi, Context konteks) {
        String isiNew = null;
        AlertDialog.Builder ad = new AlertDialog.Builder(konteks);
        ad.setTitle("Pengaturan Alamat Server");
        ad.setMessage("Contoh: example.com");
        final EditText input = new EditText(konteks);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
//        input.setLayoutParams(lp);
        input.setLayoutParams(new ViewGroup.MarginLayoutParams(5, 2));
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        if (isi != null) {
            try {
                isiNew = java.net.URLDecoder.decode(isi, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            isiNew = "";
        }
//				Log.d("BLABLABLABLABLA", isiNew);
        input.setText(isiNew);
        ad.setView(input);
        ad.setIcon(R.mipmap.ic_setting_joss);
        ad.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String urlString = input.getText().toString();
                Log.d("Inputan URL", urlString);
                String urlEncoded = Uri.encode(urlString);
                if (input.length() != 0) {
                    db.resetAlamatServer();
                    db.addAlamatServer(urlEncoded);
                    urlCustom = db.getAlamatServer();
                    try {
                        String urlDecoded = java.net.URLDecoder.decode(urlCustom, "UTF-8");
                        Log.d("URL Decodean", urlDecoded);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Alamat server terupdate.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Alamat server tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ad.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.show();
    }

    @Override
    public void onClick(View view) {
        db = new DatabaseHandler(MainActivity.this);
        countTableUrl = db.getRowCountAlamatServer();
        Log.d("COUNTER TABEL", String.valueOf(countTableUrl));
        if (view == food) {
            if (countTableUrl > 0) {
                Intent intent = new Intent(this, MenuMakanan.class);
                startActivity(intent);
            } else {
                tos.tosShort("Silakan menentukan alamat server terlebih dahulu.");
            }
        }
        if (view == drink) {
            if (countTableUrl > 0) {
                Intent intent = new Intent(this, MenuMinuman.class);
                startActivity(intent);
            } else {
                tos.tosShort("Silakan menentukan alamat server terlebih dahulu.");
            }
        }
    }
}
