package com.example.alarmmanager.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.alarmmanager.MainActivity
import com.example.alarmmanager.R

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer.isLooping = true // Повтор музыки
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val alarmName = intent?.getStringExtra("ALARM_NAME") ?: "Будильник"

        // Если действие - остановить музыку
        if (action == "STOP_ALARM") {
            stopMusic() // Останавливаем музыку
            stopSelf()  // Завершаем сервис
            return START_NOT_STICKY
        }

        // Иначе, начинаем воспроизведение музыки
        Log.i("AlarmService", "onStartCommand вызван с $alarmName")
        mediaPlayer.start()
        Log.i("AlarmService", "Музыка запущена")

        showNotification(alarmName)
        Log.i("AlarmService", "Уведомление показано")

        return START_STICKY
    }

    private fun showNotification(alarmName: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Добавляем PendingIntent для остановки музыки
        val stopIntent = Intent(this, AlarmService::class.java).apply {
            action = "STOP_ALARM" // Это действие будет отслеживаться в onStartCommand
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "ALARM_CHANNEL")
            .setContentTitle("Будильник")
            .setContentText("Сработал: $alarmName")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_launcher_foreground, "Остановить", stopPendingIntent) // Кнопка для остановки музыки
            .setAutoCancel(true)
            .build()

        Log.i("AlarmService", "Уведомление: $alarmName")
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()  // Останавливаем музыку при уничтожении сервиса
    }

    private fun stopMusic() {
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}