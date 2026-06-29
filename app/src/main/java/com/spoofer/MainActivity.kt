package com.spoofer

import android.os.Bundle
import android.os.Build
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var statusText: TextView
    private lateinit var infoText: TextView
    private lateinit var spoofBtn: Button
    private var referCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusText = findViewById(R.id.statusText)
        infoText = findViewById(R.id.infoText)
        spoofBtn = findViewById(R.id.spoofBtn)
        spoofBtn.setOnClickListener { generateNewIdentity() }
        showDeviceInfo()
    }

    private fun generateNewIdentity() {
        referCount++
        val model = MODELS[Random().nextInt(MODELS.size)]
        val mfr = MFR[Random().nextInt(MFR.size)]
        val androidId = randomHex(16)
        val imei = "35" + randomNum(13)
        val serial = randomHex(8).uppercase()
        val mac = randomMac()
        val gsfId = randomHex(16)
        val identityFile = File(filesDir, "current_identity.txt")
        val content = "ANDROID_ID=$" + androidId + "\n" +
            "IMEI=$" + imei + "\n" +
            "SERIAL=$" + serial + "\n" +
            "MAC=$" + mac + "\n" +
            "MODEL=$" + model + "\n" +
            "MANUFACTURER=$" + mfr + "\n" +
            "GSF_ID=$" + gsfId
        identityFile.writeText(content)
        statusText.text = "Identity #" + referCount + " Generated"
        infoText.text = "Model: " + model + "\n" +
            "Android ID: " + androidId + "\n" +
            "IMEI: " + imei + "\n" +
            "MAC: " + mac + "\n" +
            "Serial: " + serial
        try {
            val ext = getExternalFilesDir(null)
            if (ext != null) {
                identityFile.copyTo(File(ext, "playbb_identity.txt"), overwrite = true)
            }
        } catch (e: Exception) { }
    }

    private fun showDeviceInfo() {
        val model = Build.MODEL
        val aid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        infoText.text = "Device: " + model + "\n" + "Android ID: " + aid
    }

    companion object {
        val MODELS = arrayOf(
            "SM-S928B", "Pixel 9 Pro XL", "Pixel 8 Pro",
            "Galaxy S25 Ultra", "OnePlus 13", "Xiaomi 14 Pro"
        )
        val MFR = arrayOf("samsung", "google", "oneplus", "xiaomi", "oppo", "realme")

        fun randomHex(len: Int): String {
            val c = "0123456789abcdef"
            return (1..len).map { c[Random().nextInt(c.length)] }.joinToString("")
        }
        fun randomNum(len: Int): String = (1..len).map { Random().nextInt(10) }.joinToString("")
        fun randomMac(): String = (1..6).joinToString(":") {
            String.format("%02x", Random().nextInt(256))
        }
    }
}
