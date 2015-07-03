package com.android.dioilham.restaurant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.dioilham.restaurant.R;

public class Food_Detail extends AppCompatActivity {

    TextView title;
    TextView detail;
    TextView rupiah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Intent intent = getIntent();
        String nama = intent.getExtras().getString("name");
        String keterangan = intent.getExtras().getString("detail");
        int harga = intent.getExtras().getInt("price");

        title = (TextView) findViewById(R.id.foodname);
        detail = (TextView) findViewById(R.id.fooddetail);
        rupiah = (TextView) findViewById(R.id.foodprice);

        title.setText(nama);
        detail.setText(keterangan);
        rupiah.setText(String.valueOf(harga));


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
