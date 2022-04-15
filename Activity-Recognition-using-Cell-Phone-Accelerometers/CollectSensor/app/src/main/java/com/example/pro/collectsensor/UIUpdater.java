package com.example.pro.collectsensor;

import java.util.HashMap;
import java.util.Map;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

public class UIUpdater {

	private MainActivity mActivity;
	private SensorLogger mSensorLogger;
	private TextView mTextAccX;
	private TextView mTextAccY;
	private TextView mTextAccZ;
	private TextView mTextGyroX;
	private TextView mTextGyroY;
	private TextView mTextGyroZ;
	private TextView mTextGravX;
	private TextView mTextGravY;
	private TextView mTextGravZ;
	private TextView mTextLinAccX;
	private TextView mTextLinAccY;
	private TextView mTextLinAccZ;
	private TextView mTextRotVecX;
	private TextView mTextRotVecY;
	private TextView mTextRotVecZ;
	private Button mButtonStart;
	private Chronometer mChronometer;
	
	public UIUpdater(final MainActivity act) {
		mActivity = act;
		mSensorLogger = mActivity.getSensorLogger();
		initUIObjs();
		initActivitySpinner();
		initStartButton();
		initChronometer();
	}
	
	private void initChronometer() {
		mChronometer = (Chronometer)mActivity.findViewById(R.id.chronometer1);
		
	}
	
	private void initUIObjs() {
		mTextAccX = (TextView)mActivity.findViewById(R.id.acc_x_value);
		mTextAccY = (TextView)mActivity.findViewById(R.id.acc_y_value);
		mTextAccZ = (TextView)mActivity.findViewById(R.id.acc_z_value);
		mTextGyroX = (TextView)mActivity.findViewById(R.id.gyro_x_value);
		mTextGyroY = (TextView)mActivity.findViewById(R.id.gyro_y_value);
		mTextGyroZ = (TextView)mActivity.findViewById(R.id.gyro_z_value);
		mTextGravX = (TextView)mActivity.findViewById(R.id.grav_x_value);
		mTextGravY = (TextView)mActivity.findViewById(R.id.grav_y_value);
		mTextGravZ = (TextView)mActivity.findViewById(R.id.grav_z_value);
		mTextLinAccX = (TextView)mActivity.findViewById(R.id.linacc_x_value);
		mTextLinAccY = (TextView)mActivity.findViewById(R.id.linacc_y_value);
		mTextLinAccZ = (TextView)mActivity.findViewById(R.id.linacc_z_value);
		mTextRotVecX = (TextView)mActivity.findViewById(R.id.rotvec_x_value);
		mTextRotVecY = (TextView)mActivity.findViewById(R.id.rotvec_y_value);
		mTextRotVecZ = (TextView)mActivity.findViewById(R.id.rotvec_z_value);
	}
	
	private void initActivitySpinner() {
		Spinner spinner = (Spinner) mActivity.findViewById(R.id.spn_activity);
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(mActivity, R.array.activities_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mSensorLogger.updateActivity(arg0.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		mSensorLogger.updateActivity(spinner.getSelectedItem().toString());
	}
		
	private void initStartButton() {
		mButtonStart = (Button)mActivity.findViewById(R.id.btn_start);
		mButtonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onStartButtonClick();
			}
		});
	}
	
	private void onStartButtonClick() {
		if (mButtonStart.getText().equals("Start")) {
			mButtonStart.setText("Stop");
			startChronometer();
			mSensorLogger.start();
		}
		else {
			mButtonStart.setText("Start");
			stopChronometer();
			mSensorLogger.stop();
		}
	}
	
	private void startChronometer() {
		mChronometer.setBase(SystemClock.elapsedRealtime());//设置计时器的起始时间。 从某个时间经历了多长时间
		mChronometer.start();
	}
	
	private void stopChronometer() {
		mChronometer.stop();
		
	}
	
	public void updateValues(HashMap<SensorID, Float> data) {
		for (Map.Entry<SensorID, Float> entry : data.entrySet()) {
			switch (entry.getKey()) {
			case ACC_X:
				mTextAccX.setText(Float.toString(entry.getValue()));
				break;
			case ACC_Y:
				mTextAccY.setText(Float.toString(entry.getValue()));
				break;
			case ACC_Z:
				mTextAccZ.setText(Float.toString(entry.getValue()));
				break;
			case GRAV_X:
				mTextGravX.setText(Float.toString(entry.getValue()));
				break;
			case GRAV_Y:
				mTextGravY.setText(Float.toString(entry.getValue()));
				break;
			case GRAV_Z:
				mTextGravZ.setText(Float.toString(entry.getValue()));
				break;
			case GYRO_X:
				mTextGyroX.setText(Float.toString(entry.getValue()));
				break;
			case GYRO_Y:
				mTextGyroY.setText(Float.toString(entry.getValue()));
				break;
			case GYRO_Z:
				mTextGyroZ.setText(Float.toString(entry.getValue()));
				break;
			case LINACC_X:
				mTextLinAccX.setText(Float.toString(entry.getValue()));
				break;
			case LINACC_Y:
				mTextLinAccY.setText(Float.toString(entry.getValue()));
				break;
			case LINACC_Z:
				mTextLinAccZ.setText(Float.toString(entry.getValue()));
				break;
			case ROTVEC_X:
				mTextRotVecX.setText(Float.toString(entry.getValue()));
				break;
			case ROTVEC_Y:
				mTextRotVecY.setText(Float.toString(entry.getValue()));
				break;
			case ROTVEC_Z:
				mTextRotVecZ.setText(Float.toString(entry.getValue()));
				break;
			default:
				Log.e("MainActivity", "MainActivity.updateValues() received an invalid sensor type");
				break;
			}
		}
	}


}
