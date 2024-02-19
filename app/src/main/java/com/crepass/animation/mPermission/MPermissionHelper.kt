package com.crepass.animation.mPermission

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crepass.animation.MainActivity
import com.crepass.crepassmessage.workers.InitialDataManager.submitInitialDataRequest

object MPermissionHelper {

    const val MY_PERMISSIONS_REQUEST_READ_SMS = 1
    private val PERMISSIONS = arrayOf(
//        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )
    val permissionsNeeded: MutableList<String> = ArrayList()
    fun checkSmsData(activity: MainActivity,dataType:String) {

        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(permission)
            }
        }

        if (!permissionsNeeded.isNullOrEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsNeeded.toTypedArray(),
                MY_PERMISSIONS_REQUEST_READ_SMS
            )
        } else {
                submitInitialDataRequest(activity.applicationContext,dataType)
        }


    }

}

