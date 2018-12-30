package com.imooc.glidetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView mGlideTestIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGlideTestIV = (ImageView) findViewById(R.id.iv_glide);
    }

    public void listTest(View view) {
        startActivity(new Intent(this, ListDemoActivity.class));
    }

    public void load(View view) {
        // setup Glide request without the into() method
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load("http://img.mukewang.com/5518c3d7000175af06000338-300-170.jpg");
        // pass the request as a a parameter to the thumbnail request
        Glide.with(this)
                .load("http://img.mukewang.com/5518c3d7000175af06000338.jpg")
                .thumbnail(thumbnailRequest)
                .into(mGlideTestIV);
    }
}