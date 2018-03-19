package com.vocalabs.egtest.kotlinhandcraft

fun isPositive(arg: String): Boolean{
    val argAsInt: Int = arg.toInt()
    if(argAsInt <= 0){
        return false
    }
    else{
        return true
    }
}

fun main(args: Array<String>){
    println(isPositive("2"))
}
