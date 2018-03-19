package com.vocalabs.egtest.kotlinhandcraft
import java.io.File

fun isPositive(arg: String): Boolean{
    val argAsInt: Int = arg.toInt()
    if(argAsInt <= 0){
        return false
    }
    else{
        return true
    }
}

fun writeToFile(): Unit{
    File("./test.txt").bufferedWriter().use{ out -> out.write("I'm in a file again!")}
}

fun main(args: Array<String>){
   // println(isPositive("2"))
    writeToFile()
}
