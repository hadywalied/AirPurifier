package com.github.hadywalied.airpurifier.domain

import com.squareup.moshi.JsonClass

var CONFIG_URL = "192.168.100.1"
var BASE_URL = "192.168.1.100"

@JsonClass(generateAdapter = true)
data class LightConfig(
    var strength: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    var mode: String
)

@JsonClass(generateAdapter = true)
data class MotionSensorConfig(var state: Boolean)

@JsonClass(generateAdapter = true)
data class IntervalsConfig(
    var mode: String,
    var onTime: Int,
    var offTime: Int,
    var period: Int,
    var power: Int
)

@JsonClass(generateAdapter = true)
data class OilData(var percentage: Int, var lifetime: Int)

@JsonClass(generateAdapter = true)
data class WifiConfig(var ssidName: String, var passwd: String)

@JsonClass(generateAdapter = true)
data class IpConfiguration(var ip: String)