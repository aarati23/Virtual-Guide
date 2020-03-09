package com.virtual_guide.virtualguide;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;

public class HomeActivity extends AppCompatActivity {

    Button search, moreImages, seeMap, addInfo, moreInfo;
    TextView title, desc, desc2;
    ImageView img;
    EditText landmark;
    ProgressDialog p;
    String url;
    String place;
    String placeTitle;
    String titles, imgSrc;
    Element link, head, image1, para1, para2;
    InputStream input;
    Bitmap bitmap;
    ScrollView scrollView;
    RelativeLayout relative;
    int flagg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_home);

        landmark = findViewById(R.id.landmark);
        title = findViewById(R.id.title);
        img = findViewById(R.id.img);
        desc = findViewById(R.id.description);
        desc2 = findViewById(R.id.description2);
        search = findViewById(R.id.search);
        scrollView = findViewById(R.id.scrollView);
        relative = findViewById(R.id.buttons);
        moreImages = findViewById(R.id.butt1);
        seeMap = findViewById(R.id.butt2);
        moreInfo = findViewById(R.id.butt3);
        addInfo = findViewById(R.id.butt4);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scrollView.setBackgroundResource(R.drawable.back3);
                place = landmark.getText().toString();
                url = "https://en.wikipedia.org/wiki/" + place;
                Content content = new Content();
                content.execute();
            }
        });

        try{
            Intent intent = getIntent();
            String titleCamera = intent.getStringExtra("titleCamera");
            if(!titleCamera.isEmpty()){
                scrollView.setBackgroundResource(R.drawable.back3);
                place = titleCamera;
                url = "https://en.wikipedia.org/wiki/" + place;
                Content content = new Content();
                content.execute();
            }
        }
        catch(Exception e){
            Log.d("^&%^&%Null",e.toString());
        }



        moreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moreImages = new Intent(HomeActivity.this, MoreImages.class);
                moreImages.putExtra("Placename", placeTitle);
                startActivity(moreImages);
            }
        });

        addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String user = intent.getStringExtra("user");
                String id = intent.getStringExtra("id");
                Intent intoAddInfo = new Intent(HomeActivity.this, AddInfo.class);
                intoAddInfo.putExtra("user", user);
                intoAddInfo.putExtra("id", id);
                intoAddInfo.putExtra("landmark", placeTitle);
                startActivity(intoAddInfo);
            }
        });

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String user = intent.getStringExtra("user");
                String id = intent.getStringExtra("id");
                Intent intoMoreInfo = new Intent(HomeActivity.this, ViewInfo.class);
                intoMoreInfo.putExtra("user", user);
                intoMoreInfo.putExtra("id", id);
                intoMoreInfo.putExtra("landmark", placeTitle);
                startActivity(intoMoreInfo);
            }
        });

//        seeMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intoStorage = new Intent(HomeActivity.this, Storage.class);
//                startActivity(intoStorage);
//            }
//        });


    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(HomeActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            p.setMessage("Please wait...Fetching details");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        protected Void doInBackground(Void... voids) {
            int i = 1;
            try {
                Document document = Jsoup.connect(url).get();
                titles = document.title();
                head = document.select("h1").first();
                placeTitle = head.text();
                while(i < 10){
                    if(document.select("p").get(i).text().isEmpty()){
                        i++;
                    }
                    else{
                        break;
                    }
                }
                para1 = document.select("p").get(i);
                para2 = document.select("p").get(i + 1);
                link = document.select("table").first();
                image1 = link.select("img").first();
                imgSrc = image1.absUrl("src");
                input = new java.net.URL(imgSrc).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            title.setText(placeTitle);
            img.setImageBitmap(bitmap);
            desc.setText(para1.text());
            desc2.setText(para2.text());
            p.dismiss();
            relative.setVisibility(View.VISIBLE);
        }
    }

}