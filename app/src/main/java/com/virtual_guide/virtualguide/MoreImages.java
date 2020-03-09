package com.virtual_guide.virtualguide;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;

public class MoreImages extends AppCompatActivity {
//
    ImageView[] img;
    ProgressDialog p;
    String url;
    String[] imgSrc;
    Element link, para1, para2;
    Element[] image1;
    InputStream[] input;
    Bitmap[] bitmap;
    ScrollView scrollView;
//
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.moreimages);

        image1 = new Element[40];
        imgSrc = new String[40];
        input = new InputStream[40];
        bitmap = new Bitmap[40];
        img = new ImageView[40];

        for(int i = 0; i< 20; i++){
            String imeg = "img" + (i+1);
            int id = getResources().getIdentifier(imeg, "id", getPackageName());
            img[i] = findViewById(id);
        }

        scrollView = findViewById(R.id.scrollView);

//        Intent intent = getIntent();
//        String str = intent.getStringExtra("Placename");

        scrollView.setBackgroundResource(R.drawable.back3);
        url = "https://www.shutterstock.com/search/" + "taj mahal";
        MoreImages.Content content = new MoreImages.Content();
        content.execute();

    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MoreImages.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            p.setMessage("Please wait...Fetching details");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        protected Void doInBackground(Void... voids) {
            int i = 0;
            try {
                Document document = Jsoup.connect(url).get();

                for(int j = 10; j < 50; j++){
                    try{
                        image1[i] = document.select("img").get(j);
                        imgSrc[i] = image1[i].absUrl("src");
                        input[i] = new java.net.URL(imgSrc[i]).openStream();
                        bitmap[i] = BitmapFactory.decodeStream(input[i]);
                        if(i < 3){
                            i++;
                        }
                        else{
                            break;
                        }
                    }
                    catch (Exception e){
                        System.out.println("********imageerror***********");
                        i--;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int k = 0;
            int i  = 0;
            while(i < 20) {
                try {
                    img[i].setImageBitmap(bitmap[k]);
                    i++;
                }
                catch(Exception e){

                }
                k++;
            }

            p.dismiss();
        }
    }

}
