package com.example.alarmmanager.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmanager.entity.AlarmData
import com.example.alarmmanager.R


class AlarmAdapter(
    private val alarmList: List<AlarmData>,
    private val onSwitchToggled:(AlarmData) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val time: TextView = view.findViewById(R.id.time)
        val nameTime: TextView = view.findViewById(R.id.nameTime)
        val switchTime: SwitchCompat = view.findViewById(R.id.switchTime)

        fun bind(alarmData: AlarmData){
            time.text = alarmData.title
            nameTime.text = alarmData.name
            switchTime.isChecked = alarmData.isEnable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarmData = alarmList[position]
        holder.bind(alarmData)

        holder.switchTime.setOnCheckedChangeListener(null)

        holder.switchTime.setOnCheckedChangeListener{_, isChecked ->
            onSwitchToggled(alarmData.copy(isEnable = isChecked))
        }
    }

    override fun getItemCount(): Int = alarmList.size


}