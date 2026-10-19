package com.moorty.voice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val platform = findViewById<Spinner>(R.id.platformSpinner)
        val account = findViewById<Spinner>(R.id.accountSpinner)
        val status = findViewById<TextView>(R.id.statusText)
        val start = findViewById<Button>(R.id.startButton)
        val test = findViewById<Button>(R.id.testVoiceButton)

        platform.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("TikTok LIVE", "YouTube LIVE"))
        account.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("@moorty_yt", "@tocolita"))
        platform.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                account.isEnabled = position == 0
                if (position == 1) account.setSelection(0)
                status.text = if (position == 0) "TikTok seleccionado. La conexión LIVE aún debe integrarse." else "YouTube seleccionado. La conexión LIVE aún debe integrarse."
            }
        })

        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        test.setOnClickListener {
            val message = if (platform.selectedItemPosition == 0) "Prueba de voz de Moorty Voice para TikTok." else "Prueba de voz de Moorty Voice para YouTube."
            startVoiceService(message)
            status.text = "Prueba de voz enviada al teléfono."
        }

        start.text = "INICIAR SERVICIO DE VOZ (PRUEBA)"
        start.setOnClickListener {
            val selectedPlatform = platform.selectedItem.toString()
            val selectedAccount = account.selectedItem.toString()
            val events = buildList {
                if (findViewById<CheckBox>(R.id.commentsCheck).isChecked) add("comentarios")
                if (findViewById<CheckBox>(R.id.giftsCheck).isChecked) add("regalos")
                if (findViewById<CheckBox>(R.id.likesCheck).isChecked) add("likes")
                if (findViewById<CheckBox>(R.id.followsCheck).isChecked) add("seguidores")
                if (findViewById<CheckBox>(R.id.sharesCheck).isChecked) add("compartidos")
                if (findViewById<CheckBox>(R.id.joinsCheck).isChecked) add("entradas")
            }
            val detail = "Configuración guardada en pantalla: $selectedPlatform, $selectedAccount. Eventos seleccionados: ${events.joinToString()}. Esta versión aún no se conecta a los eventos LIVE."
            startVoiceService("Moorty Voice está listo para una prueba de voz.")
            status.text = detail
        }
    }

    private fun startVoiceService(message: String) {
        val intent = Intent(this, VoiceService::class.java).putExtra(VoiceService.EXTRA_SPEAK, message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
    }
}
