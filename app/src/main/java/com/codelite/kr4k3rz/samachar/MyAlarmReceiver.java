package com.codelite.kr4k3rz.samachar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyIntentService.class);
        context.startService(intent1);
        Log.i("MyAlarmReceiver", "Alarm in Broadcast fired");

    }
}
