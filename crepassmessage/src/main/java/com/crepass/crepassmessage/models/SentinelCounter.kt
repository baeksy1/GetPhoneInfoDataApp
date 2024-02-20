package com.crepass.crepassmessage.models

class SentinelCounter {
    var sms_success = 0
    var mms_success = 0
    var sms_failed = 0
    var mms_failed = 0

    fun reset(){
        sms_success = 0
        mms_success = 0
        sms_failed = 0
        mms_failed = 0
    }
}