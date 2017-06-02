package com.example.zfanji.myapplication;

import android.util.Log;

/**
 * Created by Zfanji on 2017/5/26.
 */

public class JavaClass {
    private static final String TAG = "JavaClass";
    private int anInt = 0;

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public void testJavaCallKotlin(String title){
            Mondai dai = new Mondai(5,title);
        Log.d(TAG,"testJavaCallKotlin dai.title="+dai.getTitle());
    }
}
