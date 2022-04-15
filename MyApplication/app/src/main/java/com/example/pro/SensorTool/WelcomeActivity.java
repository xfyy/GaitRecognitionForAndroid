package com.example.pro.SensorTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pro.SensorTool.util.SpeakUtil;

/**
 * Created by caizq on 2017-08-26.
 * Welcome Activity show the user attention firstly.
 */

public class WelcomeActivity extends Activity {
    private Button btn;
    private ImageView welcomeImage;
    public static SpeakUtil speakInstance;
    private static final String WELCOME_MSG = "欢迎使用手机传感器数据采集系统";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeImage = (ImageView) findViewById(R.id.image_welcome);
        speakInstance = SpeakUtil.getInsatnce(WelcomeActivity.this);
        speakInstance.speakInit();
        // first show the speaker voice.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);// delay 1s
                    speakInstance.speak(WELCOME_MSG);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // go to MainActivity
        findViewById(R.id.btnEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRotate();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 480L);

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        speakInstance.shutDownSpeakSource();
    }
    public void startRotate(){
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.welcome_image_retote);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if(operatingAnim!=null){
           welcomeImage.startAnimation(operatingAnim);
        }
    }
    public void stopRotate(){
        welcomeImage.clearAnimation();
    }
}
