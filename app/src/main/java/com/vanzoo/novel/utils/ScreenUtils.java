package com.vanzoo.novel.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.reflect.Method;

public class ScreenUtils {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics;
    }


    public static int dpToPx(Context context, int dp) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int spToPx(int sp, Context con) {
        DisplayMetrics metrics = getDisplayMetrics(con);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static int getTextSizePx(int size, Context context) {
        if (size == 1) {
            return ScreenUtils.dpToPx(context, 14);
        } else if (size == 2) {
            return ScreenUtils.dpToPx(context, 16);
        } else if (size == 3) {
            return ScreenUtils.dpToPx(context, 18);
        } else if (size == 4) {
            return ScreenUtils.dpToPx(context, 20);
        } else if (size == 5) {
            return ScreenUtils.dpToPx(context, 22);
        } else if (size == 6) {
            return ScreenUtils.dpToPx(context, 24);
        } else if (size == 7) {
            return ScreenUtils.dpToPx(context, 26);
        } else if (size == 8) {
            return ScreenUtils.dpToPx(context, 28);
        } else if (size == 9) {
            return ScreenUtils.dpToPx(context, 30);
        } else if (size == 10) {
            return ScreenUtils.dpToPx(context, 32);
        }
        return ScreenUtils.dpToPx(context, 18);
    }


    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        // 一般是25dp
        int height = dp2px(context, 20);
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取虚拟按键的高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 是否存在虚拟按键
     *
     * @return
     */
    private static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 获取广告宽度
     *
     * @param context
     * @param margin
     * @return
     */
    public static final int getAdWidthDp(Context context, int margin) {
        try {
            int sw = context.getResources().getDisplayMetrics().widthPixels - margin;
            float density = getDeviceDensity(context);
            int adWidth = (int) (sw / density);
            return adWidth;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1080;
    }

    public static float getDeviceDensity(Context context) {
        /**
         * 简易模式下密度变化
         * density: 3.0   context.getResources().getDisplayMetrics().density;
         * density2: 3.375  AutoSizeConfig.getInstance().getInitDensity();
         */
        float density = 1.5f;
        if (context != null) {
            density = context.getResources().getDisplayMetrics().density;//手机像素密度
        }
        return density;
    }
}
