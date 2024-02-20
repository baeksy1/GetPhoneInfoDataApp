package com.crepass.crepassmessage.listeners

import com.google.gson.JsonObject

interface AndroidDataResponseListener {

    fun onSuccess(tag : String,jsonObject: JsonObject, sentinelCount:Int)

    fun onError(tag: String?, statusCode: Int, rawResponse: String?, sentinelCount: Int)

    fun onFailure(t: Throwable?, sentinelCount: Int)
}