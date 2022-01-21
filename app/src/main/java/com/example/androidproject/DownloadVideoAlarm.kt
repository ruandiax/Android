package com.example.androidproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings

class DownloadVideoAlarm: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var mediaPlayer: MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
    }

}