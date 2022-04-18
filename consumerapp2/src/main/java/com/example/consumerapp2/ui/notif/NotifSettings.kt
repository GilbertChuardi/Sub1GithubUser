package com.example.consumerapp2.ui.notif

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapp2.R
import com.example.consumerapp2.databinding.ActivityNotifSettingsBinding
import com.example.consumerapp2.ui.alarm.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class NotifSettings : AppCompatActivity(), View.OnClickListener,
    TimePickerFragment.DialogTimeListener {


    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var binding: ActivityNotifSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotifSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.settings)

        binding.btnRepeatingTime.setOnClickListener(this)
        binding.btnEnableRepeatingAlarm.setOnClickListener(this)
        binding.btnDisableRepeatingAlarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.tvRepeatingTime.text = dateFormat.format(calendar.time)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_enable_repeating_alarm -> {
                val repeatTime = binding.tvRepeatingTime.text.toString()
                val repeatMessage = "Find new user on github"
                alarmReceiver.setRepeatingAlarm(
                    this,
                    AlarmReceiver.TYPE_REPEATING,
                    repeatTime,
                    repeatMessage
                )
            }
            R.id.btn_disable_repeating_alarm -> alarmReceiver.cancelAlarm(this)
        }
    }

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }
}