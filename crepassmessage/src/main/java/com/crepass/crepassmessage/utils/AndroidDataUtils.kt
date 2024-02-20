package com.crepass.crepassmessage.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.crepass.crepassmessage.DataManager
import com.crepass.crepassmessage.client.CrepassHttpClient
import com.crepass.crepassmessage.listeners.AndroidDataResponseListener
import com.crepass.crepassmessage.listeners.BatchedQueryCallback
import com.crepass.crepassmessage.listeners.MMSDATABatchQueried
import com.crepass.crepassmessage.models.MMSMessage
import com.crepass.crepassmessage.models.SMSMessage
import com.crepass.crepassmessage.models.SentinelCounter
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidDataUtils {

    private val TAG = AndroidDataUtils::class.java.simpleName
    fun getTimeZone(): String? {
        val sdf = SimpleDateFormat("ZZZZ", Locale.US)
        return sdf.format(Date())
    }


    fun normalizePhoneNumber(srcNumber: String?): String? {
        return removeNonDigits(srcNumber)
    }
    private fun removeNonDigits(str: String?): String {
        return if (str.isNullOrEmpty()) {
            ""
        } else str.replace("\\D+".toRegex(), "")
    }

    fun hasPermission(context: Context, permissionStr: String): Boolean {
        val hasPermission = ActivityCompat.checkSelfPermission(
            context,
            permissionStr
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            Log.d("AndroidDataUtils", "Has Permission for: $permissionStr")
        } else {
            Log.e("AndroidDataUtils", "No Permission for: $permissionStr")
        }
        return hasPermission
    }



    fun sendSMSMessages(context:Context){

                if(hasPermission(context,Manifest.permission.READ_SMS)){
                    SMSManager().getSMSMessages(context.applicationContext,object :BatchedQueryCallback{
                        override fun onBatchQueried(msgs: List<SMSMessage>) {
                            Log.d("AndroidDataUtils","SMS Batch Querying")
                            DataManager.sendSMSUsers(context.applicationContext,msgs)
                        }
                    })
                }


    }
    fun sendMMSMessages(context:Context){

        if(hasPermission(context,Manifest.permission.READ_SMS)){
            MMSManager().getMMSMessages(context.applicationContext,object :MMSDATABatchQueried{
                override fun onBatchQueried(msgs: List<MMSMessage>) {
//                    Log.d("AndroidDataUtils","MMS Batch Querying")
                    DataManager.sendMMSUsers(context.applicationContext,msgs)
                }

            })
        }


    }


    fun sendSentinelMessage(context: Context) {
        val token = GetPerference().getToken(context)
        val applicationContext = context.applicationContext
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    var thread_sender_counter = 0
                    // exit poll in 20 minutes
                    // 2020.09.16 edit 20 minutes --> 10minutes (thread_sender_counter: 40 to 20)
//                    while(hasPendingDataToSend(applicationContext) && thread_sender_counter<=40) {
                    while ( thread_sender_counter <= 20) {
                        Log.d(
                            TAG,
                            "Data submission ongoing. Submission of Sentinel Message on hold."
                        )
                        thread_sender_counter++
                        sleep(30000)
                    }
                    Log.v(TAG, "Sending Sentinel Message.")
                    CrepassHttpClient().sendAndroidData("sendSnetinelMessage()",token,DataHelper().getSentinelMessageJson(context),
                        object : AndroidDataResponseListener{
                            override fun onSuccess(
                                tag: String,
                                jsonObject: JsonObject,
                                sentinelCount: Int
                            ) {
                                Log.d(
                                    TAG,
                                    "$tag Sentinel Message Submitted successfully! $jsonObject"
                                )
                                SentinelCounter().reset()
                            }

                            override fun onError(
                                tag: String?,
                                statusCode: Int,
                                rawResponse: String?,
                                sentinelCount: Int
                            ) {
                                Log.e(
                                    TAG,
                                    "$tag Sentinel Message Submitted with Error! StatusCode:$statusCode Response:$rawResponse"
                                )

                            }

                            override fun onFailure(t: Throwable?, sentinelCount: Int) {
                                Log.e(TAG, "Network Connection Failure! " + t!!.message)

                            }

                        })

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

}