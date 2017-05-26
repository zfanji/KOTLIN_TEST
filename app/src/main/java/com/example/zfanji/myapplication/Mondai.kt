package com.example.zfanji.myapplication

/**
 * Created by Zfanji on 2017/5/26.
 */
data class Mondai(var index: Int = 0,
                  var title: String = "",
                  val ans: ArrayList<String> = ArrayList(),
                  var correct: Int = 0,
                  var comment: String = "",
                  var color: String = "",
                  private var lives: Int = 50)