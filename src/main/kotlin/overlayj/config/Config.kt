package overlayj.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

private const val CONFIG_FILENAME = "overlayj.json"

fun read(): Config {
    val jsonString = File(CONFIG_FILENAME).inputStream().readBytes().toString(Charsets.UTF_8)
    return Json.decodeFromString<Config>(jsonString)
}

fun write(config: Config) {
    val json = Json.encodeToString(config)
    File(CONFIG_FILENAME).bufferedWriter().use { it.write(json) }
}

fun clone(config: ConfigCrosshair): ConfigCrosshair {
    val json = Json.encodeToString(config)
    return Json.decodeFromString<ConfigCrosshair>(json)
}

@Serializable
data class Config(
    var hideOnADS: Boolean,
    var adsButton: Int,
    var current: ConfigCrosshair,
    var crosshairs: MutableList<ConfigCrosshair>
)

@Serializable
data class ConfigCrosshair(
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
    var opacity: Int,
    var length: Int,
    var offset: Int,
    var thickness: Int
) {
}

@Serializable
data class ConfigCrosshairLayerDot(
    var show: Boolean,
    var radius: Int,
    var opacity: Int,
    var filled: Boolean,
    var color: String
)