package com.example.aidldemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneReceiver extends BroadcastReceiver {
    public PhoneReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e("TAG","来电话了");
        //1.获取电话服务
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //2.读取状态
        switch(manager.getCallState()){
            case TelephonyManager.CALL_STATE_RINGING:
                //响铃
                String number = intent.getStringExtra("incoming_number");
                Log.e("TAG","响铃" + number);

                //ITelephony
                //ITelephony it = new I
                //endCall();

                // private ITelephony getITelephony()
                //反射
                //1.获取TelephonyManager类对象
                Class cls = TelephonyManager.class;

                //2.根据方法名获取到对应的方法
                try {
                    Method m = cls.getDeclaredMethod("getITelephony");
                    //3.设置方法可以被操作，执行方法，获取Itelephony返回值
                    m.setAccessible(true);
                    ITelephony it = (ITelephony) m.invoke(manager);
                    //4.调用endCall方法
                    it.endCall();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //摘机
                Log.e("TAG","通过中");
                break;
            case  TelephonyManager.CALL_STATE_IDLE:
                //闲置
                break;
        }
    }
}
