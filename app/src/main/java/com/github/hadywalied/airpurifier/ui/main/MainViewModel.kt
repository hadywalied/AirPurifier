package com.github.hadywalied.airpurifier.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hadywalied.airpurifier.domain.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        server.sendLightConfig(lightConfig).enqueue(object : Callback<ResponseMessage> {
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
        server.sendMotionConfig(motionSensorConfig).enqueue(object : Callback<ResponseMessage> {
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
        server.sendInterval(intervalsConfig).enqueue(object : Callback<ResponseMessage> {
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
        server.getOil().enqueue(object : Callback<OilData> {
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