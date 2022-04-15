package ru.oxbao.sensorscollect;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private TextView tvSensorList;
    private TestExecutor m_testExecutor;
    private Button btnStartAmass;
    private ProgressBar pbCollectedMsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSensorList = (TextView) findViewById(R.id.tvSensorList);
        btnStartAmass = (Button) findViewById(R.id.btnStartAmass);
        pbCollectedMsr = (ProgressBar) findViewById(R.id.pbCollectedMsr);
        pbCollectedMsr.setMax(WorkMath.NumberOfMeasurements);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        tvSensorList.setText("");
        tvSensorList.append(deviceSensors.toString());

        m_testExecutor = new TestExecutor(this);

        btnStartAmass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAmass();
            }
        });


    }


    public void SetProgressBar(int progress)
    {
        pbCollectedMsr.setProgress(progress);
    }

    public void SetText(String text)
    {
        tvSensorList.setText(text);
    }

    public void StartAmass()
    {
        m_testExecutor.Start(TestExecutor.TestEnum.test1);
        tvSensorList.setText("...");
    }


}
