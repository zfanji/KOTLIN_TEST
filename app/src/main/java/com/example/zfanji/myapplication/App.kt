package com.example.zfanji.myapplication

import android.app.Application
import android.util.Log

/**
 * Created by Zfanji on 2017/5/31. Applicaton单例化
 */
class APP:Application(){
    companion object{
        private var instance: Application? = null
        fun instance() = instance!!
        fun test(){
            Log.d("xxx","xxxx test")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}