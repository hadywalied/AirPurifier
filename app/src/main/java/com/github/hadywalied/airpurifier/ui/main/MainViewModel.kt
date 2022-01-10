package com.github.hadywalied.airpurifier.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hadywalied.airpurifier.domain.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url


class MainViewModel : ViewModel() {

    var responselivedata = MutableLiveData<ResponseMessage>()
    var oillivedata = MutableLiveData<OilData>()
    var iplivedata = MutableLiveData<IpConfiguration>()
    var errorLivedata = MutableLiveData<String>()


    fun sendWifiConfig(wifiConfig: WifiConfig) {
        ap.setWifiConfig(wifiConfig).enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                responselivedata.postValue(response.body())
                Log.i(MainViewModel::class.java.name, "onResponse: $response")
                if (response.body()?.success == true) getIp()
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                errorLivedata.postValue(t.message)
            }

        })
    }

    fun getIp() {
        ap.getIpConfig().enqueue(object : Callback<IpConfiguration> {
            override fun onResponse(
                call: Call<IpConfiguration>,
                response: Response<IpConfiguration>
            ) {
                iplivedata.postValue(response.body())
            }

            override fun onFailure(call: Call<IpConfiguration>, t: Throwable) {
                errorLivedata.postValue(t.message)
            }

        })
    }


    fun sendLightConfigurations(lightConfig: LightConfig) {
        ap.sendLightConfig(BASE_URL + "/setRGBLEDS", lightConfig)
            .enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    responselivedata.postValue(response.body())
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    errorLivedata.postValue(t.message)
                }

            })
    }

    fun sendMotionConfigurations(motionSensorConfig: MotionSensorConfig) {
        ap.sendMotionConfig(BASE_URL + "/motionSensor", motionSensorConfig)
            .enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    responselivedata.postValue(response.body())
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    errorLivedata.postValue(t.message)
                }

            })
    }


    fun sendIntervalsConfigurations(intervalsConfig: IntervalsConfig) {
        ap.sendInterval(BASE_URL + "/intervals", intervalsConfig)
            .enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    responselivedata.postValue(response.body())
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    errorLivedata.postValue(t.message)
                }

            })
    }


    fun getOilData() {
        ap.getOil(BASE_URL + "/oil").enqueue(object : Callback<OilData> {
            override fun onResponse(
                call: Call<OilData>,
                response: Response<OilData>
            ) {
                oillivedata.postValue(response.body())
            }

            override fun onFailure(call: Call<OilData>, t: Throwable) {
                errorLivedata.postValue(t.message)
            }

        })
    }

}