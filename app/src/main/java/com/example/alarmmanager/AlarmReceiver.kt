package com.example.alarmmanager


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Будильник", Toast.LENGTH_SHORT).show()
    }

}