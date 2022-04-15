package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;


public class Accuracy {
    public static List<Integer> A = new ArrayList<Integer>();

    public void Accu (List<String> X, List<ArrayList<String>> Y, List<ArrayList<Integer>> Z) {
        A.clear();
        for (int q = 0; q < Z.size(); q++) {
            float j = 0;
            int accuracy = 0;
            for (int p = 0; p < X.size(); p++) {
                if ((((Z.get(q)).get(p)) == 1)) {
                    //((X.get(p)).substring(0, 3).equals((Y.get(q).get(p)).substring(0, 3))) &&
                    j = j + 1;
                } else {
                    j = j;
                }
            }
            Log.d("MainActivity", "onSensorChanged: " + "J: " + j);
            accuracy = round(j / (X.size()) * 100);
            A.add(accuracy);

        }
    }
}
