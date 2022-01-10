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
import retrofit2.http.Url

val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

/**
 * Configuration For the Server
 */
interface NetworkInterface {
    @POST
    fun sendLightConfig(@Url baseUrl: String, @Body lightConfig: LightConfig): Call<ResponseMessage>

    @POST
    fun sendMotionConfig(@Url baseUrl: String, @Body motionSensorConfig: MotionSensorConfig): Call<ResponseMessage>

    @POST
    fun sendInterval(@Url baseUrl: String, @Body intervalsConfig: IntervalsConfig): Call<ResponseMessage>

    @GET
    fun getOil(@Url baseUrl: String): Call<OilData>

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

var retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(CONFIG_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(httpClient)
    .build()

var ap = retrofit.create(NetworkInterface::class.java)


