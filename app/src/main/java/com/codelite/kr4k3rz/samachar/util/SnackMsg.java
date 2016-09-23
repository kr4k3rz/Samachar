package com.codelite.kr4k3rz.samachar.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.codelite.kr4k3rz.samachar.R;

/**
 * Created by kr4k3rz on 8/25/16.
 */

public class SnackMsg {


    static public void showMsgShort(View rootView, String msg) {
        Snackbar snackbar = Snackbar.make(rootView, "" + msg,
                Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(rootView.getResources().getColor(R.color.colorAccent));
        snackbar.show();

    }

    static public void showMsgLong(View rootView, String msg) {
        Snackbar snackbar = Snackbar.make(rootView, "" + msg,
                Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(rootView.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
