package ru.oxbao.sensorscollect;


import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import android.os.Handler;

public class StorageInput
{
    private MainActivity m_ownerActivity;
    private Collector m_collectorOwner;
    private TestExecutor m_ownerTestExecutor;
    private String m_dir;
    private Handler m_handler;
    private boolean m_storageIsWorking;
    private static final String DIRECTORY_NAME = "/Accelerometer";
    private static final String FILE_EXTENSION = ".txt";


    private final String STORAGE_INPUT_TAG = "StorageInput";

    // Поток нужен что б ГУИ был активен во время чтения
    private class Reader extends Thread
    {
        private String fileName;

        private Reader(String fileName)
        {
            this.fileName = fileName;
        }

        @Override
        public void run()
        {
            Bundle bundle = new Bundle();
            Message msg = new Message();
            File file = new File(m_dir + "/" + fileName);
            BufferedReader bufferedReader = null;
            try
            {
                bufferedReader = new BufferedReader(new FileReader(file));
                while (bufferedReader.ready())
                {
                    String[] line = bufferedReader.readLine().split(" ");
                    try
                    {
                        double XAxis = Double.parseDouble(line[0]);
                        double YAxis = Double.parseDouble(line[1]);
                        double ZAxis = Double.parseDouble(line[2]);
                        long Time = Long.parseLong(line[3]);
                        bundle.putDouble("XAxis", XAxis);
                        bundle.putDouble("YAxis", YAxis);
                        bundle.putDouble("ZAxis", ZAxis);
                        bundle.putLong("Time", Time);
                        bundle.putString("key", "collector"); //Ключ сообщения нужен для идентификации сообщения
                        msg.setData(bundle);
                        m_handler.handleMessage(msg);
                    } catch (NumberFormatException e)
                    {
                        Log.d(STORAGE_INPUT_TAG, "NumberFormatException");
                    } catch (IndexOutOfBoundsException e)
                    {
                        Log.d(STORAGE_INPUT_TAG, "IndexOutOfBoundsException");
                    }
                }
                bufferedReader.close();
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
                Log.d(STORAGE_INPUT_TAG, "File is not found");
            } catch (IOException e)
            {
                try
                {
                    bufferedReader.close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                Log.d(STORAGE_INPUT_TAG, "Can't read line");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            bundle.clear();
            bundle.putString("key", "stop"); //Ключ сообщения нужен для идентификации сообщения
            msg.setData(bundle);
            m_handler.handleMessage(msg);
        }
    }

    public StorageInput(MainActivity activityTestExecutor, TestExecutor m_ownerTestExecutor, final Collector m_collectorOwner)
    {
        this.m_collectorOwner = m_collectorOwner;
        this.m_ownerTestExecutor = m_ownerTestExecutor;
        m_ownerActivity = activityTestExecutor;
        m_dir = Environment.getExternalStorageDirectory().toString() + DIRECTORY_NAME;
        m_handler = new Handler()
        { // Необходим для заполнения коллектора из другого потока
            @Override
            public void handleMessage(Message msg)
            {
                Bundle bundle = msg.getData();
                String key = bundle.getString("key");
                if (key.equals("collector"))
                {
                    m_collectorOwner.Amass(bundle.getDouble("XAxis"), bundle.getDouble("YAxis"),
                            bundle.getDouble("ZAxis"), bundle.getLong("Time"));
                } else if (key.equals("stop"))
                {
                    m_collectorOwner.OnDataCollected();
                }

            }


        };
    }

    public void Start()
    {
        String fileName = m_ownerTestExecutor.GetCheckedSpinner();
        if (!fileName.equals(""))
        {
            Reader reader = new Reader(fileName);
            reader.start();
        } else
        {
            m_collectorOwner.Stop();
        }
    }

    public void Stop()
    {
        m_storageIsWorking = false;
    }

    public String[] GetFilesFromFolder()
    {
        String[] files;
        try
        {
            File folder = new File(m_dir);
            if (!folder.exists())
            {
                folder.mkdir();
                files = null;
                return files;
            } else
            {
                files = folder.list(new FilenameFilter()
                {
                    @Override
                    public boolean accept(File file, String name)
                    {
                        return name.endsWith(FILE_EXTENSION);
                    }
                });
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            files = null;

        }
        return files;

    }

    public int GetNumberLines()
    {
        String fileName = m_ownerTestExecutor.GetCheckedSpinner();
        File file = new File(m_dir + "/" + fileName);
        BufferedReader bufferedReader = null;
        int count = 0;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(file));
            while (bufferedReader.ready())
            {
                count++;
                bufferedReader.readLine();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return count;
    }

}
