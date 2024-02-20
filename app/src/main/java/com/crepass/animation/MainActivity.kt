package com.crepass.animation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.crepass.animation.databinding.ActivityMainBinding
import com.crepass.animation.mPermission.MPermissionHelper.MY_PERMISSIONS_REQUEST_READ_SMS
import com.crepass.animation.mPermission.MPermissionHelper.checkSmsData
import com.crepass.crepassmessage.workers.InitialDataManager.submitInitialDataRequest
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityMainBinding
    private lateinit var preferences:SharedPreferences
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        preferences=this.applicationContext.getSharedPreferences(
            "keyword", Context.MODE_PRIVATE
        )
        val editor:SharedPreferences.Editor=preferences.edit()

        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.SMSbutton.setOnClickListener{
            val keyword=binding.SMSText.text.toString()
            editor.putString("SMSKeyword",keyword)
            editor.commit()
            checkSmsData(this@MainActivity,"SMSData")
        }
        binding.MMSbutton.setOnClickListener{
            val keyword=binding.MMSText.text.toString()
            val token=binding.tokenText.text.toString()
            editor.putString("token",token)
            editor.putString("MMSKeyword",keyword)
            editor.commit()
            checkSmsData(this@MainActivity,"MMSData")

//            submitInitialDataRequest(applicationContext)
        }
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {




                } else {
                    // 권한 거절됨, 사용자에게 설명을 제공
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_SMS
                        )
                    ) {
                        // 여기에 왜 이 권한이 필요한지 설명하는 대화 상자를 표시

                        showAlert(
                            "SMS 권한 필요",
                            "이 기능을 사용하려면 SMS 읽기 권한이 필요합니다."
                        )

                    } else {
                        // 사용자가 '다시 묻지 않음'을 선택하고 거절함
                        // 설정으로 유도하는 안내 메시지 표시

                        showAlert(
                            "권한 필요",
                            "설정에서 SMS 권한을 허용해주세요."
                        )

                    }

                }

            }
        }

    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("설정") { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }
}