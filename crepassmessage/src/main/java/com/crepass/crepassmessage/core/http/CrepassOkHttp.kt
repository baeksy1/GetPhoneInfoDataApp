package com.crepass.crepassmessage.core.http

import android.annotation.SuppressLint
import android.util.Log
import com.crepass.crepassmessage.core.listeners.OnCrepassQueryCompleteListener
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

private val TAG = CrepassOkHttp::class.java.simpleName

class CrepassOkHttp : CrepassHttpInterface{
    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
    private val KEEP_ALIVE_TIME = 8
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    private val mDecodeWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private var mClient: OkHttpClient? = null

    fun buildClient(): OkHttpClient? {

        if (mClient != null) return mClient
        mClient =
            OkHttpClient.Builder() //                        .addNetworkInterceptor(new LoggingInterceptor())
                .addInterceptor(GzipRequestInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //                .sslSocketFactory(getSSLContext(context).getSocketFactory())
                .build()
        return mClient
    }
    var mDecodeThreadPool = ThreadPoolExecutor(
        NUMBER_OF_CORES,
        NUMBER_OF_CORES,
        KEEP_ALIVE_TIME.toLong(),
        KEEP_ALIVE_TIME_UNIT,
        mDecodeWorkQueue
    )

    @SuppressLint("SuspiciousIndentation")
    override fun query(
        url: String,
        headers: java.util.HashMap<String, String>,
        rawRequest: String,
        listener: OnCrepassQueryCompleteListener
    ) {
        val requestTask = CrepassRequestTask(
            url, headers,
            rawRequest, listener
        )
            mDecodeThreadPool.execute(requestTask)

    }

    class CrepassRequestTask constructor(
        url: String, headers: HashMap<String, String>,
        rawRequest: String,
        listener: OnCrepassQueryCompleteListener
    ) :
        Runnable {
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        private val url: String
        private val headers: java.util.HashMap<String, String>?
        private val listener: OnCrepassQueryCompleteListener
        private val rawPostData: String

        init {
            this.headers = headers
            this.rawPostData = rawRequest
            this.url = url
            this.listener = listener
        }

        override fun run() {

            Log.d(TAG,"URL: $url")
            val request : Request.Builder = Request.Builder().url(url)
            val requestBody : RequestBody = rawPostData.toRequestBody(JSON)
            var response : Response?=null
            request.post(requestBody)
            try{
                response= CrepassOkHttp().buildClient()?.newCall(request.build())?.execute()
                Log.v(TAG, "Response: " + response.toString())
                if (response != null) {
                    if(response.isSuccessful){
                        listener.onComplete(response.body!!.string())
                    } else {
                        listener.onError(response.code, response.body!!.string())
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                listener.onFailure(e)
            }

        }
    }
}
