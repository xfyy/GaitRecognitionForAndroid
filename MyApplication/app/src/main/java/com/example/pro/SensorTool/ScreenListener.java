package com.example.pro.SensorTool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * the class ScreenListener is used to monitoring the secreen status.
 */
public class ScreenListener {
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    // start monitoring the screen ...
    public void start(ScreenStateListener listener) {
        mScreenStateListener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
        initScreenState();
    }

    private void initScreenState() {
        if (mScreenStateListener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            mScreenStateListener.onScreenOn();
        } else {
            mScreenStateListener.onScreenOff();
        }
    }
    // stop the monitor action.
    public void stop() {
        mContext.unregisterReceiver(mScreenReceiver);
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            // open the screen
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {// lock the screen
                mScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {// solve the lock
                mScreenStateListener.onUserPresent();
            }
        }

    }
     // call back interface
    public interface ScreenStateListener {

        void onScreenOn();

        void onScreenOff();

        void onUserPresent();

    }

}

