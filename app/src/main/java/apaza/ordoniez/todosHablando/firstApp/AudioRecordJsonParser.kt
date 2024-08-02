package apaza.ordoniez.todosHablando.firstApp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AudioRecordJsonParser {
    private val gson = Gson()

    fun toJson(audioRecords: Map<String, AppAudioRecord>): String {
        val audioRecordData = audioRecords.mapValues { (key, value) ->
            AudioRecordData(value.packageName, value.audioFilePath)
        }
        return gson.toJson(audioRecordData)
    }

    fun parse(json: String?): Map<String, AppAudioRecord> {
        if (json.isNullOrEmpty()) {
            return emptyMap()
        }
        val type = object : TypeToken<Map<String, AudioRecordData>>() {}.type
        val audioRecordData = gson.fromJson<Map<String, AudioRecordData>>(json, type)
        return audioRecordData.mapValues { (key, value) ->
            AppAudioRecord(value.packageName, value.audioFilePath)
        }
    }
}

data class AudioRecordData(val packageName: String, val audioFilePath: String)
