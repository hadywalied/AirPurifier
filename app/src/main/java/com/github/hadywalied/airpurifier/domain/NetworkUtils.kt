package com.github.hadywalied.airpurifier.domain

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

/**
 * Configuration For the Server
 */
interface NetworkInterface {
    @POST("/setRGBLEDS")
    fun sendLightConfig(@Body lightConfig: LightConfig): Call<ResponseMessage>

    @POST("/motionSensor")
    fun sendMotionConfig(@Body motionSensorConfig: MotionSensorConfig): Call<ResponseMessage>

    @POST("/interavals")
    fun sendInterval(@Body intervalsConfig: IntervalsConfig): Call<ResponseMessage>

    @GET("/oil")
    fun getOil(): Call<OilData>
}

/**
 * Configuration of the AP
 */
interface ApInterface {
    @POST("/wifiConfig")
    fun setWifiConfig(@Body wifiConfig: WifiConfig): Call<ResponseMessage>

    @GET("/getIp")
    fun getIpConfig(): Call<IpConfiguration>
}

private val moshi = Moshi.Builder().add(
    PolymorphicJsonAdapterFactory.of(Entities::class.java, "type")
        .withSubtype(LightConfig::class.java, DataType.light.name)
        .withSubtype(MotionSensorConfig::class.java, DataType.motion.name)
        .withSubtype(IntervalsConfig::class.java, DataType.interval.name)
        .withSubtype(OilData::class.java, DataType.oil.name)
        .withSubtype(WifiConfig::class.java, DataType.wifiConfig.name)
        .withSubtype(IpConfiguration::class.java, DataType.ipConfig.name)
        .withSubtype(ResponseMessage::class.java, DataType.response.name)
).add(KotlinJsonAdapterFactory()).build()

val httpLogging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
val httpClient = OkHttpClient.Builder().addInterceptor(httpLogging).build()

var retrofitAp = Retrofit.Builder()
    .baseUrl(CONFIG_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(httpClient)
    .build()

var retrofitServer = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(httpClient)
    .build()

var server = retrofitServer.create<NetworkInterface>(NetworkInterface::class.java)
var ap = retrofitAp.create<ApInterface>(ApInterface::class.java)


