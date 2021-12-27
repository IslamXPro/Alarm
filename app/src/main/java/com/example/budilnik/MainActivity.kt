package com.example.budilnik

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.budilnik.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var alarmManager: AlarmManager? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sdf = SimpleDateFormat("HH:mm",Locale.getDefault())

        binding.setAlarm.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set time for alarm")
                .build()

            materialTimePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.SECOND,0)
                calendar.set(Calendar.MILLISECOND,0)
                calendar.set(Calendar.MINUTE,materialTimePicker.minute)
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.hour)

                alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis,getAlarmInfo())
                alarmManager!!.setAlarmClock(alarmInfo,getActionPending())
                Toast.makeText(this, "Alarm set ${sdf.format(calendar.time)}", Toast.LENGTH_SHORT).show()
            }
            materialTimePicker.show(supportFragmentManager,"tag_picker")
        }
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getAlarmInfo(): PendingIntent? {
        val alarmInfoIntent = Intent(this,MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this,0,alarmInfoIntent,PendingIntent.FLAG_UPDATE_CURRENT)
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getActionPending() : PendingIntent{
        val intent = Intent(this,AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this,1,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}