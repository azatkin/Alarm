package com.example.alarmmanager.entity

data class AlarmData(
    val name: String,
    val hour:Int,
    val minute:Int,
    val isEnable:Boolean
){
    val title = "$hour:$minute"
    val id = hour * 60 + minute
}
