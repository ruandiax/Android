package com.example.androidproject

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var videov: VideoView
    private lateinit var gerenteDownload: DownloadManager
    private val filePath = File(Environment.getExternalStorageDirectory().toString() + "/video.jpg")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        // Deleta arquivo existente
        //val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()
        //var file = File(path, "video")
        filePath.delete()

        // Download arquivo da nuvem
        gerenteDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri2 =
            Uri.parse("https://images.ecycle.com.br/wp-content/uploads/2021/05/20195924/o-que-e-paisagem.jpg")
        val solicitacaoDownload = DownloadManager.Request(uri2)
        solicitacaoDownload.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        solicitacaoDownload.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        solicitacaoDownload.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.jpg")
        val referencia: Long = gerenteDownload.enqueue(solicitacaoDownload)

        // FullScreen
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
    }

    override fun onPause() {
        super.onPause()
        val activityManager: ActivityManager = applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        activityManager.moveTaskToFront(taskId, 0)
    }

    private fun deleteTempFolder() {
        val myDir = File(Environment.DIRECTORY_DOWNLOADS)
        val children = myDir.list()
        for (i in children.indices) {
            File(myDir, children[i]).delete()
        }
    }
}