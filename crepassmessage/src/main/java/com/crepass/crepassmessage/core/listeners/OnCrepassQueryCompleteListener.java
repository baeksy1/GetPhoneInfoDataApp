package com.crepass.crepassmessage.core.listeners;


public interface OnCrepassQueryCompleteListener {
    void onComplete(String rawResponse);

    void onError(int statusCode, String rawResponse);

    void onFailure(Throwable throwable);
}
