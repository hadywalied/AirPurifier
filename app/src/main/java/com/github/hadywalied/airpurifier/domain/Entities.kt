package com.github.hadywalied.airpurifier.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

var CONFIG_URL = "http://192.168.4.1:80"
var BASE_URL = "http://192.168.4.1:80"

enum class DataType {
    light, motion, interval, oil, wifiConfig, ipConfig, response
}

@JsonClass(generateAdapter = true)
sealed class Entities(@Json(name = "type") val dataType: DataType)

@JsonClass(generateAdapter = true)
data class LightConfig(
    var strength: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    var mode: String
) : Entities(DataType.light)

@JsonClass(generateAdapter = true)
data class MotionSensorConfig(var state: Boolean) : Entities(DataType.motion)

@JsonClass(generateAdapter = true)
data class IntervalsConfig(
    var mode: String,
    var onTime: Int,
    var offTime: Int,
    var period: Int,
    var power: Int
) : Entities(DataType.interval)

@JsonClass(generateAdapter = true)
data class OilData(
    @Json(name = "percentage") var percentage: Int,
    @Json(name = "lifetime") var lifetime: Int
) : Entities(DataType.oil)

@JsonClass(generateAdapter = true)
data class WifiConfig(
    @Json(name = "ssid") var ssidName: String,
    @Json(name = "password") var passwd: String
) : Entities(DataType.wifiConfig)

@JsonClass(generateAdapter = true)
data class IpConfiguration(var ip: String) : Entities(DataType.ipConfig)

@JsonClass(generateAdapter = true)
data class ResponseMessage(val success: Boolean, val message: String) : Entities(DataType.response)