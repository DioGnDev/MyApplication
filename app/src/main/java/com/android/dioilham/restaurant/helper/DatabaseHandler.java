package com.android.dioilham.restaurant.helper;

/**
 * Created by danielnimafa on 18/06/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.dioilham.restaurant.model.ItemCart;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbrestoran";

    private static final String TABEL_URL = "setserver";
    private static final String KEY_ID = "kode";
    private static final String KEY_rec_url = "alamat";
    //    cart
    private static final String KEY_ID_CART = "id";
    private static final String TABEL_CART = "cartlist";
    private static final String KEY_KODE_CART = "kode_cart";
    private static final String KEY_NAMA_CART = "nama_cart";
    private static final String KEY_HARGA_CART = "harga_cart";
    private static final String KEY_JENIS_CART = "jenis_cart";
    private static final String KEY_KETERANGAN_CART = "keterangan_cart";
    private static final String KEY_QTY_CART = "qty_cart";
    private static final String KEY_SUBTOTAL_CART = "subtotal_cart";
    private static final String KEY_TGL_CART = "tgl_cart";
    // tabel URL Setting
    private static final String CREATE_URL_SETTING = "CREATE TABLE "
            + TABEL_URL + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_rec_url + " TEXT" + ")";

    private static final String CREATE_CART = "CREATE TABLE "
            + TABEL_CART + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_KODE_CART + " TEXT,"
            + KEY_NAMA_CART + " TEXT," + KEY_HARGA_CART + " TEXT," + KEY_JENIS_CART + " TEXT,"
            + KEY_KETERANGAN_CART + " TEXT," + KEY_QTY_CART + " TEXT," + KEY_SUBTOTAL_CART + " TEXT)";

//    private static final String CREATE_CART = "CREATE TABLE "
//            + TABEL_CART + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_KODE_CART + " TEXT,"
//            + KEY_NAMA_CART + " TEXT," + KEY_HARGA_CART + " TEXT," + KEY_JENIS_CART + " TEXT,"
//            + KEY_KETERANGAN_CART + " TEXT," + KEY_QTY_CART + " TEXT," + KEY_SUBTOTAL_CART + " TEXT,"
//            + KEY_TGL_CART + " TEXT)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // buat tabel
        db.execSQL(CREATE_URL_SETTING);
        db.execSQL(CREATE_CART);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* Drop older table if existed*/
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_URL);
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_CART);
        // Create tables again
        onCreate(db);
    }

    // -------------------------------- Begin Pengaturan Server
    public void addAlamatServer(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_rec_url, url);
        db.insert(TABEL_URL, null, values);
        db.close();
    }

    public String getAlamatServer() {
        String hasil = "";
        String q = "SELECT * FROM " + TABEL_URL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(q, null);
        cur.moveToFirst();
        if (cur.getCount() > 0) {
//            kode.put(KEY_rec_url, cur.getString(1));
            hasil = cur.getString(1);
        }
        cur.close();
        db.close();
        return hasil;
    }

    public void resetAlamatServer() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_URL, null, null);
        db.close();
    }

    public int getRowCountAlamatServer() {
        String countQuery = "SELECT*FROM " + TABEL_URL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }
    // -------------------------------- End Pengaturan Server

    // -------------------------------- begin Add To Cart
    public void addToCart(String kode, String nama, String harga, String jenis, String keterangan,
                          String qty, String subtotal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_KODE_CART, kode);
        values.put(KEY_NAMA_CART, nama);
        values.put(KEY_HARGA_CART, harga);
        values.put(KEY_JENIS_CART, jenis);
        values.put(KEY_KETERANGAN_CART, keterangan);
        values.put(KEY_QTY_CART, qty);
        values.put(KEY_SUBTOTAL_CART, subtotal);
        db.insert(TABEL_CART, null, values);
        db.close();
    }

    public int countCart() {
        String countQuery = "SELECT * FROM " + TABEL_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_CART, null, null);
        db.close();
    }

    public int cekSameItemCart(String kode) {
        String countQuery = "SELECT * FROM " + TABEL_CART + " where kode_cart='"+ kode +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void updateItemCart(String kode, String qty, String subtotal) {
        Boolean hasil = null;
        String[] args = new String[]{kode};
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QTY_CART, qty);
        values.put(KEY_SUBTOTAL_CART, subtotal);
        db.update(TABEL_CART, values, "kode_cart=?", args);
        db.close();
    }

    public String getQtyItemCart(String kode) {
        String countQuery = "SELECT "+KEY_QTY_CART+" FROM " + TABEL_CART + " where kode_cart='"+ kode +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        String qty = "";
        Cursor cur = db.rawQuery(countQuery, null);
        cur.moveToFirst();
        if (cur.getCount() > 0) {
            qty = cur.getString(0);
        }
        cur.close();
        db.close();
        return qty;
    }

    public ArrayList<String> getSubtotalCart() {
        ArrayList<String> hasil = new ArrayList<String>();
        String q = "SELECT "+KEY_SUBTOTAL_CART+" FROM " + TABEL_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(q, null);
        if(cur.moveToFirst()){
            do{
                String subtot = cur.getString(0);
                hasil.add(subtot);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return hasil;
    }

    public ArrayList<ItemCart> getListCartData(){
        ArrayList<ItemCart> hoho = new ArrayList<ItemCart>();
        String q = "SELECT * FROM " + TABEL_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(q, null);
        if(cur.moveToFirst()){
            do{
                ItemCart dataCart = new ItemCart();

                dataCart.setKodeCart(cur.getString(1));
                dataCart.setNamaCart(cur.getString(2));
                dataCart.setHargaCart(cur.getString(3));
                dataCart.setJenisCart(cur.getString(4));
                dataCart.setKetCart(cur.getString(5));
                dataCart.setQtyCart(cur.getString(6));
                dataCart.setSubtotalCart(cur.getString(7));

                hoho.add(dataCart);

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return hoho;
    }
    // -------------------------------- End Add To Cart
}
