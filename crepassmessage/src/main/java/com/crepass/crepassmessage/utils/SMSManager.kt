package com.crepass.crepassmessage.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Telephony
import android.util.Log
import com.crepass.crepassmessage.listeners.BatchedQueryCallback
import com.crepass.crepassmessage.models.SMSMessage

class SMSManager {

    private val BATCH_SIZE = 20 //끊어서 보낼 사이즈 20
    private lateinit var preferences: SharedPreferences
    @SuppressLint("Range")
    fun getSMSMessages(context: Context, batchedQueryCallback: BatchedQueryCallback) {

        preferences=context.getSharedPreferences("keyword",Context.MODE_PRIVATE)

        val keyword=preferences.getString("SMSKeyword","")

        val content_uris = arrayListOf<String>()
        content_uris.add("content://sms/inbox")//수신
        content_uris.add("content://sms/sent")//발신

        for (uri in content_uris) {

            val mSmsQueryUri = Uri.parse(uri)
            val cursor =
                context.contentResolver.query(
                    mSmsQueryUri,
                    null,
                    null,
                    null,
                    null)
            if (cursor != null) {
                val colums = arrayListOf(
                    "_id",
                    "address",
                    "thread_id",
                    "date",
                    "body",
                    "type",
                    "person",
                    "date_sent",
                    "read",
                    "seen",
                    "service_center",
                    "subject",
                    "status",
                    "error_code"
                )
                var msgs = ArrayList<SMSMessage>()

                cursor.let {
                    var batchCount = 0
                    while (it.moveToNext()) {
                        val smsMsg = SMSMessage()

                        smsMsg.apply {

                            id = it.getLong(it.getColumnIndex(colums[0]))
                            address = it.getString(it.getColumnIndex(colums[1]))
                            threadId = it.getString(it.getColumnIndex(colums[2]))
                            date = it.getString(it.getColumnIndex(colums[3]))
                            msg = it.getString(it.getColumnIndex(colums[4]))
                            type = smsTypeToString(it.getString(it.getColumnIndex(colums[5])))

                            date_sent = it.getString(it.getColumnIndex(colums[7]))
                            read = it.getInt(it.getColumnIndex(colums[8]))
                            seen = it.getInt(it.getColumnIndex(colums[9]))
                            service_center = it.getString(it.getColumnIndex(colums[10]))

                            subject = it.getString(it.getColumnIndex(colums[11]))
                            status = it.getInt(it.getColumnIndex(colums[12]))
                            error_code = it.getInt(it.getColumnIndex(colums[13]))

                        }
                        if((smsMsg.address?.contains(keyword.toString(),true)!=true)){
                            continue
                        }
                        msgs.add(smsMsg)

                        batchCount++

                        if (batchCount >= BATCH_SIZE) {
                            batchedQueryCallback.onBatchQueried(msgs)
                            msgs = ArrayList()
                            batchCount = 0
                        }

                    }
                    if (batchCount > 0) {
                        batchedQueryCallback.onBatchQueried(msgs)

                    }
                    it.close()
                }


            }


        }


    }

    private fun smsTypeToString(typeStr: String): String {
        return  when (typeStr.toInt()) {
            Telephony.Sms.MESSAGE_TYPE_ALL -> "all"
            Telephony.Sms.MESSAGE_TYPE_INBOX -> "inbox"
            Telephony.Sms.MESSAGE_TYPE_SENT -> "sent"
            Telephony.Sms.MESSAGE_TYPE_DRAFT -> "draft"
            Telephony.Sms.MESSAGE_TYPE_OUTBOX -> "outbox"
            Telephony.Sms.MESSAGE_TYPE_FAILED -> "failed"
            Telephony.Sms.MESSAGE_TYPE_QUEUED -> "queued"
            else ->  "unknown"
        }
    }

}