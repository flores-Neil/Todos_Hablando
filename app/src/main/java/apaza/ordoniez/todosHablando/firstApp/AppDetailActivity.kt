package apaza.ordoniez.todosHablando.firstApp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
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

class AppDetailActivity : AppCompatActivity() {

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRecord: Button
    private lateinit var btnSave: Button
    private lateinit var btnListen: Button
    private var isRecording = false
    private var audioFilePath: String = ""

    private val REQUEST_CODE_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        val appName = intent.getStringExtra("APP_NAME") ?: "default"
        val appPackageName = intent.getStringExtra("APP_PACKAGE_NAME")
        val appIcon = intent.getParcelableExtra<Bitmap>("APP_ICON")

        val appNameTextView = findViewById<TextView>(R.id.app_name)
        val appIconImageView = findViewById<ImageView>(R.id.app_icon)
        btnRecord = findViewById(R.id.btnRecord)
        btnListen = findViewById(R.id.btnListen)
        progressBar = findViewById(R.id.progressBar)
        btnSave = findViewById(R.id.btnSave)

        appNameTextView.text = appName
        if (appIcon != null) {
            appIconImageView.setImageBitmap(appIcon)
        }

        // Define el path del archivo de audio usando el nombre de la aplicación
        audioFilePath = "${externalCacheDir?.absolutePath}/${appName}_audio.3gp"

        btnRecord.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                if (checkPermissions()) {
                    startRecording()
                }
            }
        }

        btnListen.setOnClickListener {
            if (appName != null) {
                playRecording(appName)
            }
        }

        btnSave.setOnClickListener {
            if (appName != null) {
                saveRecording(appName)
            } else {
                Toast.makeText(this, "Error: Nombre de la aplicación no encontrado", Toast.LENGTH_SHORT).show()
            }
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

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
            try {
                prepare()
                start()
                isRecording = true
                progressBar.visibility = ProgressBar.VISIBLE
                Toast.makeText(this@AppDetailActivity, "Grabación iniciada", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this@AppDetailActivity, "Error al grabar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder.apply {
            stop()
            release()
        }
        isRecording = false
        progressBar.visibility = ProgressBar.GONE
        Toast.makeText(this, "Grabación detenida", Toast.LENGTH_SHORT).show()
    }

    private fun playRecording(appName: String) {
        val recording = AudioManager.getAudioRecord(appName)
        if (recording != null) {
            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(recording.audioFilePath)
                    prepare()
                    start()
                    Toast.makeText(this@AppDetailActivity, "Reproducción iniciada", Toast.LENGTH_SHORT).show()
                    setOnCompletionListener {
                        Toast.makeText(this@AppDetailActivity, "Reproducción completada", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@AppDetailActivity, "Error al reproducir: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Grabación no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveRecording(appName: String) {
        val recording = AppAudioRecord(
            packageName = appName,
            audioFilePath = audioFilePath
        )
        AudioManager.addAudioRecord(appName, audioFilePath)
        Toast.makeText(this, "Se guardó correctamente", Toast.LENGTH_SHORT).show()
    }
}
