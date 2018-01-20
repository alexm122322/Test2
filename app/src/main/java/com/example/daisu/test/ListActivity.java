package com.example.daisu.test;


import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    ProjectDb projectDb;
    ArrayList<Product> array=new ArrayList<>();
    Product product=new Product();

    public void setNewProduct(Product product) {
        this.product=product;
    }

    public void updateList(int position){
        array.set(position,product);
        mAdapter.setmDataset(array);
        mAdapter.notifyItemChanged(position);
        projectDb.updateProduct(product);

    }

    private RecyclerView.LayoutManager mLayoutManager;
    private MyRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        projectDb=new ProjectDb(this);

        mRecyclerView=(RecyclerView)findViewById(R.id.my_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        array=getData();
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new MyRecyclerAdapter(array,this);
        RecyclerViewClickListener listener = (view, position) -> {
            //Toast.makeText(this, "Position " + position, Toast.LENGTH_SHORT).show();
            DialogFragment newFragment = MyDialog.getMyDialog(array.get(position),this,position);
            newFragment.show(getFragmentManager(),"missiles");

        };

        mAdapter.setmListener(listener);
        mRecyclerView.setAdapter(mAdapter);


    }
    public ArrayList<Product> getData(){

        return  projectDb.getAllProducts();
    }
}
