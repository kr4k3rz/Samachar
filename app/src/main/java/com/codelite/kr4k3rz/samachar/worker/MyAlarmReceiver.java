package com.codelite.kr4k3rz.samachar.worker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "In Broadcast receiver");
        Intent intent1 = new Intent(context, MyIntentService.class);
        context.startService(intent1);

    }
}
