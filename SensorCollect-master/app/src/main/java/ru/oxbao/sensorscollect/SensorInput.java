package ru.oxbao.sensorscollect;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorInput implements SensorEventListener
{
    private Collector m_collectorOwner;
    private MainActivity m_ownerActivity;
    private TestExecutor m_ownerTestExecutor;
    private SensorManager m_sensorManager;
    private boolean m_sensorIsWorking;
    private final String SENSOR_INPUT = "SensorInput";

    public SensorInput(MainActivity activityTestExecutor, TestExecutor testExecutor,
                       Collector collector)
    {
        m_collectorOwner = collector;
        m_ownerActivity = activityTestExecutor;
        m_sensorManager = (SensorManager) m_ownerActivity.getSystemService(Context.SENSOR_SERVICE);
        m_sensorIsWorking = false;
        m_ownerTestExecutor = testExecutor;
    }

    public void Start()
    {
        Sensor s = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (s != null)
        {
            m_sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failStartSensor);
            m_sensorIsWorking = true;
        } else
        {
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failSaveData);
            //m_collectorOwner.Stop();
        }

        Sensor gyro = m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyro != null)
        {
            m_sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_FASTEST);
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failStartSensor);
            m_sensorIsWorking = true;
        } else
        {
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failSaveData);
           // m_collectorOwner.Stop();
        }

        Sensor mag = m_sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mag != null)
        {
            m_sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_FASTEST);
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failStartSensor);
            m_sensorIsWorking = true;
        } else
        {
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failSaveData);
            // m_collectorOwner.Stop();
        }
    }

    public void Stop()
    {
        m_sensorManager.unregisterListener(this);
        m_sensorIsWorking = false;
        Log.d(SENSOR_INPUT, "SensorInput stopped");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            m_collectorOwner.Amass(sensorEvent.values[0], sensorEvent.values[1],
                    sensorEvent.values[2], sensorEvent.timestamp);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    public boolean IsWorking()
    {
        return m_sensorIsWorking;
    }

    @Override
    protected void finalize() throws Throwable
    {
        Stop();
        super.finalize();
    }
}
