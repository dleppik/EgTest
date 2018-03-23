package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

 class ClassBuild(var name: String, var properties: List<KType>): AbstractCodeBuilding(), ClassBuilder {
     var classSignature: String = ""
     var classBody: String = ""

     fun addClass(){
        classSignature = "class $name("
        val iter = properties.iterator()
        var index = 1
        for (t in iter){
            val nextType = iter.next()
            classSignature = "$classSignature(field$index : $nextType"
            if (index != properties.lastIndex){
                classSignature += ", "
            }
            index++
        }
         classSignature += ")"
     }

     fun addClassBody(bodyToBeAdded: String){
        classBody += bodyToBeAdded
     }

     fun build() = "$classSignature {\n$classBody\n}\n"
 }
