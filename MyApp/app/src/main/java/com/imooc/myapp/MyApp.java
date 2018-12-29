package com.imooc.myapp;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.squareup.otto.Bus;

public class MyApp extends Application {
    private static final String TAG = "MyApp-app";

    private Bus bus;

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: " + this);
        Log.d(TAG, "onCreate: " + Thread.currentThread());
        bus = new Bus();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged() called with: " + "newConfig = [" + newConfig + "]");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory: ");
    }
}
