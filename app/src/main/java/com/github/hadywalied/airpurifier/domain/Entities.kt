package com.github.hadywalied.airpurifier.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

var CONFIG_URL = "192.168.100.1"
var BASE_URL = "-1"

enum class DataType {
    light, motion, interval, oil, wifiConfig, ipConfig, response
}

sealed class Entities(@Json(name = "type") val dataType: DataType)

data class LightConfig(
    var strength: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    var mode: String
) : Entities(DataType.light)

data class MotionSensorConfig(var state: Boolean) : Entities(DataType.motion)

data class IntervalsConfig(
    var mode: String,
    var onTime: Int,
    var offTime: Int,
    var period: Int,
    var power: Int
) : Entities(DataType.interval)

data class OilData(var percentage: Int, var lifetime: Int) : Entities(DataType.oil)

data class WifiConfig(var ssidName: String, var passwd: String) : Entities(DataType.wifiConfig)

data class IpConfiguration(var ip: String) : Entities(DataType.ipConfig)

data class ResponseMessage(val success: Boolean, val message: String) : Entities(DataType.response)