package com.crepass.crepassmessage

import android.content.Context
import android.util.Log
import com.crepass.crepassmessage.client.CrepassHttpClient
import com.crepass.crepassmessage.listeners.AndroidDataResponseListener
import com.crepass.crepassmessage.models.MMSMessage
import com.crepass.crepassmessage.models.SMSMessage
import com.crepass.crepassmessage.models.SentinelCounter
import com.crepass.crepassmessage.utils.DataHelper
import com.crepass.crepassmessage.utils.GetPerference
import com.google.gson.JsonObject

object DataManager :AndroidDataResponseListener {
    private val sentinelCounter: SentinelCounter = SentinelCounter()
    private val countLock = Any()
    private var remaining_data_count = 0
    private val TAG = DataManager::class.java.name

    private fun incrementRemainingDataCount() {
        synchronized(DataManager.countLock) {//멀티스레드에서 하나만 동작하도록 하는 함수!!**
            DataManager.remaining_data_count++
            Log.d(
                DataManager.TAG,
                "incremented remaining_data_count = " + DataManager.remaining_data_count
            )
        }
    }

    private fun decrementRemainingDataCount() {
        synchronized(DataManager.countLock) {
            DataManager.remaining_data_count--
            Log.d(
                DataManager.TAG,
                "decremented remaining_data_count = " + DataManager.remaining_data_count
            )
        }
    }

    fun sendSMSUsers(context:Context, messagesChunk : List<SMSMessage>){
        Log.d("dataManager","Queue Sending sms")
        val token = GetPerference().getToken(context)

        CrepassHttpClient().sendAndroidData("sendUsersSMS()",token,DataHelper().getSMSJson(messagesChunk),object : AndroidDataResponseListener{
            override fun onSuccess(tag: String, jsonObject: JsonObject, sentinelCount: Int) {
                sentinelCounter.sms_success=sentinelCounter.sms_success+sentinelCount
            }

            override fun onError(
                tag: String?,
                statusCode: Int,
                rawResponse: String?,
                sentinelCount: Int
            ) {
                sentinelCounter.sms_failed=sentinelCounter.sms_failed+sentinelCount

            }

            override fun onFailure(t: Throwable?, sentinelCount: Int) {
                sentinelCounter.sms_failed=sentinelCounter.sms_failed+sentinelCount
            }

        })
    }
    fun sendMMSUsers(context:Context, messagesChunk : List<MMSMessage>){
        Log.d("dataManager","Queue Sending mms")
        val token = GetPerference().getToken(context)

        CrepassHttpClient().sendAndroidData("sendUsersSMS()",token,DataHelper().getMMSJson(messagesChunk),object : AndroidDataResponseListener{
            override fun onSuccess(tag: String, jsonObject: JsonObject, sentinelCount: Int) {
                sentinelCounter.mms_success=sentinelCounter.mms_success+sentinelCount
            }

            override fun onError(
                tag: String?,
                statusCode: Int,
                rawResponse: String?,
                sentinelCount: Int
            ) {
                sentinelCounter.mms_failed=sentinelCounter.mms_failed+sentinelCount

            }

            override fun onFailure(t: Throwable?, sentinelCount: Int) {
                sentinelCounter.mms_failed=sentinelCounter.mms_failed+sentinelCount
            }

        })


    }

    override fun onSuccess(tag: String, jsonObject: JsonObject, sentinelCount: Int) {
        Log.d(
            TAG,
            "$tag AndroidDataResponseListener Success: $jsonObject Count: $sentinelCount"
        )
        decrementRemainingDataCount()
    }

    override fun onError(tag: String?, statusCode: Int, rawResponse: String?, sentinelCount: Int) {
        Log.e(
            TAG,
            "$tag AndroidDataResponseListener Error : StatusCode:$statusCode Count: $sentinelCount response:$rawResponse"
        )
        decrementRemainingDataCount()
    }

    override fun onFailure(t: Throwable?, sentinelCount: Int) {
        Log.e(TAG, "Network failure! " + t!!.message + " Count: " + sentinelCount)
        decrementRemainingDataCount()
    }
}
