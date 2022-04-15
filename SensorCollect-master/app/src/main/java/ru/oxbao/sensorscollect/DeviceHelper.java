package ru.oxbao.sensorscollect;


import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;

public class DeviceHelper
{
    private static DeviceHelper ourInstance = new DeviceHelper();
    public static SensorManager mSensorManager;

    public static DeviceHelper getInstance()
    {
        return ourInstance;
    }

    private DeviceHelper()
    {

    }

    public static void GetOsVersion()
    {

    }
    public static void getSensors()
    {
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE)
    }
}
