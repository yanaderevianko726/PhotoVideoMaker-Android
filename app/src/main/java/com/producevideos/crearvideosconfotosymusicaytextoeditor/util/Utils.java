package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Utils {
    public static boolean isLmpOrAbove() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isLmpMR1OrAbove() {
        return VERSION.SDK_INT >= 22;
    }

    public static boolean isLmpMR1() {
        return VERSION.SDK_INT == 22;
    }

    public static int getPixelForDp(Context context, int displayPixels) {
        return (int) TypedValue.applyDimension(1, (float) displayPixels, context.getResources().getDisplayMetrics());
    }

    public static Path getWavePath(float width, float height, float amplitude, float shift, float divide) {
        Path path = new Path();
        float quadrant = height - amplitude;
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.0f, quadrant);
        for (int i = 0; ((float) i) < 10.0f + width; i += 10) {
            path.lineTo((float) i, (((float) Math.sin((((((double) (i + 10)) * 3.141592653589793d) / 180.0d) / ((double) divide)) + ((double) shift))) * amplitude) + quadrant);
        }
        path.lineTo(width, 0.0f);
        path.close();
        return path;
    }

    public static void setFont(Activity mContext, TextView tv) {
        tv.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/ROBOTO-MEDIUM.TTF"));
    }

    public static void setFont(Activity mContext, int id) {
        View v = mContext.findViewById(id);
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/ROBOTO-MEDIUM.TTF");
        if (v instanceof TextView) {
            ((TextView) mContext.findViewById(id)).setTypeface(typeFace);
        } else if (v instanceof Button) {
            ((Button) mContext.findViewById(id)).setTypeface(typeFace);
        }
    }

    public static void setFont(Activity mContext, View v) {
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/ROBOTO-MEDIUM.TTF");
        if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeFace);
        } else if (v instanceof Button) {
            ((Button) v).setTypeface(typeFace);
        } else if (v instanceof EditText) {
            ((EditText) v).setTypeface(typeFace);
        }
    }
}
