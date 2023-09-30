package overlayj.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

private val configFilename = "overlayj.json"

fun read(): ConfigData {
    val jsonString = File(configFilename).inputStream().readBytes().toString(Charsets.UTF_8)
    return Json.decodeFromString<ConfigData>(jsonString)
}

@Serializable
data class ConfigData(
    var hideOnADS: Boolean,
    var adsButton: Int,
    var crosshairs: List<ConfigCrosshair>)

@Serializable
data class ConfigCrosshair(
    var id: String,
    var name: String,
    var layers: List<ConfigCrosshairLayer>
)

@Serializable
data class ConfigCrosshairLayer(
    var line: ConfigCrosshairLayerLine,
    var dot: ConfigCrosshairLayerDot
)

@Serializable
data class ConfigCrosshairLayerLine(
    var top: Boolean,
    var bottom: Boolean,
    var left: Boolean,
    var right: Boolean,
    var color: String,
    var length: Int,
    var offset: Int,
    var thickness: Int
)

@Serializable
data class ConfigCrosshairLayerDot(
    var show: Boolean,
    var radius: Int,
    var filled: Boolean,
    var color: String
)