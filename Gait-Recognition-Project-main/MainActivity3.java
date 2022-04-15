package com.example.myapplication;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.myapplication.MainActivity.Xdata2;
import static com.example.myapplication.MainActivity.Xdata3;
import static com.example.myapplication.MainActivity.Ydata2;
import static com.example.myapplication.MainActivity.Ydata3;
import static com.example.myapplication.MainActivity.Zdata2;
import static com.example.myapplication.MainActivity.Zdata3;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MainActivity3 {

    List<Float> Distance = new ArrayList<Float>();
    List<Float> Distance2 = new ArrayList<Float>();
    List<ArrayList<Float>> Distance3 = new ArrayList<ArrayList<Float>>();
    List<ArrayList<Float>> Distance4 = new ArrayList<ArrayList<Float>>();

    List<Integer> D = new ArrayList<Integer>();
    List<ArrayList<Integer>> D2 = new ArrayList<ArrayList<Integer>>();
    List<Integer> D3 = new ArrayList<Integer>();
    List<ArrayList<Integer>> D4 = new ArrayList<ArrayList<Integer>>();

    public static List<String> L1 = new ArrayList<String>();
    List<String> L2 = new ArrayList<String>();
    public static List<String> L3 = new ArrayList<String>();
    public static List<ArrayList<String>> L4 = new ArrayList<ArrayList<String>>();
    List<Float> LS1 = new ArrayList<Float>();
    List<Float> LS2 = new ArrayList<Float>();
    List<Integer> LS3 = new ArrayList<Integer>();
    List<ArrayList<Float>> LS4 = new ArrayList<ArrayList<Float>>();
    List<ArrayList<Float>> LS6 = new ArrayList<ArrayList<Float>>();
    public static List<ArrayList<Integer>> LS5 = new ArrayList<ArrayList<Integer>>();

    //public static List<Integer> A = new ArrayList<Integer>();

    private int r4;
    private int k;


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }*/


    public void Read(int I) {
        if (I == 1) {

            L1.clear();
            L2.clear();
            L3.clear();
            L4.clear();

            Distance.clear();
            Distance2.clear();
            Distance3.clear();
            Distance4.clear();

            D.clear();
            D2.clear();
            D3.clear();
            D4.clear();

            r4 = 0;
            //A.clear();
            //k = 3;

        }

        if (I == 2) {
            /*try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            for (int h = 0; h < Xdata2.size(); h++) {
                float v1 = (Xdata2.get(h));
                String s1 = Float.toString(v1);
                float v2 = (Ydata2.get(h));
                String s2 = Float.toString(v2);
                float v3 = (Zdata2.get(h));
                String s3 = Float.toString(v3);
                L1.add(s1+" "+s2+" "+s3);

            }

        }
        if (I == 3) {
            L2.clear();
            L3.clear();
            L4.clear();
            Distance.clear();
            Distance2.clear();
            Distance3.clear();
            Distance4.clear();
            D.clear();
            D2.clear();
            D3.clear();
            D4.clear();
            r4 = 0;
            //A.clear();

            float U1 = 0;
            float U2 = 0;
            String num1 = " ";
            String num2 = " ";
            LS1.clear();
            LS2.clear();
            LS3.clear();
            LS4.clear();
            LS5.clear();
            LS6.clear();
            for (int j = 0; j < Xdata3.size(); j++) {
                float v4 = (Xdata3.get(j));
                String s4 = Float.toString(v4);
                float v5 = (Ydata3.get(j));
                String s5 = Float.toString(v5);
                float v6 = (Zdata3.get(j));
                String s6 = Float.toString(v6);
                L2.add(s4 + " " + s5 + " " + s6);


            }


            k = 5;
            for (int m = 0; m < Xdata2.size(); m++) {
                for (int l = 0; l < Xdata3.size(); l++) {
                    float X = Xdata3.get(l) - Xdata2.get(m);
                    float Y = Ydata3.get(l) - Ydata2.get(m);
                    float Z = Zdata3.get(l) - Zdata2.get(m);
                    Distance.add((float) (sqrt((pow((X), 2) + (pow((Y), 2)) + (pow((Z), 2))))));
                    Distance2.add((float) (sqrt((pow((X), 2) + (pow((Y), 2)) + (pow((Z), 2))))));
                }
            }
            //Log.d("MainActivity", "onSensorChanged: " + "X2: " + Xdata2);
            //Log.d("MainActivity", "onSensorChanged: " + "X3: " + Xdata3);
            //Log.d("MainActivity", "onSensorChanged: " + "Distance " + Distance);
            //Log.d("MainActivity", "onSensorChanged: " + "Distance2: " + Distance2);
            //Log.d("MainActivity", "onSensorChanged: " + "X2: " + Xdata2.size());
            //Log.d("MainActivity", "onSensorChanged: " + "X3: " + Xdata3.size());
            //Log.d("MainActivity", "onSensorChanged: " + "Distance " + Distance.size());


            Collections.sort(Distance); //organised y
            //Log.d("MainActivity", "onSensorChanged: " + "Distance " + Distance);
            for (int u = 0; u < (Distance.size()); u += Xdata3.size()) {
                Distance3.add(new ArrayList<Float>(Distance2.subList(u, Math.min(Distance.size(), u + Xdata3.size()))));
            }
            for (int u = 0; u < (Distance.size()); u += Xdata3.size()) {
                Distance4.add(new ArrayList<Float>(Distance2.subList(u, Math.min(Distance.size(), u + Xdata3.size()))));
            }
            //Log.d("MainActivity", "onSensorChanged: " + "Distance3: " + Distance3);
            //Log.d("MainActivity", "onSensorChanged: " + "Distance4: " + Distance4);
            //DistanceX4 = DistanceX3;
            /*for (int z = 0; z < (Distance.size()); z++) {
                //Distance4.add(new ArrayList<Float>(Distance.subList(u, Math.min(Distance.size(), u + Xdata.size()))));
                Collections.sort(Distance3.get(z));
            }*/

            for (int z = 0; z < (Xdata2.size()); z++) {
                //Distance4.add(new ArrayList<Float>(Distance.subList(u, Math.min(Distance.size(), u + Xdata.size()))));
                Collections.sort(Distance3.get(z));
            }
            //Collections.sort(Distance3);
            // Log.d("MainActivity", "onSensorChanged: " + "Distance3: " + Distance3);
            //Log.d("MainActivity", "onSensorChanged: " + "Distance: " + Distance.size());
            //Log.d("MainActivity", "onSensorChanged: " + "Distance3: " + Distance3.size());
            //Log.d("MainActivity", "onSensorChanged: " + "Distance4: " + Distance4.size());
            for (int x = 0; x < Distance3.size(); x++) {
                for (int o = 0; o < Xdata3.size(); o++) {
                    for (int n = 0; n < Xdata3.size(); n++) {
                        if (((Distance4.get(x).get(n))).equals((Distance3.get(x).get(o)))) {
                            n = n;
                            if ((o > 0) && ((Distance4.get(x).get(n))).equals((Distance3.get(x).get(o - 1)))) {
                                n = n + 1;
                                break;
                            }
                            //r2 = n;
                            //r3 = n;
                            r4 = n;
                            D.add(r4);
                        }
                    }

                }
            }

            //Log.d("MainActivity", "onSensorChanged: " + "R: " + D.size());
            for (int u = 0; u < (Distance.size()); u += Xdata3.size()) {
                D2.add(new ArrayList<Integer>(D.subList(u, Math.min(D.size(), u + Xdata3.size()))));
            }
            for (int q = 0; q < D2.size(); q++) {
                for (int p = 0; p < k; p++) {
                    D3.add((D2.get(q)).get(p));
                }
            }

            for (int u = 0; u < D3.size(); u += k) {
                D4.add(new ArrayList<Integer>(D3.subList(u, u + k)));
            }
            Log.d("MainActivity", "onSensorChanged: " + "R: " + D);
            Log.d("MainActivity", "onSensorChanged: " + "R2: " + D2);
            Log.d("MainActivity", "onSensorChanged: " + "R3: " + D3);
            Log.d("MainActivity", "onSensorChanged: " + "R4: " + D4);
            Log.d("MainActivity", "onSensorChanged: " + "R: " + D.size());
            Log.d("MainActivity", "onSensorChanged: " + "R2: " + D2.size());
            Log.d("MainActivity", "onSensorChanged: " + "R3: " + D3.size());
            Log.d("MainActivity", "onSensorChanged: " + "R4: " + D4.size());
            for (int u = 0; u < D4.size(); u++) {
                for (int t = 0; t < k; t++) {
                    L3.add(L2.get(((D4.get(u))).get(t)));
                }
            }

            /*for (int u = 0; u < (L3.size() - L1.size() + 1); u++) {
                L4.add(new ArrayList<String>(L3.subList(u, u + L1.size())));
            }*/
            for (int u = 0; u < (L3.size()); u+=k) {
                L4.add(new ArrayList<String>(L3.subList(u, u + (k))));
            }
            Log.d("MainActivity", "onSensorChanged: " + "L1: " + L1);
            Log.d("MainActivity", "onSensorChanged: " + "L2: " + L2);
            Log.d("MainActivity", "onSensorChanged: " + "L3: " + L3);
            Log.d("MainActivity", "onSensorChanged: " + "L4: " + L4);
            //L4.add(new ArrayList<String>(L3.subList(0, L3.size())));


                for (int p = 0; p < L1.size(); p++) {
                    String str1 = L1.get(p);
                    String regex = ("(\\d+(?:\\.\\d+)?)|-(\\d+(?:\\.\\d+)?)");
                    //"-?\\d+"
                    //"(\\d+(?:\\.\\d+)?)"
                    Matcher matcher = Pattern.compile(regex).matcher(str1);
                    while ((matcher.find() == true) && (LS1.size() < L1.size() * 3)) {
                        num1 = matcher.group();
                        U1 = Float.valueOf(num1);
                        LS1.add(U1);
                    }
                }
            for (int c = 0; c < L4.size(); c++) {
                for (int q = 0; q < (k); q++) {
                String str2 = (L4.get(c)).get(q);
                String regex = ("(\\d+(?:\\.\\d+)?)|-(\\d+(?:\\.\\d+)?)");
                Matcher matcher2 = Pattern.compile(regex).matcher(str2);
                    while ((matcher2.find() == true) && (LS2.size()< (L2.size())*(k*3))) {
                        num2 = matcher2.group();
                        U2 = Float.valueOf(num2);
                        LS2.add(U2);
                    }
                }
            }
            Log.d("MainActivity", "onSensorChanged: " + "L1 " + L1);
            Log.d("MainActivity", "onSensorChanged: " + "L2: " + L2);
            Log.d("MainActivity", "onSensorChanged: " + "L3: " + L3);
            Log.d("MainActivity", "onSensorChanged: " + "L4: " + L4);
            Log.d("MainActivity", "onSensorChanged: " + "L1: " + L1.size());
            Log.d("MainActivity", "onSensorChanged: " + "L4: " + L4.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS1: " + LS1);
            Log.d("MainActivity", "onSensorChanged: " + "LS1: " + LS1.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS2: " + LS2);
            Log.d("MainActivity", "onSensorChanged: " + "LS2: " + LS2.size());


            for (int u = 0; u < (LS2.size()); u+=(3*k)) {
                LS4.add(new ArrayList<Float>(LS2.subList(u, Math.min(LS2.size(), u + (3*k)))));
            }
            Log.d("MainActivity", "onSensorChanged: " + "LS4: " + LS4);
            Log.d("MainActivity", "onSensorChanged: " + "LS4: " + LS4.size());

            for (int u = 0; u < (LS1.size()); u+=(3)) {
                LS6.add(new ArrayList<Float>(LS1.subList(u, Math.min(LS1.size(), u + (3)))));
            }
            Log.d("MainActivity", "onSensorChanged: " + "LS6: " + LS6 );
            Log.d("MainActivity", "onSensorChanged: " + "LS6: " + LS6.size() );
            for (int q = 0; q < LS4.size(); q++) {
                if (((abs(LS6.get(q).get(0) - (LS4.get(q)).get(0)) <= 0.75)&&(abs(LS6.get(q).get(1) - (LS4.get(q)).get(1)) <= 0.75)&&(abs(LS6.get(q).get(2) - (LS4.get(q)).get(2)) <= 0.75))||((abs(LS6.get(q).get(0) - (LS4.get(q)).get(3)) <= 0.75)&&(abs(LS6.get(q).get(1) - (LS4.get(q)).get(4)) <= 0.75)&&(abs(LS6.get(q).get(2) - (LS4.get(q)).get(5)) <= 0.75))||((abs(LS6.get(q).get(0) - (LS4.get(q)).get(6)) <= 0.75)&&(abs(LS6.get(q).get(1) - (LS4.get(q)).get(7)) <= 0.75)&&(abs(LS6.get(q).get(2) - (LS4.get(q)).get(8)) <= 0.75))||((abs(LS6.get(q).get(0) - (LS4.get(q)).get(9)) <= 0.75)&&(abs(LS6.get(q).get(1) - (LS4.get(q)).get(10)) <= 0.75)&&(abs(LS6.get(q).get(2) - (LS4.get(q)).get(11)) <= 0.75))||((abs(LS6.get(q).get(0) - (LS4.get(q)).get(12)) <= 0.75)&&(abs(LS6.get(q).get(1) - (LS4.get(q)).get(13)) <= 0.75)&&(abs(LS6.get(q).get(2) - (LS4.get(q)).get(14)) <= 0.75))){
                        LS3.add(1);
                    }
                    else {
                        LS3.add(0);
                    }
                //}
            }
            Log.d("MainActivity", "onSensorChanged: " + "LS3: " + LS3);
            /*for (int u = 0; u < ((L4.size())*(L1.size())); u+= (L1.size())) {
                LS5.add(new ArrayList<Integer>(LS3.subList(u, u + (L1.size()))));
            }*/
            LS5.add(new ArrayList<Integer>(LS3.subList(0, (LS3.size()))));

            Log.d("MainActivity", "onSensorChanged: " + "R: " + D);
            Log.d("MainActivity", "onSensorChanged: " + "R2: " + D2);
            Log.d("MainActivity", "onSensorChanged: " + "R3: " + D3);
            Log.d("MainActivity", "onSensorChanged: " + "R4: " + D4);
            Log.d("MainActivity", "onSensorChanged: " + "X2: " + Xdata2);
            Log.d("MainActivity", "onSensorChanged: " + "X3: " + Xdata3);
            Log.d("MainActivity", "onSensorChanged: " + "Y2: " + Ydata2);
            Log.d("MainActivity", "onSensorChanged: " + "Y3: " + Ydata3);
            Log.d("MainActivity", "onSensorChanged: " + "Z2: " + Zdata2);
            Log.d("MainActivity", "onSensorChanged: " + "Z3: " + Zdata3);
            Log.d("MainActivity", "onSensorChanged: " + "D1: " + Distance);
            Log.d("MainActivity", "onSensorChanged: " + "D2: " + Distance2);
            Log.d("MainActivity", "onSensorChanged: " + "D3: " + Distance3);
            Log.d("MainActivity", "onSensorChanged: " + "D4: " + Distance4);
            Log.d("MainActivity", "onSensorChanged: " + "L1 " + L1);
            Log.d("MainActivity", "onSensorChanged: " + "L2: " + L2);
            Log.d("MainActivity", "onSensorChanged: " + "L3: " + L3 );
            Log.d("MainActivity", "onSensorChanged: " + "L4: " + L4 );
            Log.d("MainActivity", "onSensorChanged: " + "L3: " + L3.size());
            Log.d("MainActivity", "onSensorChanged: " + "L4: " + L4.size());
            Log.d("MainActivity", "onSensorChanged: " + "L1 X2: " + L1.size()+" "+Xdata2.size());
            Log.d("MainActivity", "onSensorChanged: " + "L2 X3: " + L2.size()+" "+Xdata3.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS1: " + LS1.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS2: " + LS2.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS3: " + LS3.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS1: " + LS1);
            Log.d("MainActivity", "onSensorChanged: " + "LS2: " + LS2);
            Log.d("MainActivity", "onSensorChanged: " + "LS4: " + LS4.size());
            Log.d("MainActivity", "onSensorChanged: " + "LS6: " + LS6.size() );
            Log.d("MainActivity", "onSensorChanged: " + "LS4: " + LS4);
            Log.d("MainActivity", "onSensorChanged: " + "LS6: " + LS6 );
            Log.d("MainActivity", "onSensorChanged: " + "LS5: " + LS5);
            Log.d("MainActivity", "onSensorChanged: " + "LS5: " + LS5.size());
        }
    }
}