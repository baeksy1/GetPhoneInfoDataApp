package com.crepass.crepassmessage.listeners

import com.crepass.crepassmessage.models.MMSMessage

interface MMSDATABatchQueried {
    fun onBatchQueried(msgs:List<MMSMessage>)
}