package apaza.ordoniez.todosHablando.firstApp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import apaza.ordoniez.todosHablando.R
import java.io.IOException

class RegistroDetailActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var progressBar: ProgressBar
    private lateinit var btnListen: Button
    private var audioFilePath: String = ""

    private val REQUEST_CODE_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_detail)

        val appName = intent.getStringExtra("APP_NAME") ?: "default"
        val appIcon = intent.getParcelableExtra<Bitmap>("APP_ICON")

        val appNameTextView = findViewById<TextView>(R.id.app_name)
        val appIconImageView = findViewById<ImageView>(R.id.app_icon)
        btnListen = findViewById(R.id.btnListen)
        progressBar = findViewById(R.id.progressBar)

        appNameTextView.text = appName
        if (appIcon != null) {
            appIconImageView.setImageBitmap(appIcon)
        }

        // Define el path del archivo de audio usando el nombre de la aplicación
        audioFilePath = "${externalCacheDir?.absolutePath}/${appName}_audio.3gp"

        btnListen.setOnClickListener {
            playRecording()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        return if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), REQUEST_CODE_PERMISSIONS)
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permisos concedidos, puedes realizar acciones que requieran permisos
            } else {
                Toast.makeText(this, "Permisos necesarios no concedidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
                Toast.makeText(this@RegistroDetailActivity, "Reproducción iniciada", Toast.LENGTH_SHORT).show()
                setOnCompletionListener {
                    Toast.makeText(this@RegistroDetailActivity, "Reproducción completada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@RegistroDetailActivity, "Error al reproducir: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
