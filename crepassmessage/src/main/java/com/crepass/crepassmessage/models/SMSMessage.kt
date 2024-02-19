package com.crepass.crepassmessage.models

class SMSMessage {

    var address:String?=null
    var threadId :String?=null
    var date :String?=null
    var msg :String?=null
    var type :String?=null
    var date_sent :String?=null

    var read=0
    var seen=0

    var service_center:String?=null
    var subject:String?=null

    var status = 0
    var error_code = 0

    var id: Long = 0



}