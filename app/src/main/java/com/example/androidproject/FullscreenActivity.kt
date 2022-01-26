package com.example.androidproject

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.RequiresApi
import java.util.*
import android.app.DownloadManager

import android.os.Environment
import android.app.DownloadManager.Request
import android.webkit.CookieManager
import android.webkit.URLUtil


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var videov: VideoView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Screen Orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Tela sempre ligada
        window.setFlags(LayoutParams.FLAG_KEEP_SCREEN_ON, LayoutParams.FLAG_KEEP_SCREEN_ON)

        videov = findViewById(R.id.videoView)
        val videopath = "android.resource://com.example.androidproject/raw/propagandamercedes"
        val uri: Uri = Uri.parse(videopath)
        videov.setVideoURI(uri)
        videov.start()

        videov.setOnCompletionListener {
            videov.start()
        }

        // Tentativa de baixar video
        var calendar: Calendar = Calendar.getInstance()


        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            19,
            25,
            0
        )

        setAlarm(calendar.timeInMillis)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(timeInMillis: Long) {
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(this, DownloadVideoAlarm::class.java)

        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        // We doing this too stop user from exiting app, normally.
        var url: String = "https://github.com/reesiqueira/pernshealerdownload/archive/refs/heads/main.zip"

        var request: DownloadManager.Request = Request(Uri.parse(url))

        var title: String = URLUtil.guessFileName(url, null, null)

        // only download via WIFI
        request.setAllowedNetworkTypes(Request.NETWORK_WIFI)
        request.setTitle(title)
        request.setDescription("Downloading")
        var cookie: String = CookieManager.getInstance().getCookie(url)
        request.addRequestHeader("cookie", cookie)
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)

        var downloadManager: DownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)


        Toast.makeText(this, "Download Start", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        val activityManager: ActivityManager = applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        activityManager.moveTaskToFront(taskId, 0)
    }

}