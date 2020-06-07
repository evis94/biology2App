package com.example.shahar.biologyapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class ServiceTimer extends IntentService {

    public ServiceTimer() {
        super("ServiceReminder");
    }

    /**
     * This method counts an hour in the background and sends the information to the broadcast
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        int m = intent.getExtras().getInt("count");
        String echoMessage=  "The hours that past since the last edit: " +m;
        //flag=intent.getExtras().getBoolean("flag");
        intent.setAction(ActivityPlantList.FILTER_ACTION_KEY);
        SystemClock.sleep(3600000); //an hour in milliseconds

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("broadcastMessage", echoMessage));
        Log.d("--------","finished running service reminder code");
        Log.d("-----m in outside class",String.valueOf(m));
    }

}
