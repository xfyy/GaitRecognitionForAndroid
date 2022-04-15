package com.example.pro.collectsensor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.hardware.Sensor;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.google.common.base.Joiner;

public class DataWriter {
    final String FILENAME = generateFilename();
    boolean mFirstLine = true;
    ArrayList<SensorID> mSensorIDs = new ArrayList<SensorID>();

    public DataWriter(SparseArray<Sensor> sensors) {
        buildSensorIDs(sensors);
        writeFirstLine();
//        writeLine();
    }

    private void buildSensorIDs(SparseArray<Sensor> sensors) {
        for (int i = 0; i < sensors.size(); i++)
            mSensorIDs.addAll(SensorID.getSensorIDs(sensors.valueAt(i).getType()));
    }

    private void writeFirstLine() {
        ArrayList<String> strings = new ArrayList<String>();

        for (SensorID sensorID : mSensorIDs)
            strings.add(sensorID.toString());
        strings.add("TIME");
        strings.add("CLASS");
        writeLine(Joiner.on(",").join(strings));
        Log.v("DataWriter", "Write first line: " + Joiner.on(",").join(strings));
    }

    private void writeLine(String string) {
        try {
            FileWriter fileWriter = new FileWriter(new File(Environment.getExternalStorageDirectory(), FILENAME), true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            if (!mFirstLine) writer.newLine();
            else mFirstLine = false;
            writer.write(string);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLine(HashMap<SensorID, Float> hm, long time, String activity) {
        ArrayList<String> strings = new ArrayList<String>();

        for (SensorID sensorID : mSensorIDs)
            strings.add(Float.toString(hm.get(sensorID)));
        strings.add(Long.toString(time));
        strings.add(activity);
        writeLine(Joiner.on(",").join(strings));
    }

    private String generateFilename() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
        return "/app_log/" + formatter.format(now) + ".csv";
    }
}
