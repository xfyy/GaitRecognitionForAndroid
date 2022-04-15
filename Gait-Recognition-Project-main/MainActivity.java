package com.example.myapplication;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Accuracy.A;
import static com.example.myapplication.ActivityRecognizedService.I;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

//import static com.example.myapplication.MainActivityT.L1;
//import static com.example.myapplication.MainActivityT.L4;
//import static com.example.myapplication.MainActivityT.LS5;
import static com.example.myapplication.MainActivity2.L1;
//import static com.example.myapplication.MainActivity2.L3;
import static com.example.myapplication.MainActivity2.L4;
import static com.example.myapplication.MainActivity2.LS5;
//import static com.example.myapplication.MainActivity3.L1;
//import static com.example.myapplication.MainActivity3.L4;
//import static com.example.myapplication.MainActivity3.LS5;
//import static com.example.myapplication.MainActivity3.L3;
public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final static int SEND_SMS_PERMISSION_REQ=1;
    private static final String SMS_SENT_INTENT_FILTER = "com.example.myapplication.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.example.myapplication.sms_delivered";
    public GoogleApiClient mApiClient;
    private TextView xText, yText, zText, aText, textViewLongitude, textViewLatitude;
    private LocationRequest request;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CODE = 8990;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    String Longitude;
    String Latitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static Sensor mySensor;
    private static SensorManager SM;
    private Button start;
    public static int Indicator = 0;
    public static List<Float> Xdata2 = new ArrayList<>();
    public static List<Float> Ydata2 = new ArrayList<>();
    public static List<Float> Zdata2 = new ArrayList<>();
    public static List<Float> Xdata3 = new ArrayList<>();
    public static List<Float> Ydata3 = new ArrayList<>();
    public static List<Float> Zdata3 = new ArrayList<>();

    //private List<Float> AT1 = new ArrayList<Float>();

    //List<Float> AT = new ArrayList<Float>();
    int PI = 0;
    int y = 0;
    //MainActivityT T = new MainActivityT();
    MainActivity2 P = new MainActivity2();
   //MainActivity3 S = new MainActivity3();
    Accuracy Ac = new Accuracy();


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewLatitude = findViewById(R.id.latitude);
        textViewLongitude = findViewById(R.id.longitude);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest()
                .setFastestInterval(300)
                .setInterval(300)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder().addLocationRequest(request);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        start = (Button) findViewById(R.id.buttonStart);
        start.setOnClickListener(this);

        // Create our Sensor Manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign TextView
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        aText = (TextView) findViewById(R.id.aText);
        Intent serviceIntent = new Intent(this, ExampleService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
            Intent mintent = new Intent(MainActivity.this, ActivityRecognizedService.class);
            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                PackageManager.PERMISSION_GRANTED) {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 5000, pendingIntent);
        }*/
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 3000, pendingIntent);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        mApiClient.connect();
        if((PI == 1) )   {
                if (((sqrt(pow(((double) (event.values[0])), 2) + pow(((double) (event.values[1])), 2) + pow(((double) (event.values[2])), 2))) >= 9) && (Xdata2.size()<100) && (I == 2) ) {
               /* try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
                    Xdata2.add(event.values[0]);
                    Ydata2.add(event.values[1]);
                    Zdata2.add(event.values[2]);
                    xText.setText("X: " + event.values[0]);
                    yText.setText("Y: " + event.values[1]);
                    zText.setText("Z: " + event.values[2]);
                }
               else if (((sqrt(pow(((double) (event.values[0])), 2) + pow(((double) (event.values[1])), 2) + pow(((double) (event.values[2])), 2))) >= 9) &&  (Xdata2.size() == 100)){
                    textViewLatitude.setText("Stop");
                    PI = 0;
                    Indicator = 2;
                    TThread threadT = new TThread(Indicator);
                    threadT.start();
                }
            }
            else if ((I == 2) && ((Xdata2.size() == 100)) && (PI == 0) && (Xdata2.size()*3 > Xdata3.size())) {
                if ((sqrt(pow(((double) (event.values[0])), 2) + pow(((double) (event.values[1])), 2) + pow(((double) (event.values[2])), 2))) >= 9) {
                    textViewLongitude.setText("Tab");
                    Xdata3.add(event.values[0]);
                    Ydata3.add(event.values[1]);
                    Zdata3.add(event.values[2]);
                    xText.setText("X: " + event.values[0]);
                    yText.setText("Y: " + event.values[1]);
                    zText.setText("Z: " + event.values[2]);
                }
            }


            else if ((I == 2) && ((Xdata2.size() == 100)) && (PI == 0)  && (Xdata2.size()*3 == Xdata3.size())) {
                F(Xdata2, Xdata3);
                F(Ydata2, Ydata3);
                F(Zdata2, Zdata3);
                textViewLongitude.setText("Recog");
                Indicator = 3;
                PI = 3;
                TThread threadT = new TThread(Indicator);
                threadT.start();
                try {
                    threadT.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                AccuThread threadA = new AccuThread(L1, L4, LS5);
                threadA.start();
                try {
                    threadA.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d("MainActivity", "onSensorChanged: " + "Full Accuracy: " + A);
                if (A.get(0) < 65) {
                    y = y +1;
                    //sendSMS();
               }
            /*if (y == 3){
                sendSMS();
                y = 0;
            }*/
                textViewLatitude.setText("Similarity: " + A + "%");


        } else if (I == 1) {
                textViewLongitude.setText("? ");
                xText.setText("X: ");
                yText.setText("Y: ");
                zText.setText("Z: ");
                Xdata3.clear();
                Ydata3.clear();
                Zdata3.clear();
                PI = 0;

            }

            message();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == start) {
            Xdata2.clear();
            Xdata3.clear();
            Ydata2.clear();
            Ydata3.clear();
            Zdata2.clear();
            Zdata3.clear();
            Indicator = 1;
            PI = 1;
            /*try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            textViewLatitude.setText("START WALKING");
            TThread threadT = new TThread(Indicator);
            threadT.start();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Longitude = String.valueOf(location.getLongitude());
        Latitude = String.valueOf(location.getLatitude());
        //textViewLongitude.setText("Long"+location.getLongitude());
        //textViewLatitude.setText("Lat"+location.getLatitude());
        /*if (y == 2){
            sendSMS();
            y = 0;
        }*/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude","disable");
    }


    public class AccuThread extends Thread { //memory
        List<String> X;
        List<ArrayList<String>> Y;
        List<ArrayList<Integer>> Z;

        AccuThread(List<String> X, List<ArrayList<String>> Y, List<ArrayList<Integer>> Z) {
            this.X = X;
            this.Y = Y;
            this.Z = Z;
        }

        @Override
        public void run() {
            Ac.Accu(X, Y, Z);
        }
    }

    public class TThread extends Thread { //memory
        int In;

        TThread(int In) {

            this.In = In;
        }

        @Override
        public void run() {
            //T.Read(In);
            P.Read(In);
            //S.Read(In);
        }
    }

    public void message() {
        if (I == 1) {
            aText.setText("Standing Still");
        } else if (I == 2) {
            aText.setText("Walking?");
        } else if ((I == 0)) {
            aText.setText("Null");
        }
    }


    protected void sendSMS() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        String message = "Lat: " + Latitude+ " "+ "Long " + Longitude;
        String phnNo = " "; //set location tracker phone number
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_SENT_INTENT_FILTER), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SMS_DELIVERED_INTENT_FILTER), 0);
        SmsManager sms = SmsManager.getDefault();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
        sms.sendTextMessage(phnNo, null, message, sentPI, deliveredPI);
    }
    }
    public static double d(float[] x1, float[] x2) {
        int n1 = x1.length;
        int n2 = x2.length;
        double[][] table = new double[2][n2 + 1];

        table[0][0] = 0;

        for (int i = 1; i <= n2; i++) {
            table[0][i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 1; i <= n1; i++) {
            table[1][0] = Double.POSITIVE_INFINITY;

            for (int j = 1; j <= n2; j++) {
                double cost = Math.abs(x1[i-1] - x2[j-1]);

                double min = table[0][j - 1];

                if (min > table[0][j]) {
                    min = table[0][j];
                }

                if (min > table[1][j - 1]) {
                    min = table[1][j - 1];
                }

                table[1][j] = cost + min;
            }

            double[] swap = table[0];
            table[0] = table[1];
            table[1] = swap;
        }
        Log.d("MainActivity", "onSensorChanged: " + "DTW " + table[0][n2]);
        return table[0][n2];

    }
    public void F (List<Float> X2w, List<Float> X3w) {
        float[] X2 = new float[X2w.size()];
        for (int i =0; i < X2w.size(); i++) {
            X2[i] = X2w.get(i);
        }
        float[] X3 = new float[X3w.size()];
        for (int i =0; i < X3w.size(); i++) {
            X3[i] = X3w.get(i);
        }
        d(X2, X3);
    }
}



