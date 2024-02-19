package com.crepass.crepassmessage.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.crepass.crepassmessage.utils.AndroidDataUtils
import java.lang.Exception

class SendInitialDataWorker(context:Context,workerParams: WorkerParameters):
    Worker(context,workerParams) {
    override fun doWork(): Result {
        val context=applicationContext
       return try {

           if(inputData.getString("dataType")=="SMSData"){
               AndroidDataUtils().sendSMSMessages(context)
           }else{
               AndroidDataUtils().sendMMSMessages(context)
           }
            Result.success()
        }catch (e:Exception){
            e.printStackTrace()
            Result.failure()
        }


    }


}