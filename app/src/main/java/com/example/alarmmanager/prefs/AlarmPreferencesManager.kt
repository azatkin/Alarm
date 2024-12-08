package com.example.alarmmanager.prefs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmmanager.AlarmReceiver
import com.example.alarmmanager.entity.AlarmData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class AlarmPreferencesManager(private val context: Context) {
    private val calendar = Calendar.getInstance()
    private var alarmManager: AlarmManager? = null
    private val sharedPreferences = context.getSharedPreferences("Alarm_list", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAlarm(alarm: AlarmData) {
        val alarmList = getAlarms()
        val index = alarmList.map { it.id }.indexOf(alarm.id)
        val result = if (index != -1) {
            alarmList.minus(alarmList[index]).plus(alarm)
        } else {
            getAlarms().plus(alarm)
        }
        if (alarm.isEnable) {
            enableAlarm(alarm)
        } else {
            disableAlarm(alarm)
        }
        val json = gson.toJson(result)
        sharedPreferences.edit().putString("alarms", json).apply()
    }

    fun getAlarms(): List<AlarmData> {
        val json = sharedPreferences.getString("alarms", null)
        val type = object : TypeToken<List<AlarmData>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun deleteAlarms(): List<AlarmData> {
        sharedPreferences.edit().remove("alarms").apply()
        return emptyList()
    }

    private fun enableAlarm(alarm: AlarmData) {
        if (!checkExactAlarmPermission(context)) {
            Toast.makeText(context, "Нужно дать разрешение AlarmManager", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            if (context is AppCompatActivity) {
                context.startActivity(intent)
            }
            return
        }

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
        }
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
        calendar.set(Calendar.MINUTE, alarm.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
        Log.i("enable", "Будильник включен")
    }

    private fun checkExactAlarmPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }

    private fun disableAlarm(alarm: AlarmData) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context,
                alarm.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager?.cancel(alarmIntent)
        Log.i("disable","Будильник выключен")
    }

}