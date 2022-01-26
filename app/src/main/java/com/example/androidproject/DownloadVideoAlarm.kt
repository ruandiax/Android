package com.example.androidproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment.DIRECTORY_DOWNLOADS
import java.io.File


class DownloadVideoAlarm: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val path = context?.getExternalFilesDir(DIRECTORY_DOWNLOADS);
        var file = File(path, "propagandamercedes.mp4");
        file.delete();
    }


}