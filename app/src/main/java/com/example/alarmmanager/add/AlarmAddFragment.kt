package com.example.alarmmanager.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alarmmanager.R
import com.example.alarmmanager.entity.AlarmData
import com.example.alarmmanager.list.AlarmFragment
import com.example.alarmmanager.prefs.AlarmPreferencesManager

class AlarmAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_alarm_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameAlarm = view.findViewById<EditText>(R.id.nameAlarm)

        val setAlarmButton = view.findViewById<Button>(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {
            val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
            val hour = timePicker.hour
            val minute = timePicker.minute

            val alarmData = AlarmData(nameAlarm.text.toString(), hour, minute, true)
            AlarmPreferencesManager(requireContext()).saveAlarm(alarmData)

            Toast.makeText(context, "Будильник созднай на: $hour:$minute", Toast.LENGTH_SHORT).show()

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, AlarmFragment())
                .commit()
        }

        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, AlarmFragment())
                .commit()
        }

    }

}