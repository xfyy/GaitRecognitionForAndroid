package ru.oxbao.sensorscollect;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class StorageOutput
{
    private static final String DIRECTORY_NAME = "/Accelerometer";
    private static final String FILE_EXTENSION = ".txt";
    private String m_path;
    private String m_prefix;
    private BufferedWriter m_bufferedWriter;
    private TestExecutor m_ownerTestExecutor;
    private final String STORAGE_OUTPUT_TAG = "Saver";

    public StorageOutput(TestExecutor testExecutor, String prefix)
    {
        m_prefix = prefix;
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        m_path = GetDirectoryToSaveWhere(externalStorageDirectory);
        m_ownerTestExecutor = testExecutor;
    }

    public boolean SaveInFile(TestData testData)
    {
        try
        {
           // String secStore = System.getenv("SECONDARY_STORAGE");
           // File file = new File(secStore);

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                m_ownerTestExecutor.setText("mounted");
            }else
            {
                m_ownerTestExecutor.setText("unmounted");
            }
            m_path = Environment.getExternalStorageDirectory().getPath() + DIRECTORY_NAME;
            File file = new File(m_path, GenerateNewFileName(m_prefix));
            m_bufferedWriter = new BufferedWriter(new FileWriter(file));
            String header = "XAxis YAxis ZAxis Time";
            m_bufferedWriter.write(header);
            try
            {
                for (int i = 0; i < testData.XAxis.length; i++)
                {
                    m_bufferedWriter.newLine();
                    String tmp = testData.XAxis[i] + " " + testData.YAxis[i] + " " +
                            testData.ZAxis[i] + " " + testData.TimeInNanoSeconds[i];
                    m_bufferedWriter.write(tmp);
                }
            } catch (IndexOutOfBoundsException e)
            {
                Log.d(STORAGE_OUTPUT_TAG, "Index is out of range");
            }
            m_bufferedWriter.close();
            m_ownerTestExecutor.setText("OK");
        } catch (IOException e)
        {
            e.printStackTrace();
            //m_ownerTestExecutor.setText("Fail");
            m_ownerTestExecutor.ShowToast(TestExecutor.ToastMessage.failSaveData);
            try
            {
                m_bufferedWriter.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            } catch (NullPointerException e1)
            {
                e.printStackTrace();
            }

        }

        return true;
    }

    public static String GetDirectoryToSaveWhere(String externalStorageDirectory)
    {
        String dir = externalStorageDirectory + DIRECTORY_NAME;
        File folder = new File(dir);

        if (!folder.exists())
        {
            folder.mkdir();
        }

        return dir;
    }

    private static String GenerateNewFileName(String prefix)
    {
        Calendar calendar = Calendar.getInstance();
        return prefix + '_' + calendar.get(Calendar.YEAR) + '_' + (calendar.get(Calendar.MONTH) + 1) + '_' +
                calendar.get(Calendar.DAY_OF_MONTH) + '_' + calendar.get(Calendar.HOUR_OF_DAY) + '_' +
                calendar.get(Calendar.MINUTE) + '_' + calendar.get(Calendar.SECOND) + FILE_EXTENSION;
    }

    public void SetPrefix(String prefix)
    {
        m_prefix = prefix;
    }
}
