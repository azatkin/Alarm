package com.example.alarmmanager.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmanager.R
import com.example.alarmmanager.add.AlarmAddFragment
import com.example.alarmmanager.prefs.AlarmPreferencesManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlarmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alarm_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmPreferencesManager = AlarmPreferencesManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recycle_view)

        val alarmList = alarmPreferencesManager.getAlarms().toMutableList()
        val adapter = AlarmAdapter(alarmList) { alarm, isChecked ->
            alarm.isEnable = isChecked
            Toast.makeText(requireContext(), "Будильник '${alarm.name}' ${if (isChecked) "включен" else "выключен"}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val button: FloatingActionButton = view.findViewById(R.id.fab)
        button.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("app")
                .replace(R.id.container, AlarmAddFragment())
                .commit()

        }

        val deleteAllFab = view.findViewById<Button>(R.id.deleteAllFab)
        deleteAllFab.setOnClickListener {
            alarmPreferencesManager.deleteAlarms()
            alarmList.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Все будильники удалены", Toast.LENGTH_SHORT).show()
        }

    }

}