package com.crepass.crepassmessage.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Telephony
import android.util.Log
import com.crepass.crepassmessage.listeners.BatchedQueryCallback
import com.crepass.crepassmessage.models.MMSMessage

class MMSManager {
    private val BATCH_SIZE = 20 //끊어서 보낼 사이즈 20
    private lateinit var preferences: SharedPreferences

    @SuppressLint("Range")
    fun getMMSMessages(context: Context, batchedQueryCallback: BatchedQueryCallback) {


        preferences = context.getSharedPreferences("keyword", Context.MODE_PRIVATE)

        val phoneNumber = preferences.getString("MMSKeyword", "")

        val mmsInboxUri = Telephony.Mms.Inbox.CONTENT_URI
        val projection = arrayOf(Telephony.Mms._ID)  // MMS 메시지 ID를 가져옵니다.
        val mmsCursor = context.contentResolver.query(mmsInboxUri, projection, null, null, null)
        var mmsgs = ArrayList<MMSMessage>()

        mmsCursor?.use { cursor ->
            while (cursor.moveToNext()) {


                val messageId = cursor.getString(cursor.getColumnIndex(Telephony.Mms._ID))

                // 각 MMS 메시지 ID에 대해 주소를 검색합니다.
                context.contentResolver.query(
                    Uri.parse("content://mms/$messageId/addr"),
                    null,
                    "${Telephony.Mms.Addr.MSG_ID}=?",
                    arrayOf(messageId.toString()),
                    null
                )?.use { addrCursor ->
                    while (addrCursor.moveToNext()) {
                        val address =
                            addrCursor.getString(addrCursor.getColumnIndex(Telephony.Mms.Addr.ADDRESS))

                        // 전화번호 형식을 정규화합니다(국가 코드 등을 고려해야 할 수 있음).
                        if (address.contains(phoneNumber.toString(), true)) {


                            val mmsMsg = MMSMessage()
                            Log.d("asdasdas", address)


                            mmsMsg.address = address
                            context.contentResolver.query(
                                Uri.parse("content://mms/part"),
                                null,
                                "${Telephony.Mms.Part.MSG_ID}=?",
                                arrayOf(messageId.toString()),
                                null
                            )?.use { cursor1 ->
                                if (cursor1.moveToNext()) {
                                    while (cursor1.moveToNext()) {

                                        val colums = arrayListOf(
                                            "chset",
                                            "cd",
                                            "cid",
                                            "cl",
                                            "ct",
                                            "ctt_s",
                                            "ctt_t",
                                            "fn",
                                            "mid",
                                            "name",
                                            "seq",
                                            "_data"
                                        )
                                        mmsMsg.apply {
                                            chset = cursor1.getString(
                                                cursor1.getColumnIndex(colums[0])
                                            )
                                            cd = cursor1.getString(
                                                cursor1.getColumnIndex(colums[1])
                                            )
                                            cid =
                                                cursor1.getInt(cursor1.getColumnIndex(colums[2]))
                                            cl =
                                                cursor1.getInt(cursor1.getColumnIndex(colums[3]))
                                            ct = cursor1.getString(
                                                cursor1.getColumnIndex(colums[4])
                                            )
                                            ctt_s =
                                                cursor1.getInt(cursor1.getColumnIndex(colums[5]))
                                            ctt_t = cursor1.getString(
                                                cursor1.getColumnIndex(colums[6])
                                            )
                                            fn = cursor1.getString(
                                                cursor1.getColumnIndex(colums[7])
                                            )
                                            mid =
                                                cursor1.getInt(cursor1.getColumnIndex(colums[8]))
                                            name = cursor1.getString(
                                                cursor1.getColumnIndex(colums[9])
                                            )
                                            seq =
                                                cursor1.getInt(cursor1.getColumnIndex(colums[10]))

                                        }



                                        if (!(cursor1.getString(
                                                cursor1.getColumnIndexOrThrow(
                                                    Telephony.Mms.Part._DATA
                                                )
                                            )).isNullOrEmpty()
                                        ) {
                                            mmsMsg._data =
                                                cursor1.getString(
                                                    cursor1.getColumnIndex(
                                                        colums[11]
                                                    )
                                                )


                                        }
                                        if (!(cursor1.getString(
                                                cursor1.getColumnIndexOrThrow(
                                                    Telephony.Mms.Part.TEXT
                                                )
                                            )).isNullOrEmpty()
                                        ) {


                                        }
                                        mmsgs.add(mmsMsg)
                                        Log.d("smsData",mmsgs.toString())
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        mmsCursor?.close()
//        var cursor= context.contentResolver.query(
//            Telephony.Mms.CONTENT_URI,null,null,null,null
//        )
//
//
//
//        cursor.let {
//            if (it != null) {
//                while(it.moveToNext()){
////                    println( cursor?.getInt(cursor.getColumnIndexOrThrow(Telephony.Mms._ID)))
//                    if (cursor != null) {
////                        getSender(context, cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Mms._ID)))
//                        getBody(context, cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Mms._ID)))
//                    }
//                }
//            }
//        }

// Cursor 사용 후 닫기


    }


    private fun getSender(context: Context, id: Int) {
        context.contentResolver.query(
            Uri.parse("content://mms/$id/addr"),
            null,
            "${Telephony.Mms.Addr.MSG_ID}=?",
            arrayOf(id.toString()),
            null
        )?.use { senderAddressCursor ->
            if (senderAddressCursor.moveToNext()) {
                do {
                    val isSender = senderAddressCursor.getInt(
                        senderAddressCursor.getColumnIndexOrThrow(Telephony.Mms.Addr.TYPE)
                    ) == 137 // 151는 발신

                    if (isSender) {
                        println(
                            senderAddressCursor.getString(
                                senderAddressCursor.getColumnIndexOrThrow(
                                    Telephony.Mms.Addr.ADDRESS
                                )
                            )
                        )
                    }

                } while (senderAddressCursor.moveToNext())
            }
        }
    }


    private fun getBody(context: Context, id: Int) {
//        println("들어옴?")
        context.contentResolver.query(
            Uri.parse("content://mms/part"),
            null,
            "${Telephony.Mms.Part.MSG_ID}=?",
            arrayOf(id.toString()),
            null
        )?.use { partsCursor ->
            if (partsCursor.moveToNext()) {
                do {
                    val isSender = partsCursor.getString(
                        partsCursor.getColumnIndexOrThrow(Telephony.Mms.Part.CONTENT_TYPE)
                    ) // 151는 발신
//                    println(isSender)
                    if (isSender != "text/plain") {
                        println(partsCursor.getString(partsCursor.getColumnIndexOrThrow(Telephony.Mms.Part.TEXT)))
                    }

                } while (partsCursor.moveToNext())
            }
        }
    }


}