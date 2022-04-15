package com.example.pro.SensorTool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.pro.SensorTool.Config.SystemSampleParametersConfig;
import com.example.pro.SensorTool.util.CurrentDateTimeInstnce;
import com.example.pro.SensorTool.util.FileHelper;

import java.io.IOException;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity implements ScreenListener.ScreenStateListener, View.OnClickListener {
    private TextToSpeech textToSpeech;
    private ScreenListener screenListener;
    private TextView acc_x;
    private TextView acc_y;
    private TextView acc_z, gra_x, gra_y, gra_z, gyr_x, gyr_y, gyr_z;
    private SensorManager sensorManager = null;
    private Sensor accelerationSensor = null;//accleration
    private Sensor gravitySensor = null;//gravity
    private Sensor gyroScopeSensor = null;//gyr
    private float[] accValue = new float[3];
    private FileHelper fileHelper;//文件操作辅助类
    private CurrentDateTimeInstnce currentDateTimeInstance;//获取当前时间
    private StringBuffer bufferGrivaty = new StringBuffer();//线程安全 对字符串进行修改 不产生新的对象
    private StringBuffer bufferAcceleration = new StringBuffer();
    private StringBuffer bufferGyroscope = new StringBuffer();
    private int index0 = 0, index1 = 0, index2 = 0;//记录的每个传感器里面的x,y,z轴的值
    private Button btnRun, btnSit, btnBicycle, btnUpstaris, btnDownStari, btnWalk;//记录人的状态 走路、跑步
    private int count = 5;//设置的倒计时
    private volatile String mode = "walking";//vilatile 确保不同线程下的即时更新

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakInit();
        viewInit();
        verifyStoragePermissions(this);
        currentDateTimeInstance = CurrentDateTimeInstnce.getInstnce();
        fileHelper = new FileHelper(getApplicationContext());
//        Toast.makeText(this,fileHelper.getFILESPATH(),Toast.LENGTH_LONG);
        findViewById(R.id.btnSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak("开始采集数据", TextToSpeech.QUEUE_ADD, null);
                registerSensor();

            }
        });
        findViewById(R.id.stopSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak("停止数据采集", TextToSpeech.QUEUE_ADD, null);
                unRegisterSensor();
            }
        });

    }

    /*
     * android 动态权限申请
     * */
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
//            int permission1 = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.READ_EXTERNAL_STORAGE");
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // textToSpeech tool init
    private void speakInit() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.CHINESE);
            }
        });
    }

    // shut down resource
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }

    // view init
    private void viewInit() {

        btnRun = (Button) findViewById(R.id.btnRun);
        btnBicycle = (Button) findViewById(R.id.btnBicycle);
        btnDownStari = (Button) findViewById(R.id.btnDownStrais);
        btnUpstaris = (Button) findViewById(R.id.btnUpdtaris);
        btnWalk = (Button) findViewById(R.id.btnWalk);
        btnSit = (Button) findViewById(R.id.btnSit);

        btnDownStari.setOnClickListener(this);
        btnBicycle.setOnClickListener(this);
        btnUpstaris.setOnClickListener(this);
        btnWalk.setOnClickListener(this);
        btnSit.setOnClickListener(this);
        btnRun.setOnClickListener(this);


        acc_x = (TextView) findViewById(R.id.acc_x);
        acc_y = (TextView) findViewById(R.id.acc_y);
        acc_z = (TextView) findViewById(R.id.acc_z);
        gra_x = (TextView) findViewById(R.id.gra_x);
        gra_y = (TextView) findViewById(R.id.gra_y);
        gra_z = (TextView) findViewById(R.id.gra_z);
        gyr_x = (TextView) findViewById(R.id.gyr_x);
        gyr_y = (TextView) findViewById(R.id.gyr_y);
        gyr_z = (TextView) findViewById(R.id.gyr_z);
        screenListener = new ScreenListener(this);
        screenListener.start(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroScopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    /* @Override
     protected void onResume() {
         super.onResume();
         sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 10000);
         sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 10000);
         sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),10000);
     }*/
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            accValue[0] = sensorEvent.values[0];
            accValue[1] = sensorEvent.values[1];
            accValue[2] = sensorEvent.values[2];
            String locationX = String.valueOf(accValue[0]);
            String locationY = String.valueOf(accValue[1]);
            String locationZ = String.valueOf(accValue[2]);
            String splitLine = "\n";
            synchronized (this) {
                switch (sensorEvent.sensor.getType()) {
                    case Sensor.TYPE_GRAVITY:
                        gra_x.setText(String.valueOf(accValue[0]));
                        gra_y.setText(String.valueOf(accValue[1]));
                        gra_z.setText(String.valueOf(accValue[2]));
                        bufferGrivaty.append(String.valueOf(System.currentTimeMillis()));
                        bufferGrivaty.append(" ");
                        bufferGrivaty.append(locationX);
                        bufferGrivaty.append(" ");
                        bufferGrivaty.append(locationY);
                        bufferGrivaty.append(" ");
                        bufferGrivaty.append(locationZ);
                        bufferGrivaty.append(splitLine);
                        index0++;
                        if (index0 == SystemSampleParametersConfig.SAMPLE_lENGTH) {
                            String folderName1 = "Gravity" + mode + currentDateTimeInstance.formatDateTime() + ".txt";
                            try {
                                fileHelper.createSDFile(folderName1).getAbsoluteFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fileHelper.writeSDFile(bufferGrivaty.toString(), folderName1);
                            bufferGrivaty.delete(0, bufferGrivaty.length());
                            if (hasAllWriteOver(index0, index1, index2, SystemSampleParametersConfig.SAMPLE_lENGTH)) {
                                unRegisterSensor();
                                index0 = 0;
                                index1 = 0;
                                index2 = 0;
                                textToSpeech.speak(mode + "数据采集已结束，正在保存数据，请稍后", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        acc_x.setText(String.valueOf(accValue[0]));
                        acc_y.setText(String.valueOf(accValue[1]));
                        acc_z.setText(String.valueOf(accValue[2]));
                        bufferAcceleration.append(String.valueOf(System.currentTimeMillis()));
                        bufferAcceleration.append(" ");
                        bufferAcceleration.append(locationX);
                        bufferAcceleration.append(" ");
                        bufferAcceleration.append(locationY);
                        bufferAcceleration.append(" ");
                        bufferAcceleration.append(locationZ);
                        bufferAcceleration.append(splitLine);
                        index1++;
                        if (index1 == SystemSampleParametersConfig.SAMPLE_lENGTH) {
                            String folderName2 = "Accelreation" + mode + currentDateTimeInstance.formatDateTime() + ".txt";
                            try {
                                fileHelper.createSDFile(folderName2).getAbsoluteFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fileHelper.writeSDFile(bufferAcceleration.toString(), folderName2);
                            bufferAcceleration.delete(0, bufferAcceleration.length());
                            if (hasAllWriteOver(index0, index1, index2, SystemSampleParametersConfig.SAMPLE_lENGTH)) {
                                unRegisterSensor();
                                index0 = 0;
                                index1 = 0;
                                index2 = 0;
                                textToSpeech.speak(mode + "数据采集已结束，正在保存数据，请稍后", TextToSpeech.QUEUE_ADD, null);
                            }

                        }
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        gyr_x.setText(String.valueOf(accValue[0]));
                        gyr_y.setText(String.valueOf(accValue[1]));
                        gyr_z.setText(String.valueOf(accValue[2]));
                        bufferGyroscope.append(String.valueOf(System.currentTimeMillis()));
                        bufferGyroscope.append(" ");
                        bufferGyroscope.append(locationX);
                        bufferGyroscope.append(" ");
                        bufferGyroscope.append(locationY);
                        bufferGyroscope.append(" ");
                        bufferGyroscope.append(locationZ);
                        bufferGyroscope.append(splitLine);
                        index2++;
                        if (index2 == SystemSampleParametersConfig.SAMPLE_lENGTH) {
                            String folderName3 = "Gyroscope" + mode + currentDateTimeInstance.formatDateTime() + ".txt";
                            try {
                                fileHelper.createSDFile(folderName3).getAbsoluteFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fileHelper.writeSDFile(bufferGyroscope.toString(), folderName3);
                            bufferGyroscope.delete(0, bufferGyroscope.length());
                            if (hasAllWriteOver(index0, index1, index2, SystemSampleParametersConfig.SAMPLE_lENGTH)) {
                                unRegisterSensor();
                                index0 = 0;
                                index1 = 0;
                                index2 = 0;
                                textToSpeech.speak(mode + "数据采集已结束，正在保存数据，请稍后", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                        break;
                }

            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterSensor();
    }

    @Override
    public void onScreenOn() {

    }

    @Override
    public void onScreenOff() {
        // register again
        sensorManager.unregisterListener(listener);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 20000);//100hz
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 20000);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 20000);

    }

    @Override
    public void onUserPresent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRun:
                mode = "Run";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("run go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }).start();

                break;
            case R.id.btnSit:
                mode = "Sit";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("sit go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();

                break;
            case R.id.btnUpdtaris:
                mode = "Upstrirs";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("up stairs go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();


                break;
            case R.id.btnDownStrais:
                mode = "DownStrais";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("down stairs go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();


                break;
            case R.id.btnWalk:
                mode = "Walk";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("walk go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();


                break;
            case R.id.btnBicycle:
                mode = "Bicycle";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {

                                if (count == 5) {
                                    textToSpeech.speak("我即将开始采集数据了", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(500);
                                    textToSpeech.speak("请做好准备", TextToSpeech.QUEUE_ADD, null);
                                    Thread.sleep(3000);
                                    textToSpeech.speak(String.valueOf(5), TextToSpeech.QUEUE_ADD, null);
                                }
                                Thread.sleep(1200);
                                textToSpeech.speak(String.valueOf(--count), TextToSpeech.QUEUE_ADD, null);
                                if (count == 0) {
                                    count = 5;
                                    Thread.sleep(600);
                                    textToSpeech.speak("bicycle go", TextToSpeech.QUEUE_ADD, null);
                                    registerSensor();
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();

                break;
            default:
                break;

        }
    }

    private void registerSensor() {
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 20000);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 20000);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 20000);
    }

    private void unRegisterSensor() {

        sensorManager.unregisterListener(listener);
    }

    private boolean hasAllWriteOver(int index0, int index1, int index2, int samplePoint) {
        if (index0 >= samplePoint && index2 >= samplePoint && index1 >= samplePoint) {
            return true;
        }
        return false;
    }
}
