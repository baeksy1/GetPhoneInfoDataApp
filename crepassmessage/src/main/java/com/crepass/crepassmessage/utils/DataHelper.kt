package com.crepass.crepassmessage.utils

import com.crepass.crepassmessage.models.MMSMessage
import com.crepass.crepassmessage.models.SMSMessage
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
}