package com.example.zfanji.myapplication

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.View
import android.view.ViewGroup
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

        APP.test()
        //////////////kt 与 java 互调
        val javaClass = JavaClass()
        javaClass.anInt = 5
        info(javaClass.anInt)
        javaClass.testJavaCallKotlin("test~")

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

        //////////////////////
        testDataClass()
        ///////////////////
        testCollection()
        ////////////////////
        testEnum()
        ///////////////
        testLambda()
        ///////////////////
        toastEx("类的扩展")
        fun Animal.bark() = "animal"
        //////////////
        var animal: Animal = Animal()
        if (animal is Animal) {//用 is 来判断一个对象是否是某个类的实例，用 as 来做强转。
            info(animal.bark())
        }

        //////接口 委托
        val dog = Dog(AnimalRun())
        dog.run()
        val cat = Cat(AnimalRun())
        cat.run()

        //////////////Ranges
        for (i in 0..10)//Ranges默认会自增长
            info("Ranges="+i)
        for(i in 10 downTo 0)//递减
            info("Ranges="+i)
        for (i in 1..4 step 2) info("step= "+i)
        for (i in 4 downTo 1 step 2) info("downTo step= "+i)
        for (i in 0 until 4) info("until= "+i)//0 until 4 == 0..3

        //////////when
        val x = 5
        when (x){
            in 1..5 -> info(" 1..5 ");
            1 -> info("x == 1")
            2 -> info("x == 2")
            else -> {
                info("I'm a block")
                info("x is neither 1 nor 2")
            }
        }
    }

    private fun testLambda() {

        //////基础Lambda表达式
        fun max(i: Int , j: Int): Boolean {//这是一个简单的比较数字大小的函数
            return i  > j
        }
        val max = {i: Int , j: Int -> i > j}// 把上面max fun 转换为Lambda表达式
        info(max(5,9))
        //////简化版本的Lambda表达式
        val sum: (Int , Int)->Int = {x , y -> x + y}// 这个简化的Lambda表达式很容易理解，Kotlin语言支持类型推导，通过前面sum变量指定的参数类型，很容易推导出x,y均为Int类型，故类型参数可以省略掉;
        info(sum(4,5))

        val ints = setOf<Int>(4,2,8,5)
        info(ints.filter { it > 4 })//Kotlin语言中有一个约定，如果Lambda表达式的参数只有一个，则可以省略参数声明语句；

        val cl: (Int)->Boolean = { it > 0 }
        info(cl(5))

    }

    //定义与java中类似，存放一些常量。定义如下：
    enum class Color{
        RED,BLACK,BLUE,GREEN,WHITE
    }
    //默认名称为枚举字符名，值从0开始。若需要指定值，则可以使用其构造函数：
    enum class Shape(value:Int){
        ovel(100),
        rectangle(200)
    }
    //同时，枚举还支持自定义方法，如：
    enum class ProtocolState {
        WAITING {
            override fun signal() = TALKING
        },

        TALKING {
            override fun signal() = WAITING
        };

        abstract fun signal(): ProtocolState
    }
    private fun testEnum() {
        var colorA:Color=Color.RED

        info("testEnum(1): "+colorA.name)////获取枚举名称
        info("testEnum(2): "+colorA.ordinal)//获取枚举值在所有枚举数组中定义的顺序

        //其他值转换成枚举值  枚举类提供两个方法：
        var colorB:Color = Color.valueOf("RED")
        //var colorB:Color = Color.valueOf("TESX")//找不到转换会报异常
        info("testEnum(3): "+colorB.toString())
        var colorC:Array<Color> = Color.values()
        colorC.forEach { info("testEnum(4): "+it.name) }
    }

    ///////////////////////使用数据类
    fun testDataClass(){
        var dai = Mondai(0,"title1")
        info(dai)
        var newDai = dai.copy(index = 1,title = "title2")//复制
        info(newDai)
        val (id,titile) = newDai//数据类和解构声明：
        info("id=$id")
        info("titile=$titile")
    }

    ////////////////////////集合
    /*
     *分类： Set（集） List（列表）Map（映射）
     *明确的区分了可变和只读的集合(list, set, map等)
     * 关键字： Iterable  MutableIterable Collection MutableCollection
     */
    fun testCollection(){

        ////////////Set  最简单的一种集合。集合中的对象不按特定的方式排序，并且没有重复对象。
        val setTea: Set<String> = setOf("E", "F", "B", "C", "A", "D", "F", "B")
        info(setTea)//1. Set中没有重复的对象  2. Set中对象不按特定的方式排序
        //Set 自定义类 判断对象是不是重复
        val daiA = Mondai(0,"title0")
        val daiB = Mondai(1,"title1")
        val daiC = Mondai(2,"title2")
        val daiD = Mondai(3,"title3")
        val daiE = Mondai(0,"title1")

        var setDai = setOf(daiA, daiB, daiC, daiD, daiE)////只读Set
        info(setDai.size)
        for (item in setDai) {
            info("item:$item , hashCode: ${item.hashCode()}")
        }

        var setMulable = mutableSetOf(daiA, daiB, daiC, daiD, daiE) ////////////可变Set
        setMulable.add(Mondai(20, "Floor"))
        setMulable.remove(daiA)
        for (item in setMulable) {
            info("item:$item , hashCode: ${item.hashCode()}")
        }

        ////////////List 特征是其元素以线性方式存储，集合中可以存放重复对象。
        var listDai = listOf<Mondai>(daiA,daiB,daiC,daiD,daiE,daiC)//只读List
        info(listDai.get(0))// 获取位置0处的元素
        listDai.indexOf(daiC)// 获取bookA第一次出现的位置
        listDai.lastIndexOf(daiC)// 获取bookA最后一次出现的位置
        listDai.forEach { info("foreach listDai->"+it.toString()) }
        for (dai in listDai) {
            info("for listDai->"+dai.toString())
        }
        var iterator = listDai.listIterator()
        while (iterator.hasNext()) {
            info("listDai.listIterator->"+iterator.next().toString())
        }
        val _items = mutableListOf<Mondai>(daiA,daiB,daiC,daiD,daiE,daiC)//可变List
        val daiF = Mondai(6,"titlef")
        _items.add(daiF)
        val itemsRead: List<Mondai> = _items.toList()
        info("itemRead-> "+itemsRead.toString())
        ////////////Map 是一种把键对象和值对象映射的集合  分为只读map和可变两种Map 对应 mapOf(),mutableMapOf()
        val mapEmpty = emptyMap<Int, Mondai>()
        val map= mapOf<Int, Mondai>(1 to daiA, 2 to daiB, 3 to daiC, 4 to daiD, 5 to daiE)
        var mapMutable = mutableMapOf(1 to daiA, 2 to daiB, 3 to daiC, 4 to daiD, 5 to daiE)
        val mapPair = mapOf(Pair(1, daiA), Pair(1, daiB))//Pair类用来存储两个值，这两个值可以是任何类型的，可以用于任何场景
        val hashMap = hashMapOf(1 to daiA, 2 to daiB)//HashMap Map基于散列表的实现。插入和查询“键值对”的开销是固定的。可以通过构造器设置容量capacity和负载因子load factor，以调整容器的性能
        val linkedHashMap = linkedMapOf(1 to daiA, 2 to daiA)//LinkedHashMap 类似于HashMap，但是迭代遍历它时，取得“键值对”的顺序是其插入次序，或者是最近最少使用(LRU)的次序。只比HashMap慢一点。而在迭代访问时发而更快，因为它使用链表维护内部次序。

        val setValues = map.values
        val setKeys = map.keys
        info("setValues-> "+setValues.toString())
        info("setKeys-> "+setKeys.toString())
        mapMutable.put(6,daiF)
        mapMutable.forEach { info(" key:"+it.key+" info:  "+it.value.toString()) }
        ////////////聚合操作  http://www.cnblogs.com/figozhg/p/5031398.html
        ////////////映射操作  http://www.cnblogs.com/figozhg/p/5031398.html
        ////////////元素操作
        ////////////生成操作
        ////////////排序操作

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

    /*
        扩展方法是静态解析的，而并不是真正给类添加了这个方法。
     */
    fun Activity.toastEx(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
