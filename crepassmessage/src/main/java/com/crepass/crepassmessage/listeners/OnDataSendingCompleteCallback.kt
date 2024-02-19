package com.crepass.crepassmessage.listeners

interface OnDataSendingCompleteCallback {
    fun onDataSendingSuccess()

    fun onDataSendingError(statusCode: Int, errorMessage: String?)

    fun onDataSendingFailed(t: Throwable?)

    fun onDataSendingStart()
}