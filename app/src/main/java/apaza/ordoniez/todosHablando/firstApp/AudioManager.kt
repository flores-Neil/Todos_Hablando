package apaza.ordoniez.todosHablando.firstApp

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import java.io.IOException

object AudioManager {

    private val audioRecords = mutableMapOf<String, AppAudioRecord>()

    fun addAudioRecord(packageName: String, audioFilePath: String) {
        audioRecords[packageName] = AppAudioRecord(packageName, audioFilePath)
    }

    fun getAudioRecord(packageName: String): AppAudioRecord? {
        return audioRecords[packageName]
    }

    fun getAllRecords(): List<AppAudioRecord> {
        return audioRecords.values.toList()
    }

    fun startRecording(context: Context, packageName: String) {
        val audioFileName = "${packageName}_audio.3gp"
        val audioFilePath = "${context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)}/${audioFileName}"

        val mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // Guardar el MediaRecorder en una variable global o similar si es necesario para detenerlo más tarde
    }

    fun stopRecording(mediaRecorder: MediaRecorder) {
        mediaRecorder.apply {
            stop()
            release()
        }
    }

    fun playRecording(context: Context, packageName: String) {
        val recording = getAudioRecord(packageName)
        if (recording != null) {
            val mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(recording.audioFilePath)
                    prepare()
                    start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            // Manejar el caso en que no se encuentre la grabación
            println("No se encontró la grabación para el paquete: $packageName")
        }
    }
}

data class AudioRecord(val packageName: String, val audioFilePath: String)
