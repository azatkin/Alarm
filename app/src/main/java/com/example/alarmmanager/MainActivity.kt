package com.example.alarmmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmmanager.list.AlarmFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AlarmFragment())
            .commit()
    }
}