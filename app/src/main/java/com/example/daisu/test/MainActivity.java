package com.example.daisu.test;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mDownload_btn, mList_btn;
    ProjectDb projectDb;

    public Button getmList_btn() {
        return mList_btn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        projectDb=new ProjectDb(this);
        mDownload_btn=(Button)findViewById(R.id.download_btn);
        mDownload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList_btn.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.download_xml, Toast.LENGTH_SHORT).show();
                OkHttpHandler okHttpHandler= new OkHttpHandler(MainActivity.this,projectDb);
                okHttpHandler.execute();
            }
        });

        mList_btn=(Button)findViewById(R.id.list_btn);
        if(!projectDb.productsIsSet())
            mList_btn.setVisibility(View.GONE);
        mList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListActivity.class) ;
                startActivity(intent);
            }
        });
    }
}

class OkHttpHandler extends AsyncTask<Void,String,String> {
    private Product product=new Product();
    private ArrayList<Product> array=new ArrayList<>();
    private String resp="";
    private MainActivity main;
    private ProjectDb projectDb;
    private boolean internetError=false;

    OkHttpClient client = new OkHttpClient();

    public OkHttpHandler(MainActivity main, ProjectDb projectDb) {
        this.main = main;
        this.projectDb=projectDb;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(internetError){
            Toast.makeText(main, R.string.connect_error, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();


            DefaultHandler handler = new DefaultHandler(){
                private boolean id = false;
                private boolean name = false;
                private boolean price = false;

                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("id")) {
                        id = true;
                    }
                    if (qName.equalsIgnoreCase("name")) {
                        name = true;
                    }
                    if (qName.equalsIgnoreCase("price")) {
                        price = true;
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (id) {
                        product.setmId(Integer.parseInt(new String(ch, start, length)));
                        System.out.println("\n\n id : " + new String(ch, start, length));
                        id = false;
                    }
                    if (name) {
                        product.setmName(new String(ch, start, length));
                        System.out.println("\n\n Name : " + new String(ch, start, length));
                        name = false;
                    }
                    if (price) {
                        product.setmPrice(new String(ch, start, length));
                        System.out.println("\n Salary : " + new String(ch, start, length));
                        price = false;

                        array.add(product);
                        product=new Product();
                    }
                }
            };
            InputSource source = new InputSource(new StringReader(s));
            saxParser.parse(source,handler);
            addProductToDb(array);

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }

        @Override
    protected String doInBackground(Void... voids) {

        Request.Builder builder = new Request.Builder();
        builder.url("http://ainsoft.pro/test/test.xml");
        Request request = builder.build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                resp=response.body().string();
            } catch (IOException e) {
               internetError=true;
            }
        return resp;
    }

    private void addProductToDb(ArrayList<Product> array){
        for(Product pr:array){
            projectDb.addToDb(pr);
        }
        Toast.makeText(main, R.string.download_xml_completed, Toast.LENGTH_SHORT).show();
        main.getmList_btn().setVisibility(View.VISIBLE);
    }
}






