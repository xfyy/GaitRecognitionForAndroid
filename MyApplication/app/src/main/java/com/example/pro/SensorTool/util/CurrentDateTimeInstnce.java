package com.example.pro.SensorTool.util;

import java.text.SimpleDateFormat;

/**
 * Created by caizq on 2017-08-30.
 * get the format current date and time
 */
public class CurrentDateTimeInstnce {
    // single instance
    private static volatile  CurrentDateTimeInstnce instnce;
    private CurrentDateTimeInstnce()
    {

    }
    public  static  CurrentDateTimeInstnce getInstnce()
    {
        if(instnce == null)
        {
            synchronized (CurrentDateTimeInstnce.class)
            {
                if(instnce == null)
                {
                    instnce = new CurrentDateTimeInstnce();
                }
            }

        }
        return instnce;
    }
    // format current date & time
    public String  formatDateTime()
    {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}
