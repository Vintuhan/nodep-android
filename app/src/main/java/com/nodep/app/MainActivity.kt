package com.nodep.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        status = findViewById(R.id.status)
        btn = findViewById(R.id.btn_access)
        Blocklist.load(this)

        btn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        val on = isAccessibilityOn()
        NodepAccessibilityService.isEnabled = on
        if (on) {
            status.text = "защита включена · ${Blocklist.size()} доменов\nможно пользоваться обычным VPN"
            btn.text = "настройки спец. возможностей"
        } else {
            status.text = "защита выключена · ${Blocklist.size()} доменов в базе\nне занимает VPN — включи службу nodep"
            btn.text = "включить защиту"
        }
    }

    private fun isAccessibilityOn(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        if (!am.isEnabled) return false
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabled.contains(packageName)
    }
}
