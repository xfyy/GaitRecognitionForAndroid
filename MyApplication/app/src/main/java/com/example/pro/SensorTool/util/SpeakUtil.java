package com.example.pro.SensorTool.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by caizq on 2017-08-26.
 */

public class SpeakUtil {
    private TextToSpeech textToSpeech;
    // single instance
    private static volatile SpeakUtil insatnce = null;
    private Context context;
    private SpeakUtil(Context context)
    {
        this.context = context;
    }
    public static SpeakUtil getInsatnce(Context context)
    {
        if(insatnce == null)
        {
            synchronized(SpeakUtil.class)
            {
                if(insatnce == null)
                {
                    insatnce = new SpeakUtil(context);
                }
            }
        }
        return insatnce;
    }
    // soeak init and default speak chinese
    public void speakInit()
    {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.CHINESE);
            }
        });
    }
    public void setLanguage(Locale language)
    {
        textToSpeech.setLanguage(language);
    }
    // shutdown speak
    public void shutDownSpeakSource()
    {
        if(textToSpeech != null)
        {
            textToSpeech.shutdown();
        }
    }
    // speack content
    public void speak(String content)
    {
        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
    }
}
