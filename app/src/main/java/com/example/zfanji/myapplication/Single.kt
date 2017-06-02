package com.example.zfanji.myapplication

/**
 * Created by Zfanji on 2017/5/31.  单例
 */
class Single private constructor() {

    //companion修饰的对象为伴生对象
    companion object {
        fun get():Single{
            return Holder.instance
        }
    }

    private object Holder {
        val instance = Single()
    }
}