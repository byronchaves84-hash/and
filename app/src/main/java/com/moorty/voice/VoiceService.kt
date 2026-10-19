package com.moorty.voice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceService : Service(), TextToSpeech.OnInitListener {
    companion object {
        const val EXTRA_SPEAK = "com.moorty.voice.SPEAK"
        private const val CHANNEL_ID = "moorty_voice_channel"
        private const val NOTIFICATION_ID = 801
    }

    private var tts: TextToSpeech? = null
    private var ready = false
    private var pendingText: String? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        tts = TextToSpeech(applicationContext, this)
        startForeground(NOTIFICATION_ID, buildNotification("Servicio de voz activo; monitor LIVE no conectado"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra(EXTRA_SPEAK)?.let { speak(it) }
        return START_NOT_STICKY
    }

    private fun speak(text: String) {
        if (ready) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "moorty-${System.currentTimeMillis()}")
        else pendingText = text
    }

    override fun onInit(status: Int) {
        ready = status == TextToSpeech.SUCCESS
        if (ready) {
            tts?.language = Locale("es", "ES")
            pendingText?.let { speak(it); pendingText = null }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Moorty Voice", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(text: String): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID).setContentTitle("Moorty Voice").setContentText(text)
                .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off).build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this).setContentTitle("Moorty Voice").setContentText(text)
                .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off).build()
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
