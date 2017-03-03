package com.tigersoft.offerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Deyter on 02.03.2017.
 */

public class GjBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intentApp = new Intent(context,GpsJsonService.class);
            context.startService(intentApp);
            Log.i("Autostart", "started");
        }
    }
}