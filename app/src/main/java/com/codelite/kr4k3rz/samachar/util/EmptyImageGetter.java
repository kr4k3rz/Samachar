package com.codelite.kr4k3rz.samachar.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

/**
 * Created by kr4k3rz on 8/28/16.
 */

public class EmptyImageGetter implements Html.ImageGetter {
    private static final Drawable TRANSPARENT_DRAWABLE = new ColorDrawable(Color.TRANSPARENT);

    @Override
    public Drawable getDrawable(String source) {
        return TRANSPARENT_DRAWABLE;
    }
}
