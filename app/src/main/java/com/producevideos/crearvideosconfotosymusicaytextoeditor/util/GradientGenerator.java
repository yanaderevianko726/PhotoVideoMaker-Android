package com.producevideos.crearvideosconfotosymusicaytextoeditor.util;

import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class GradientGenerator {
    public static Shader getRadialGradient(float width, float height, int startColor, int endColor) {
        return new RadialGradient(width / 2.0f, height / 2.0f, Math.max(width, height) / 2.0f, startColor, endColor, TileMode.CLAMP);
    }

    public static Shader getLinearGradient(int angle, float width, float height, int startColor, int endColor) {
        float x1 = 0.0f;
        float y1 = 0.0f;
        float y2 = 0.0f;
        float x2 = (float) (((double) height) / Math.tan((double) angle));
        if (angle <= 180) {
            y1 = height;
            if (angle > 90) {
                x1 = width;
            }
        } else if (angle <= 360) {
            y2 = height;
            if (angle <= 270) {
                x1 = width;
            }
        }
        return new LinearGradient(x1, y1, x2, y2, startColor, endColor, TileMode.CLAMP);
    }
}
