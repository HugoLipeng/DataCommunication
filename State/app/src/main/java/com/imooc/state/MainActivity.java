package com.imooc.state;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity-vv";
    public static final String CREATE_TIME = "createTime";

    private long createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            createTime = savedInstanceState.getLong(CREATE_TIME);
        } else {
            createTime = System.currentTimeMillis();
        }

        String formatTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(this.createTime));
        TextView tvCreateTime = (TextView) findViewById(R.id.tvCreateTime);
        tvCreateTime.setText(formatTime);

        Log.d(TAG, "onCreate: " + this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = [" + outState + "]");
        outState.putLong(CREATE_TIME, this.createTime);
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = [" + outState + "]");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + this);
    }
}
