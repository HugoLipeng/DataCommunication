package com.example.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.servicedemo.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                imai.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operate(View v){
        switch(v.getId()){
            case R.id.start:
                //远程启动服务
                Intent it = new Intent();
                it.setAction("com.imooc.myservice");
                it.setPackage("com.example.servicedemo");
                startService(it);
                break;
            case R.id.stop:
                Intent it2 = new Intent();
                it2.setAction("com.imooc.myservice");
                it2.setPackage("com.example.servicedemo");
                stopService(it2);
                break;
            case R.id.bind:
                Intent it3 = new Intent();
                it3.setAction("com.imooc.myservice");
                it3.setPackage("com.example.servicedemo");
                bindService(it3,conn,BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                unbindService(conn);
                break;

        }
    }
}
