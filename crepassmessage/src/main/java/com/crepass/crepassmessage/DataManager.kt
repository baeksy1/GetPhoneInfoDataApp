package com.crepass.crepassmessage

import android.content.Context
import android.util.Log
import com.crepass.crepassmessage.models.SMSMessage
import com.crepass.crepassmessage.utils.DataHelper

object DataManager {

    fun sendUsers(context:Context,messagesChunk : List<SMSMessage>){
        Log.d("dataManager","Queue Sending sms")
        Log.d("smsData",DataHelper().getSMSJson(messagesChunk).toString())

    }

}