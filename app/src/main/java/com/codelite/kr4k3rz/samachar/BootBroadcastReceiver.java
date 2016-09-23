package com.codelite.kr4k3rz.samachar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class BootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
                intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
            // Launch the specified service when this message is received
            Intent startServiceIntent = new Intent(context, MyIntentService.class);
            startWakefulService(context, startServiceIntent);
        }
    }
}
