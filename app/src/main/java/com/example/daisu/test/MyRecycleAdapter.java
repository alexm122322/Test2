package com.example.daisu.test;

import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by daisu on 21.11.2017.
 */

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private ArrayList<Product> mDataset;
    ListActivity listActivity;
    private RecyclerViewClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNameView, mPriceView;
        private RecyclerViewClickListener mListener;
        public ViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mNameView = (TextView) v.findViewById(R.id.name_text);
            mPriceView = (TextView) v.findViewById(R.id.price_text);
            mListener = listener;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }

    }

    public MyRecyclerAdapter(ArrayList<Product> mDataset,ListActivity listActivity) {
        this.mDataset = mDataset;
        this.listActivity=listActivity;

    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cards,parent,false);
        ViewHolder vh=new ViewHolder(v,mListener);
        return vh;
    }

    public void setmDataset(ArrayList<Product> mDataset) {
        this.mDataset = mDataset;
    }

    public void setmListener(RecyclerViewClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.mNameView.setText(mDataset.get(position).getmName());
        holder.mPriceView.setText("Price:"+mDataset.get(position).getmPrice());

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

interface RecyclerViewClickListener {

    void onClick(View view, int position);
}
