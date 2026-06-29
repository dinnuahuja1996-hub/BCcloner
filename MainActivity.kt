cat > app/src/main/java/com/spoofer/MainActivity.kt << 'KTFILE'
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
        
        spoofBtn.setOnClickListener {
            generateNewIdentity()
        }
        
        showDeviceInfo()
    }
    
    private fun generateNewIdentity() {
        referCount++
        
        // Generate completely new random identity
        val model = MODELS[Random().nextInt(MODELS.size)]
        val mfr = MFR[Random().nextInt(MFR.size)]
        val androidId = randomHex(16)
        val imei = "35" + randomNum(13)
        val serial = randomHex(8).uppercase()
        val mac = randomMac()
        val btMac = randomMac().uppercase()
        val gsfId = randomHex(16)
        
        // Save all this to a file that PlayBB can read
        val identityFile = File(filesDir, "current_identity.txt")
        identityFile.writeText("""
ANDROID_ID=$androidId
IMEI=$imei
SERIAL=$serial
MAC=$mac
BT_MAC=$btMac
MODEL=$model
MANUFACTURER=$mfr
GSF_ID=$gsfId
TIMESTAMP=$System.currentTimeMillis()
""")
        
        statusText.text = "✅ NEW IDENTITY GENERATED #$referCount"
        infoText.text = """
Model: $model
Android ID: $androidId
IMEI: $imei
MAC: $mac
Serial: $serial

Use this identity for PlayBB install.
        """.trimIndent()
        
        // Copy to shared storage so PlayBB can access
        try {
            val sharedFile = File(getExternalFilesDir(null), "playbb_identity.txt")
            identityFile.copyTo(sharedFile, overwrite = true)
        } catch(e: Exception) {}
    }
    
    private fun showDeviceInfo() {
        infoText.text = """
Original Device:
Model: ${android.os.Build.MODEL}
Android ID: ${android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID)}
        """.trimIndent()
    }
    
    companion object {
        val MODELS = arrayOf("SM-S928B","Pixel 9 Pro XL","Pixel 8 Pro","Galaxy S25 Ultra",
            "OnePlus 13","Xiaomi 14 Pro","Nothing Phone 3","Oppo Find X8 Pro",
            "CPH2499","RMX3700","V2029","2211133G","iPhone16,1","iPhone15,3")
        val MFR = arrayOf("samsung","google","oneplus","xiaomi","oppo","nothing","realme","vivo","apple")
        
        fun randomHex(len: Int): String {
            val chars = "0123456789abcdef"
            return (1..len).map { chars[Random().nextInt(chars.length)] }.joinToString("")
        }
        
        fun randomNum(len: Int): String {
            return (1..len).map { Random().nextInt(10).toString() }.joinToString("")
        }
        
        fun randomMac(): String {
            return (1..6).joinToString(":") { 
                String.format("%02x", Random().nextInt(256))
            }
        }
    }
}
KTFILE
