package com.crepass.crepassmessage.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager


object InitialDataManager {
    private const val WORK_SEND_INITIAL_DATA = "submit_initial_data_work"

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
    fun submitInitialDataRequest(context: Context,dataType:String) {


        val inputData= Data.Builder()
            .putString("dataType",dataType).build()

        val initialDataWorkRequest = OneTimeWorkRequest.Builder(SendInitialDataWorker::class.java)
            .setConstraints(constraints)
            .addTag(WORK_SEND_INITIAL_DATA)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(context).beginUniqueWork(WORK_SEND_INITIAL_DATA, ExistingWorkPolicy.KEEP, initialDataWorkRequest).enqueue()
    }
}