package com.example.pro.SensorTool.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by caizq on 2017-08-30.
 */
public class FileHelper
{
    private Context context;
    /** SD卡是否存在**/
    private boolean hasSD = false;
    /** SD卡的路径**/
    private String SDPATH;
    /** 当前程序包的路径**/
    private String FILESPATH;
    public FileHelper(Context context)
    {
        this.context = context;
        hasSD = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        SDPATH = Environment.getExternalStorageDirectory().getPath();
        FILESPATH = this.context.getFilesDir().getPath();
    }
        /**
         * 在SD卡上创建文件
         */
        public File createSDFile(String fileName) throws IOException {
            String fileDir = SDPATH + "/SensorData";
//            File file = new File(SDPATH + "//" + fileName);

            new File(fileDir).mkdirs();

            File file = new File(fileDir +"//"+ fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            boolean a = file.exists();
            return file;
        }
        /**
         * 删除SD卡上的文件
         *
         * @param fileName
         */
        public boolean deleteSDFile(String fileName) {
            File file = new File(SDPATH +  "/SensorData"+ "//" + fileName);
            if (file == null || !file.exists() || file.isDirectory())
                return false;
            return file.delete();
        }
        /**
         * 写入内容到SD卡中的txt文本中
         * str为内容
         */
        public void writeSDFile(String str,String fileName) {
            String filePath = SDPATH + "/SensorData"+ "//" + fileName;
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(filePath, true);
                bw = new BufferedWriter(fw);
//                bw.append("在已有的基础上添加字符串");
                bw.write(str);
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 读取SD卡中文本文件
         *
         * @param fileName
         * @return
         */
        public String readSDFile(String fileName)
        {
            StringBuffer sb = new StringBuffer();
            File file = new File(SDPATH + "//" + fileName);
            try
            {
                FileInputStream fis = new FileInputStream(file);
                int c;
                while ((c = fis.read()) != -1)
                {
                    sb.append((char) c);
                }
                fis.close();
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return sb.toString();
        }
        public String getFILESPATH()
        {
            return FILESPATH;
        }
        public String getSDPATH()
        {
            return SDPATH;
        }
        public boolean hasSD()
        {
            return hasSD;
        }
    }
