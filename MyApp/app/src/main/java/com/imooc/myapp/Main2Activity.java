package com.imooc.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity-app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate: " + getApplication() + ", " + this);
        setTitle("Main2Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + BusProvider.getBus());
        BusProvider.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + BusProvider.getBus());
        BusProvider.getBus().unregister(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetUserName: {
                MyApp app = (MyApp) getApplication();
                Toast.makeText(Main2Activity.this, app.getUserName(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
