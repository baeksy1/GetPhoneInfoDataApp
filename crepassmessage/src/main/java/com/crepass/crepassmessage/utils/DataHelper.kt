package com.crepass.crepassmessage.utils

import android.content.Context
import com.crepass.crepassmessage.models.MMSMessage
import com.crepass.crepassmessage.models.SMSMessage
import com.crepass.crepassmessage.models.SentinelCounter
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/** json object 변환 해주는 클래스 */
class DataHelper {
    private val KEY_TYPE = "type"
    private val KEY_DISPLAY_NAME = "displayName"
    private val KEY_THREAD_ID = "thread_id"
    private val KEY_DATE = "date"
    private val KEY_MESSAGE = "message"
    private val KEY_MESSAGE_SIZE = "message_size"
    private val MOBILE_DATA_SMS = "sms"
    private val KEY_ADDRESS = "address"
    private val KEY_ID = "id"
    private val KEY_DETAILS = "details"


    /** mms 데이터 */
    private val MOBILE_DATA_MMS = "mms"

    private val CHARSET="chset"
    private val CONTENT_DISPOSITION="cd"
    private val CONTENT_ID="cid"
    private val CONTENT_LOCATION="cl"
    private val CONTENT_TYPE="ct"
    private val CT_START="ctt_s"
    private val CT_TYPE="ctt_t"
    private val FILENAME="fn"
    private val MSG_ID="mid"
    private val NAME="name"
    private val SEQ="seq"
    private val TEXT="text"
    private val _DATA="_data"
    private val ADDRESS="address"



    fun getSMSJson(messages: List<SMSMessage>): JsonObject {
        val obj = JsonObject()
        obj.addProperty(KEY_TYPE, MOBILE_DATA_SMS)
        val messagesArray = JsonArray()
        for (message in messages) {
            val details = JsonObject()
            details.addProperty(KEY_ID, message.id)
//            if (clientOptions.isEnablePhoneNumberHashing()) {
//                details.addProperty(
//                    DataHelper.KEY_ADDRESS,
//                    AndroidDataUtils.hashPhoneNumber(message.getAddress())
//                )
//            } else {
                details.addProperty(KEY_ADDRESS, message.address)
                details.addProperty(
                    KEY_ADDRESS + "_normalized",
                    AndroidDataUtils().normalizePhoneNumber(message.address)
                )
//            }
            details.addProperty(KEY_THREAD_ID, message.threadId)
            details.addProperty(KEY_DATE, message.date)
            details.addProperty(KEY_TYPE, message.type)

            details.addProperty(KEY_MESSAGE, message.msg)
            details.addProperty(KEY_MESSAGE_SIZE, message.msg?.length ?: 0)
            details.addProperty("date_sent", message.date_sent)
            details.addProperty("read", message.read)
            details.addProperty("seen", message.seen)
            details.addProperty("service_center", message.service_center)
            details.addProperty("subject", message.subject)
            details.addProperty("status", message.status)
            details.addProperty("error_code", message.error_code)
            messagesArray.add(details)
        }
        obj.add(KEY_DETAILS, messagesArray)
        return obj
    }
    fun getMMSJson(messages: List<MMSMessage>): JsonObject {
        val obj = JsonObject()
        obj.addProperty(KEY_TYPE, MOBILE_DATA_MMS)
        val messagesArray = JsonArray()
        for (message in messages) {
            val details = JsonObject()
            details.addProperty(KEY_ID, message.id)
//            if (clientOptions.isEnablePhoneNumberHashing()) {
//                details.addProperty(
//                    DataHelper.KEY_ADDRESS,
//                    AndroidDataUtils.hashPhoneNumber(message.getAddress())
//                )
//            } else {
            details.addProperty(KEY_ADDRESS, message.address)
            details.addProperty(
                KEY_ADDRESS + "_normalized",
                AndroidDataUtils().normalizePhoneNumber(message.address)
            )
//            }
            details.addProperty(CHARSET, message.chset)
            details.addProperty(CONTENT_DISPOSITION, message.cd)
            details.addProperty(CONTENT_ID, message.cid)
            details.addProperty(CONTENT_LOCATION, message.cl)
            details.addProperty(CONTENT_TYPE, message.ct)
            details.addProperty(CT_START, message.ctt_s)
            details.addProperty(CT_TYPE, message.ctt_t)
            details.addProperty(FILENAME, message.fn)
            details.addProperty(MSG_ID, message.mid)
            details.addProperty(NAME, message.name)
            details.addProperty(SEQ, message.seq)
            details.addProperty(TEXT, message.text)
            details.addProperty(_DATA, message._data)
            messagesArray.add(details)
        }
        obj.add(KEY_DETAILS, messagesArray)
        return obj
    }
    private fun getSentinelCount(success: Int, failed: Int): JsonObject? {
        val `object` = JsonObject()
        `object`.addProperty("success", success)
        `object`.addProperty("failed", failed)
        return `object`
    }

    fun getSentinelMessageJson(context: Context):JsonObject{
        val obj = JsonObject()
        val sentinelCounter = SentinelCounter()
        val count = JsonObject()
        count.addProperty("sms",getSentinelCount(sentinelCounter.sms_success,sentinelCounter.sms_failed).toString())
        count.addProperty("sms",getSentinelCount(sentinelCounter.mms_success,sentinelCounter.mms_failed).toString())
        val details = JsonObject()
        details.addProperty("transmission", "complete")
        details.addProperty("version", "0.1")
        details.add("count", count)

        obj.addProperty(KEY_TYPE, "sentinel")
        obj.add(KEY_DETAILS, details)
        return obj
    }

}