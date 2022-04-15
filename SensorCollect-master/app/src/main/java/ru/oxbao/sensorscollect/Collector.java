package ru.oxbao.sensorscollect;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class Collector
{
    // Flags, parameters
    private boolean m_fromSensorMeasurements = false;
    private int m_numberOfMeasurements;
    private int m_count;
    // Objects
    private InputInterface m_inputInterface;
    private TestExecutor m_ownerExecutor;
    private MainActivity m_ownerActivity;
    // Service
    private Bundle m_bundle;
    private Message m_message;
    private final String COLLECTOR_TAG = "Collector";

    public Collector(MainActivity activityTestExecutor, TestExecutor testExecutor, int number)
    {
        m_inputInterface = new InputInterface(activityTestExecutor, testExecutor, this);
        m_numberOfMeasurements = number;
        m_ownerExecutor = testExecutor;
        m_ownerActivity = activityTestExecutor;
        m_bundle = new Bundle();
        m_message = new Message();
    }

    public void Start(InputInterface.InputTypeEnum inputTypeEnum)
    {
        if (inputTypeEnum.equals(InputInterface.InputTypeEnum.sensors))
        {
            m_fromSensorMeasurements = true;
        } else
        {
            m_fromSensorMeasurements = false;
        }

        m_count = 0;
        m_inputInterface.Start(inputTypeEnum);
    }

    public void Stop()
    {
        m_inputInterface.Stop();
    }

    public void Amass(double XAxis, double YAxis, double ZAxis, long time)
    {
        try
        {
            m_ownerExecutor.g_testData.XAxis[m_count] = XAxis;
            m_ownerExecutor.g_testData.YAxis[m_count] = YAxis;
            m_ownerExecutor.g_testData.ZAxis[m_count] = ZAxis;
            m_ownerExecutor.g_testData.TimeInNanoSeconds[m_count] = time;
        } catch (IndexOutOfBoundsException e)
        {
            Log.d(COLLECTOR_TAG, "index testData is out of range");
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.d(COLLECTOR_TAG, "Anything exception ");
        }

        //Сделано для облегчения метода при использовании датчика
        if (m_fromSensorMeasurements)
        {
            m_ownerActivity.SetProgressBar(m_count);
        } else
        {
     //       m_ownerActivity.SendMessage(m_count);
        }

        m_count++;
        //Log.d(COLLECTOR_TAG, String.valueOf(m_count) + " " + String.valueOf(m_numberOfMeasurements));
        if (m_count > m_numberOfMeasurements - 1)
        {
            Stop();
            Log.d(COLLECTOR_TAG, "Stopped ");
            m_ownerExecutor.OnDataCollected();     // Окончание накопления
        }
    }

    public String[] GetFilesNames()
    {
        return m_inputInterface.GetFilesNames();
    }

    public void SetNumberOfMeasurements(int number)
    {
        m_numberOfMeasurements = number;
    }

    /*Нужен для остановки при чтении из файла. Возможно возникновение исклучений при чтении и счетчик накопленных значений не заполняется до конца*/
    public void OnDataCollected()
    {
        Stop();
        m_ownerExecutor.OnDataCollected();
    }
}
