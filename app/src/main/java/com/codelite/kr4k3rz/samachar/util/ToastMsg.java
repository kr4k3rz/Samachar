package com.codelite.kr4k3rz.samachar.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kr4k3rz on 9/4/16.
 */

class ToastMsg {
   public static void shortMsg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

   public static void longMsg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
    }
}

