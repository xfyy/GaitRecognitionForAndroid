package com.example.pro.collectsensor;

import java.util.HashMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

// suppresses the leak warnings since there are no long delayed messages
@SuppressLint("HandlerLeak")

public class MainActivity extends Activity {
    public final static int MSG_SENSOR_UPDATE = 0;
    private SensorManager mSensorManager;
    private SensorLogger mSensorLogger;
    private UIUpdater mUIUpdater;

    // Handles messages from the ui updater to update the UI
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SENSOR_UPDATE:
                    mUIUpdater.updateValues((HashMap<SensorID, Float>) msg.obj);
                    break;
                default:
                    Log.e("MainActivity", "handleMessage received an invalid msg.what");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("HandlerLeak")
    private void init() {
        initSensorLogger();
        mUIUpdater = new UIUpdater(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keep the screen on
    }

    private void initSensorLogger() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		PermisionUtils p = new PermisionUtils();
		p.verifyStoragePermissions(this);
        mSensorLogger = SensorLogger.getInstance(mSensorManager, mHandler);
    }

    public SensorLogger getSensorLogger() {
        return mSensorLogger;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the u
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
