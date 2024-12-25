package com.example.alarmmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alarmmanager.entity.AlarmData
import com.example.alarmmanager.prefs.AlarmPreferencesManager

class AlarmViewModel(private var alarmPreferencesManager: AlarmPreferencesManager) : ViewModel() {
    private val _alarms = MutableLiveData<List<AlarmData>>()
    val alarms: LiveData<List<AlarmData>> get() = _alarms

    init {
        loadAlarms()
    }

    private fun loadAlarms(){
        _alarms.value = alarmPreferencesManager.getAlarms()
    }

    fun saveAlarm(alarm: AlarmData){
        alarmPreferencesManager.saveAlarm(alarm)
        loadAlarms()
    }

    fun deleteAllAlarms(){
        alarmPreferencesManager.deleteAlarms()
        loadAlarms()
    }

    fun toggleAlarm(alarm: AlarmData, isEnable: Boolean){
        saveAlarm(alarm.copy(isEnable = isEnable))
    }
}