package com.example.androidproject

import android.app.ActivityManager
import android.app.AlarmManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.app.KeyguardManager.KeyguardLock

import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    // lateinit var timePicker: TimePicker;
    private lateinit var videov: VideoView;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // timePicker = findViewById<TimePicker>(R.id.time);

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars());
        } else {
            window.setFlags(
                LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN
            );
        }

        // Screen Orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        // Tela sempre ligada
        window.setFlags(LayoutParams.FLAG_KEEP_SCREEN_ON, LayoutParams.FLAG_KEEP_SCREEN_ON);

        videov = findViewById(R.id.videoView);
        val videopath = "android.resource://com.example.androidproject/raw/propagandamercedes";
        val uri: Uri = Uri.parse(videopath);
        videov.setVideoURI(uri);
        videov.start();

        videov.setOnCompletionListener {
            videov.start();
        }

        // Tentativa de baixar video
        var calendar: Calendar = Calendar.getInstance();


        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            1,
            30,
            0
        );
        
        setAlarm(calendar.timeInMillis);
    }

    private fun setAlarm(timeInMillis: Long) {
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(this, DownloadVideoAlarm::class.java);

        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    override fun onBackPressed() {
        // We doing this too stop user from exiting app, normally.
        // super.onBackPressed();
    }
    override fun onPause() {
        super.onPause();
        val activityManager: ActivityManager = getApplicationContext()
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;

        activityManager.moveTaskToFront(taskId, 0);
    }



}