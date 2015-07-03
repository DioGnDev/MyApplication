package com.android.dioilham.restaurant.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.dioilham.restaurant.R;
import com.android.dioilham.restaurant.helper.TampilToast;

/**
 * Created by danielnimafa on 20/06/2015.
 */
public class CartDetil extends ActionBarActivity {

    TampilToast tos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keranjang_detil);
        getRefRes();
        callNewObjects();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_detil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delitem) {
            hapusItem();
        }
        return super.onOptionsItemSelected(item);
    }

    private void callNewObjects() {
        tos = new TampilToast(CartDetil.this);
    }

    private void getRefRes() {

    }

    private void hapusItem() {

    }
}
