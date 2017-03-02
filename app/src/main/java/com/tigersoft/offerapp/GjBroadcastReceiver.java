package com.tigersoft.offerapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by Deyter on 02.03.2017.
 */

public class GjBroadcastReceiver extends BroadcastReceiver {

    PendingIntent ni;

    public GjBroadcastReceiver() {
        //ni = PendingIntent.getActivity(MainActivity.getContext(), 0, new Intent(),0);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intentApp = new Intent(context,GpsJsonService.class);
            context.startService(intent);
            Log.i("Autostart", "started");
        }
    }
}