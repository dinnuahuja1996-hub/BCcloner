package com.spoofer

  import android.os.Bundle
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
          identityFile.writeText("ANDROID_ID=$androidId
IMEI=$imei
SERIAL=$serial
MAC=$mac
MODEL=$model
MANUFACTURER=$mfr
GSF_ID=$gsfId")
          statusText.text = "Identity #$referCount Generated"
          infoText.text = "Model: $model
Android ID: $androidId
IMEI: $imei
MAC: $mac
Serial: $serial"
          try { File(getExternalFilesDir(null), "playbb_identity.txt").also { identityFile.copyTo(it, overwrite = true) } } catch(e: Exception) {}
      }

      private fun showDeviceInfo() {
          infoText.text = "Device: ${android.os.Build.MODEL}
Android ID: ${android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID)}"
      }

      companion object {
          val MODELS = arrayOf("SM-S928B","Pixel 9 Pro XL","Pixel 8 Pro","Galaxy S25 Ultra","OnePlus 13","Xiaomi 14 Pro")
          val MFR = arrayOf("samsung","google","oneplus","xiaomi","oppo","realme")
          fun randomHex(len: Int): String { val c = "0123456789abcdef"; return (1..len).map { c[Random().nextInt(c.length)] }.joinToString("") }
          fun randomNum(len: Int): String = (1..len).map { Random().nextInt(10).toString() }.joinToString("")
          fun randomMac(): String = (1..6).joinToString(":") { String.format("%02x", Random().nextInt(256)) }
      }
  }