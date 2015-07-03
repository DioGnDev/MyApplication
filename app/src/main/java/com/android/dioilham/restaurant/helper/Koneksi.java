package com.android.dioilham.restaurant.helper;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by danielnimafa on 18/06/2015.
 */
public class Koneksi {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private static final String urlPath = "/restaurant_server/";
    private static String alamatServer = "";
    String urlIndex, urlFull;
    DatabaseHandler db;

    public Koneksi(Context konteks) {
        db = new DatabaseHandler(konteks);
        alamatServer = db.getAlamatServer();
        try {
            urlIndex = java.net.URLDecoder.decode(alamatServer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String urlItem(){
        String hasil = "http://"+urlIndex + urlPath;
        return hasil;
    }
}
