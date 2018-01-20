package com.example.daisu.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by daisu on 18.01.2018.
 */

public class ProjectDb extends SQLiteOpenHelper {
    private final String TABLE_NAME="product";
    private final String KEY_ID="id";
    private final String KEY_NAME="name";
    private final String KEY_PRICE="price";
    private Context main;

    private Product myProduct=new Product();

    ProjectDb dbHelper =this;
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    public ProjectDb(Context context) {
        super(context, "testDB", null, 1);
        main=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table product(" +
                "id integer primary key," +
                "name text," +
                "price text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  ArrayList<Product> getAllProducts(){
        ArrayList<Product> arrayList=new ArrayList<>();
        Product product;
        Cursor c = db.query(TABLE_NAME,
                null,
                null,
                null, null, null, null);
        while (c.moveToNext()){
            product=new Product();
            product.setmId(Integer.parseInt(c.getString(0)));
            product.setmName(c.getString(1));
            product.setmPrice(c.getString(2));
            arrayList.add(product);
        }
        return arrayList;
    }

    public void addToDb(Product product){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,product.getmId());
        cv.put(KEY_NAME,product.getmName());
        cv.put(KEY_PRICE,product.getmPrice());
        if(!productIsSet(product.getmId())){

            long rowID = db.insert(TABLE_NAME, null, cv);
            System.out.println("Запись сделана");
        }else
            if(!myProduct.getmPrice().equals(product.getmPrice())){
                long rowID = db.update(TABLE_NAME,cv,"id=?",new String[]{String.valueOf(product.getmId())});
                System.out.println("Запись сделана");
            }
            else{
                System.out.println("Запись уже сделана");
            }

    }

    public boolean productIsSet(int id){
        Cursor c = db.query(TABLE_NAME,
                null,
                " id = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if(c.getCount()>0) {
            c.moveToFirst();
            myProduct=new Product();
            myProduct.setmId(c.getInt(0));
            myProduct.setmName(c.getString(1));
            myProduct.setmPrice(c.getString(2));
            return true;
        }
        else
            return false;

    }
    public boolean productsIsSet(){
        Cursor c = db.query(TABLE_NAME,
                null,
                null,
                null, null, null, null);
        if(c.getCount()>5)
            return true;
        else
            return false;
    }

    public void updateProduct(Product product){
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME,product.getmName());
        cv.put(KEY_PRICE,product.getmPrice());
        db.update(TABLE_NAME,cv,"id=?",new String[]{String.valueOf(product.getmId())});

    }
}
