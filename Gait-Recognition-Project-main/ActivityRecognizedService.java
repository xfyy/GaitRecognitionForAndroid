package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;


import java.util.List;

public class ActivityRecognizedService extends IntentService {
    private static final String TAG = "ActivityRecognizedService";
    public static int I = 0;
    //Call the super IntentService constructor with the name for the worker thread//
    public ActivityRecognizedService() {
        super(TAG);

    }
    public ActivityRecognizedService(String name) {
        super(name);
    }
    /*@Override
    public void onCreate() {
        super.onCreate();
    }*/

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivity(result.getProbableActivities());

        }

    }

    @SuppressLint("LongLogTag")
    private void handleDetectedActivity(List<DetectedActivity> probableActivities) {
        for (DetectedActivity activity: probableActivities){
            switch(activity.getType()){
                case DetectedActivity.IN_VEHICLE:{
                    Log.e(TAG, "handleDetectedActivity: IN_VEHICLE "+ activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_BICYCLE:{
                    Log.e(TAG, "handleDetectedActivity: ON_BICYCLE "+ activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_FOOT:{
                    Log.e(TAG, "handleDetectedActivity: ON_FOOT "+ activity.getConfidence());
                    if( activity.getConfidence() >= 50 ) {
                        I = 3;
                    }
                    break;
                }
                case DetectedActivity.RUNNING:{
                    Log.e(TAG, "handleDetectedActivity: RUNNING "+ activity.getConfidence());
                    break;
                }
                case DetectedActivity.STILL:{
                    Log.e(TAG, "handleDetectedActivity: STILL "+ activity.getConfidence());
                    if( activity.getConfidence() >= 75 ) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you still?");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }
                    if( activity.getConfidence() >= 75 ) {
                        I = 1;
                    }
                    break;
                }
                case DetectedActivity.WALKING:{
                    Log.e(TAG, "handleDetectedActivity: WALKING "+ activity.getConfidence());
                    if( activity.getConfidence() >= 50 ) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you walking?");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }
                    if( activity.getConfidence() >= 50 ) {
                        I = 2;
                    }
                        break;

                }
                case DetectedActivity.TILTING:{
                    Log.e(TAG, "handleDetectedActivity: TILTING "+ activity.getConfidence());
                    break;
                }
                case DetectedActivity.UNKNOWN:{
                    Log.e(TAG, "handleDetectedActivity: UNKNOWN "+ activity.getConfidence());
                    if( activity.getConfidence() >= 40 ) {
                        I = 5;
                    }
                    break;
                }

            }
        }
    }

}
