package com.crepass.crepassmessage.listeners

import com.crepass.crepassmessage.models.SMSMessage

interface BatchedQueryCallback {

    fun onBatchQueried(msgs:List<SMSMessage>)

}