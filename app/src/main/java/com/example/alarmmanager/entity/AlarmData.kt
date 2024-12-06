package com.example.alarmmanager.entity

data class AlarmData(
    val name: String,
    val time: String,
    var isEnable: Boolean
) {
    val id  = hour * 60 + minute
}
