package com.imooc.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Bus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity-app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + getApplication());
        setTitle("MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp app = (MyApp) getApplication();
        Bus bus = app.getBus();
        Log.d(TAG, "onResume: " + bus);
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp app = (MyApp) getApplication();
        Bus bus = app.getBus();
        Log.d(TAG, "onPause: " + bus);
        bus.unregister(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGotoActivity2:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.btnStartService:
                startService(new Intent(this, MyService.class));
                break;
            case R.id.btnSetUserName: {
                MyApp app = (MyApp) getApplication();
                app.setUserName("iMooc");
                Toast.makeText(MainActivity.this, "Set user name to be " + app.getUserName(), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnGetUserName: {
                MyApp app = (MyApp) getApplication();
                Toast.makeText(MainActivity.this, app.getUserName(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
