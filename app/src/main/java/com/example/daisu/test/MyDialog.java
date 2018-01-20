package com.example.daisu.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by daisu on 18.01.2018.
 */

public class MyDialog  extends DialogFragment {
    private Product product;
    private ListActivity mListActivity;
    private EditText mPriceText;
    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public void setProduct(Product product) {

        this.product = product;
    }

    public void setmListActivity(ListActivity mListActivity) {
        this.mListActivity = mListActivity;
    }

    public static MyDialog getMyDialog(Product product, ListActivity mListActivity, int position){

        MyDialog md=new MyDialog();
        md.setProduct(product);
        md.setmListActivity(mListActivity);
        md.setPosition(position);
        return md;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View linearlayout = getActivity().getLayoutInflater().inflate(R.layout.my_dialog, null);

        mPriceText=(EditText) linearlayout.findViewById(R.id.price_text);
        builder.setView(linearlayout)
                .setTitle(product.getmName())
                .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        product.setmPrice(clear(mPriceText.getText().toString()));
                        mListActivity.setNewProduct(product);
                        mListActivity.updateList(position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyDialog.this.getDialog().cancel();
                    }
                });

        mPriceText.setText(product.getmPrice());
        return builder.create();
    }

    public String clear(String str){
        str=str.replace(" ","");
        if(str.substring(str.length()-1,str.length()).equals("."))
            str=str.replace(".","");

        return str;
    }
}
