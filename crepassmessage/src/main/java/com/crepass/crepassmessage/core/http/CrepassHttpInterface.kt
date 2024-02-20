package com.crepass.crepassmessage.core.http

import com.crepass.crepassmessage.core.listeners.OnCrepassQueryCompleteListener
import java.util.*


interface CrepassHttpInterface {

    fun query(url: String, headers: HashMap<String, String>,
              rawRequest: String,listener: OnCrepassQueryCompleteListener
    )

}
