package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;

import java.lang.reflect.Method;

public class ActivityAnimUtil {
    private static Method sClipRevealMethod;

    static {
        sClipRevealMethod = null;
        try {
            sClipRevealMethod = ActivityOptions.class.getDeclaredMethod("makeClipRevealAnimation", new Class[]{View.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
        } catch (Exception e) {
        }
    }

    public static boolean startActivitySafely(View v, Intent intent) {
        boolean success = false;
        try {
            success = startActivity(v, intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(v.getContext(), R.string.applicatoin_not_found, Toast.LENGTH_SHORT).show();
        }
        return success;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"WrongConstant"})
    @android.annotation.TargetApi(23)
    private static boolean startActivity(View r14, Intent r15) {
        /*
        r9 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r15.addFlags(r9);
        r1 = r14.getContext();
        r7 = 0;
        if (r14 == 0) goto L_0x0078;
    L_0x000c:
        r6 = 0;
        r9 = sClipRevealMethod;	 Catch:{ SecurityException -> 0x0082 }
        if (r9 == 0) goto L_0x0048;
    L_0x0011:
        r8 = r14.getMeasuredWidth();	 Catch:{ SecurityException -> 0x0082 }
        r5 = r14.getMeasuredHeight();	 Catch:{ SecurityException -> 0x0082 }
        r9 = sClipRevealMethod;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r10 = 0;
        r11 = 5;
        r11 = new java.lang.Object[r11];	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r12 = 0;
        r11[r12] = r14;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r12 = 1;
        r13 = 0;
        r13 = java.lang.Integer.valueOf(r13);	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r11[r12] = r13;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r12 = 2;
        r13 = 0;
        r13 = java.lang.Integer.valueOf(r13);	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r11[r12] = r13;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r12 = 3;
        r13 = java.lang.Integer.valueOf(r8);	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r11[r12] = r13;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r12 = 4;
        r13 = java.lang.Integer.valueOf(r5);	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r11[r12] = r13;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r9 = r9.invoke(r10, r11);	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r0 = r9;
        r0 = (android.app.ActivityOptions) r0;	 Catch:{ IllegalAccessException -> 0x007d, InvocationTargetException -> 0x0085 }
        r6 = r0;
    L_0x0048:
        if (r6 != 0) goto L_0x0064;
    L_0x004a:
        r9 = com.androworld.photovideomaker.util.Utils.isLmpOrAbove();	 Catch:{ SecurityException -> 0x0082 }
        if (r9 != 0) goto L_0x0064;
    L_0x0050:
        r9 = 0;
        r10 = 0;
        r11 = r14.getMeasuredWidth();	 Catch:{ SecurityException -> 0x0082 }
        r12 = r14.getMeasuredHeight();	 Catch:{ SecurityException -> 0x0082 }
        r6 = android.app.ActivityOptions.makeScaleUpAnimation(r14, r9, r10, r11, r12);	 Catch:{ SecurityException -> 0x0082 }
        if (r6 != 0) goto L_0x008a;
    L_0x0060:
        r7 = r6.toBundle();	 Catch:{ SecurityException -> 0x0082 }
    L_0x0064:
        if (r6 != 0) goto L_0x0076;
    L_0x0066:
        r9 = com.androworld.photovideomaker.util.Utils.isLmpMR1();	 Catch:{ SecurityException -> 0x0082 }
        if (r9 == 0) goto L_0x0076;
    L_0x006c:
        r9 = 2130771988; // 0x7f010014 float:1.7147082E38 double:1.0527412384E-314;
        r10 = 2130771987; // 0x7f010013 float:1.714708E38 double:1.052741238E-314;
        r6 = android.app.ActivityOptions.makeCustomAnimation(r1, r9, r10);	 Catch:{ SecurityException -> 0x0082 }
    L_0x0076:
        if (r6 != 0) goto L_0x0078;
    L_0x0078:
        r1.startActivity(r15, r7);
        r9 = 1;
    L_0x007c:
        return r9;
    L_0x007d:
        r2 = move-exception;
        r9 = 0;
        sClipRevealMethod = r9;	 Catch:{ SecurityException -> 0x0082 }
        goto L_0x0048;
    L_0x0082:
        r4 = move-exception;
        r9 = 0;
        goto L_0x007c;
    L_0x0085:
        r3 = move-exception;
        r9 = 0;
        sClipRevealMethod = r9;	 Catch:{ SecurityException -> 0x0082 }
        goto L_0x0048;
    L_0x008a:
        r7 = 0;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.androworld.photovideomaker.util.ActivityAnimUtil.startActivity(android.view.View, android.content.Intent):boolean");
    }
}
