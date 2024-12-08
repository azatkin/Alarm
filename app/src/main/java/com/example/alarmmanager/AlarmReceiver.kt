package com.example.alarmmanager


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.alarmmanager.prefs.AlarmPreferencesManager
import com.example.alarmmanager.service.AlarmService


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId == -1) return

        val alarmPreferencesManager = AlarmPreferencesManager(context)

        // Обновляем состояние будильника
        val alarms = alarmPreferencesManager.getAlarms().toMutableList()
        val alarm = alarms.find { it.id == alarmId }
        if (alarm != null) {
            val updatedAlarm = alarm.copy(isEnable = false)
            alarmPreferencesManager.saveAlarm(updatedAlarm)
            Log.i("AlarmReceiver", "Будильник ${updatedAlarm.name} отключен")
        }

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_NAME", alarm?.name ?: "Неизвестный будильник")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}