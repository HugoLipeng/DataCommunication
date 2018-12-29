package jay.android.com.batterymonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final String TAG = "BatteryMonitor";
    private BatteryInfoReceiver mBatteryReceiver;

    private ProgressBar mBatteryLevel;
    private TextView mBatteryStatus;
    private TextView mBatteryHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBatteryReceiver = new BatteryInfoReceiver();
        mBatteryLevel = (ProgressBar)findViewById(R.id.pb_level);
        mBatteryLevel.setMax(100);
        mBatteryStatus = (TextView)findViewById(R.id.tv_status);
        mBatteryHealth = (TextView)findViewById(R.id.tv_health);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBatteryInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBatteryInfo();
    }

    private void registerBatteryInfo() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, filter);
    }

    private void unregisterBatteryInfo() {
        unregisterReceiver(mBatteryReceiver);
    }

    class BatteryInfoReceiver extends BroadcastReceiver {
        int health;
        int level;
        int status;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == (Intent.ACTION_BATTERY_CHANGED)) {
//                Log.d(TAG, "battery changed action");
                health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD);
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_DISCHARGING);
                updateBatteryView(level, status, health);
            }
        }
    }

    private void updateBatteryView(int level, int status, int health) {
        //更新电池电量
        mBatteryLevel.setProgress(level);
        if(status == BatteryManager.BATTERY_STATUS_CHARGING) {
            mBatteryStatus.setText("电池状态：充电中");
        } else if(status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            mBatteryStatus.setText("电池状态：未充电");
        }

        if(health == BatteryManager.BATTERY_HEALTH_GOOD) {
            mBatteryHealth.setText("电池健康程度：" + "Good");
        } else if(health == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
            mBatteryHealth.setText("电池健康程度：" + "Unknow");
        }
    }
}
