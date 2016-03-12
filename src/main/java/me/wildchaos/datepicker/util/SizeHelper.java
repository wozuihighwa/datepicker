package me.wildchaos.datepicker.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by 孙俊伟 on 2016/2/24.
 */
public class SizeHelper {
    /**
     * 根据 dpi ，返回合适的尺寸，初始像素以 xxhdpi 为标准
     *
     * @param originPixel
     * @return
     */
    public static int getSuitablePixel(int originPixel, Activity activity) {
        int densityDpi = getDensityDpi(activity);
        int suitablePixel = originPixel;
        if (densityDpi > 240 && densityDpi <= 320) {            // xhdpi
            suitablePixel = originPixel * 2 / 3;
        } else if (densityDpi > 320 && densityDpi <= 480) {     // xxhdpi
            suitablePixel = originPixel;
        } else if (densityDpi > 480) {                          // xxxhdpi
            suitablePixel = originPixel * 4 / 3;
        }

        return suitablePixel;
    }

    private static int getDensityDpi(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dpi = metrics.densityDpi;

        return dpi;
    }
}
