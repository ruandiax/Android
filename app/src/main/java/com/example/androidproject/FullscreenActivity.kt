package com.example.androidproject

import android.content.pm.ActivityInfo
import android.net.Uri
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var videov: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        videov = findViewById(R.id.videoView)
        val videopath = "android.resource://com.example.androidproject/raw/testemp4"
        val uri: Uri = Uri.parse(videopath)
        videov.setVideoURI(uri)
        videov.start()

        videov.setOnCompletionListener {
            videov.start()
        }

    }

}