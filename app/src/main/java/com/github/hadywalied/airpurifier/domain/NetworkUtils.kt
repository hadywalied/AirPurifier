package com.github.hadywalied.airpurifier.domain

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

class OkHttpHelper {
    var client: OkHttpClient = OkHttpClient()

    fun post(url: String, json: String): Call {
        val request = Request.Builder()
            .url(url)
            .post(json.toRequestBody(JSON))
            .build()

        return client.newCall(request)
    }

    fun get(url: String): Call {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        return client.newCall(request)
    }
}
