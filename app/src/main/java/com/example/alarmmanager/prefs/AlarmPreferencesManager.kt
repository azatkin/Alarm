package com.example.alarmmanager.prefs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.alarmmanager.AlarmReceiver
import com.example.alarmmanager.entity.AlarmData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmPreferencesManager(private val context: Context) {
    private lateinit var alarmIntent: PendingIntent
    private var alarmManager: AlarmManager? = null
    private val sharedPreferences = context.getSharedPreferences("Alarm_list", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAlarm(alarm: AlarmData) {
        val json = gson.toJson(getAlarms().plus(alarm))
        sharedPreferences.edit().putString("alarms", json).apply()
    }

    fun getAlarms(): List<AlarmData> {
        val json = sharedPreferences.getString("alarms", null)
        val type = object : TypeToken<List<AlarmData>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun deleteAlarms(): List<AlarmData>{
        sharedPreferences.edit().remove("alarms").apply()
        return emptyList()
    }

    fun enableAlarm(alarm: AlarmData){
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }

}