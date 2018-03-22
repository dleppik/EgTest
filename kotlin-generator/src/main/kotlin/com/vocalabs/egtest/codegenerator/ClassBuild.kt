package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

 class ClassBuild(var name: String, var properties: List<KType>): AbstractCodeBuilding(), ClassBuilder {
     var classSignature: String = ""

     fun addClass(){
        classSignature += this.name
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
     }

     fun addClassBody(bodyToBeAdded: String){
        classSignature += bodyToBeAdded
     }
 }
