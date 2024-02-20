package com.crepass.crepassmessage.client

import android.util.Log
import com.crepass.crepassmessage.core.http.BaseUrlConfig
import com.crepass.crepassmessage.core.http.CrepassOkHttp
import com.crepass.crepassmessage.core.listeners.OnCrepassQueryCompleteListener
import com.crepass.crepassmessage.listeners.AndroidDataResponseListener
import com.crepass.crepassmessage.utils.AndroidDataUtils
import com.crepass.crepassmessage.utils.GetPerference
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.HashMap

class CrepassHttpClient {
    fun sendAndroidData(tag: String, serviceToken: String, payload: JsonObject, listener: AndroidDataResponseListener?) {


        val sentinelCount: Int
        val details = payload.get("details")
        var countHolder = 0
        if (details != null && details is JsonArray) {
            countHolder = details.size()
        }
        sentinelCount = countHolder
        val url = "$BASE_URL$ANDROID_DATA?service_token=$serviceToken"
        Log.e(TAG, "url : $url")

        val headers = HashMap<String, String>()
        //headers.put("authToken", serviceToken)

        payload.addProperty("version","0.1")
        payload.addProperty("tz", AndroidDataUtils().getTimeZone())

        CrepassOkHttp().query(url, headers,  payload.toString(),  object :
            OnCrepassQueryCompleteListener {

            override fun onComplete(rawResponse: String) {
                Log.d(TAG, "response = $rawResponse")
                if (listener != null) {
                    val response = JsonParser().parse(rawResponse).asJsonObject
                    listener.onSuccess(tag, response, sentinelCount)
                }
            }

            override fun onError(statusCode: Int, rawResponse: String) {
                listener?.onError(tag, statusCode, rawResponse, sentinelCount)
            }

            override fun onFailure(throwable: Throwable) {
                listener?.onFailure(throwable, sentinelCount)
            }
        })

    }
    companion object {
        private val TAG = CrepassHttpClient::class.java.simpleName
        var BASE_URL = BaseUrlConfig.BASEURL_PROD_APIGATEWAY
        private const val ANDROID_DATA = "/mobile/Data"
    }
}