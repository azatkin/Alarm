package com.example.alarmmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmmanager.prefs.AlarmPreferencesManager

class ViewModelFactory(private val alarmPreferencesManager: AlarmPreferencesManager)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(alarmPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}