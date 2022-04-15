package com.example.pro.collectsensor;

import java.util.ArrayList;
import java.util.Arrays;

import android.hardware.Sensor;
import android.util.SparseArray;

public enum SensorID {
	ACC_X, ACC_Y, ACC_Z, 
	GYRO_X, GYRO_Y, GYRO_Z, 
	LINACC_X, LINACC_Y, LINACC_Z,
	GRAV_X, GRAV_Y, GRAV_Z, 
	ROTVEC_X, ROTVEC_Y, ROTVEC_Z;

	public static ArrayList<SensorID> getSensorIDs(int sensorType) {
		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.ACC_X, SensorID.ACC_Y, SensorID.ACC_Z));
		case Sensor.TYPE_GYROSCOPE:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.GYRO_X, SensorID.GYRO_Y, SensorID.GYRO_Z));
		case Sensor.TYPE_GRAVITY:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.GRAV_X, SensorID.GRAV_Y, SensorID.GRAV_Z));
		case Sensor.TYPE_LINEAR_ACCELERATION:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.LINACC_X, SensorID.LINACC_Y, SensorID.LINACC_Z));
		case Sensor.TYPE_ROTATION_VECTOR:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.ROTVEC_X, SensorID.ROTVEC_Y, SensorID.ROTVEC_Z));
		default:
			throw new IllegalArgumentException("Invalid sensor type in getSensorIDs()");
		}
	}
	
	public static ArrayList<SensorID> getSensorIDs(int[] sensorTypes) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int sensorType : sensorTypes)
			sensorIDs.addAll(getSensorIDs(sensorType));
		return sensorIDs;
	}
	
	public static ArrayList<SensorID> getSensorIDs(SparseArray<Sensor> sensors) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int i = 0; i < sensors.size(); i++)
			sensorIDs.addAll(getSensorIDs(sensors.valueAt(i).getType()));
		return sensorIDs;
	}
}
