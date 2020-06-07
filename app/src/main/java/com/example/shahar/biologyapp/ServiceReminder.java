package com.example.shahar.biologyapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class ServiceReminder extends IntentService {

    public ServiceReminder() {
        super("ServiceReminder");
    }

    /**
     * This method counts an hour in the background and sends the information to the broadcast
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        String message = intent.getStringExtra("count");
        intent.setAction(ActivityPlantList.FILTER_ACTION_KEY);
        SystemClock.sleep(3600000); //an hour in milliseconds
        int m= Integer.parseInt(message)+1;
        message =String.valueOf(m);
        String echoMessage = "Hours that past since the last edit: " + message;
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("broadcastMessage", echoMessage));
    }
}



