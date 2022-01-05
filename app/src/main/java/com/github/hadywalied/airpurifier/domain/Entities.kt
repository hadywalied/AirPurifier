package com.github.hadywalied.airpurifier.domain

var CONFIG_URL = "192.168.100.1"
var BASE_URL = "192.168.1.100"

data class LightConfig(
    var strength: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    var mode: String
)

data class MotionSensorConfig(var state: Boolean)

data class IntervalsConfig(
    var mode: String,
    var onTime: Int,
    var offTime: Int,
    var period: Int,
    var power: Int
)

data class OilData(var percentage: Int, var lifetime: Int)

data class WifiConfig(var ssidName: String, var passwd: String)

data class IpConfiguration(var ip: String)