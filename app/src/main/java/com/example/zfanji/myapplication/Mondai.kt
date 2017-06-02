package com.example.zfanji.myapplication

/**
 * Created by Zfanji on 2017/5/26. 数据类！  会自动生成 equals()/hashCode() pair,
 * 数据类对象必须满足一下要求：
 * 1，构造函数必须至少有一个参数
 * 2。所有的构造函数参数必须标注val或者var
 * 3。数据类对象不能是 abstract, open, sealed or inner;
 * 4.数据类对象不能继承其他类，但是可以实现接口
 * 5。在Java虚拟机里，如果生成的类需要有一个无参数的构造函数，所有属性的默认值必须有一个具体的值，
 */
data class Mondai(var index: Int = 0,
                  var title: String = ""){

    override fun hashCode(): Int {
        return super.hashCode()
    }
}