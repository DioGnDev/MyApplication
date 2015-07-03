package com.android.dioilham.restaurant.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by danielnimafa on 19/06/2015.
 */
public class TampilToast {

    Context konteks;

    public TampilToast(Context konteks){
        this.konteks = konteks;
    }

    //    make short toast
    public void tosShort(String pesan){
        Toast.makeText(konteks, pesan, Toast.LENGTH_SHORT).show();
    }

    //    make Long toast
    public void tosLong(String pesan){
        Toast.makeText(konteks, pesan, Toast.LENGTH_LONG).show();
    }

    //    make Custom Duration toast
    public void tosCus(String pesan, int duration){
        Toast.makeText(konteks, pesan, duration).show();
    }
}
