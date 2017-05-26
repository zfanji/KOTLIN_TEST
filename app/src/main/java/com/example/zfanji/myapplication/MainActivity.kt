package com.example.zfanji.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.io.File
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() , AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_text.text = "ano Hello kotlin!!!"

        ////////////////控件加载及使用
        buttonAsync.setOnClickListener{
            warn("setOnClickListener ~~~")
            info("setOnClickListener ~~~~")
            longToast("this is a toast")
            doAsync {
                val text = "这是耗时操作"
                uiThread {
                    main_text.text = text
                    reLayout();//加载dsl布局
                }
            }

        }
        //////////////kt 调 java
        val javaClass = JavaClass()
        javaClass.anInt = 5
        info(javaClass.anInt)

        //////////////////////空安全
        val nullable: Int? = 0
        val nonNullable: Int = 2
       // nullable.toFloat() // 编译错误
        nullable?.toFloat() // 如果null，什么都不做，否则调用toFloat
        nullable!!.toFloat() // 强制转换为非空对象，并调用toFloat；如果nullable为null，抛空指针异常
        nonNullable.toFloat() // 正确

        //////////////////////自定义Delegate
        var aInt: Int by Preference(this, "aInt", 0)
        info(aInt)//会从SharedPreference取这个数据
        aInt = aInt+9 //会将这个数据写入SharedPreference
        warn(aInt)//会从SharedPreference取这个数据

    }

    //////////////////////dsl 布局
    fun reLayout(){
        linearLayout {
            button("1").lparams(wrapContent, wrapContent){
                weight = 1f
            }
            button("2").lparams(wrapContent, wrapContent){
                weight = 1f
            }
            button("3").lparams(wrapContent, wrapContent){
                weight = 1f
            }
        }
    }

}
