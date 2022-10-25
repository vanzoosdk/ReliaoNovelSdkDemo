package com.vanzoo.novel.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.vanzoo.novelsdk.R;


public class StatusBarUtil {

    public static final int DEFAULT_MASK_ALPHA = 112;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;
    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, DEFAULT_MASK_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity  需要设置的activity
     * @param color     状态栏颜色值
     * @param maskAlpha 状态栏遮罩透明度
     */

    public static void setColor(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int maskAlpha) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, maskAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(calculateStatusColor(color, maskAlpha));
            } else {
                fakeStatusBarView = createStatusBarView(activity, color, maskAlpha);
                if (fakeStatusBarView != null) {
                    decorView.addView(fakeStatusBarView);
                }
            }
            setRootView(activity);
        }
    }

    /**
     * 设置状态栏纯色 不加半透明遮罩效果
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColorNoTranslucent(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, DEFAULT_MASK_ALPHA);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     */
    public static void setTranslucent(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || activity == null) {
            return;
        }
        setTransparent(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || activity == null) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, DEFAULT_MASK_ALPHA, needOffsetView);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha,
                                                  View needOffsetView) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addTranslucentView(activity, statusBarAlpha);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
        }
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, DEFAULT_MASK_ALPHA, needOffsetView);
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    public static void setTranslucentForImageViewInFragment(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha,
                                                            View needOffsetView) {
        if (activity == null) {
            return;
        }
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity);
        }
    }

    /**
     * 隐藏伪状态栏 View
     *
     * @param activity 调用的 Activity
     */
    public static void hideFakeStatusBarView(Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
        View fakeTranslucentView = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            fakeTranslucentView.setVisibility(View.GONE);
        }
    }

    // 缓存状态
    static boolean isActivityLightMode = false;

    /**
     * Set the status bar's light mode.
     *
     * @param activity    The activity.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    public static void setStatusBarLightMode(@NonNull final Activity activity,
                                             final boolean isLightMode) {
        // 缓存状态，避免主界面频繁调用statusbar设置
        boolean isMainActivity = false;
        try {

            // 缓存会导致概率出现变量和实际结果不一致的情况，因此暂时屏蔽该逻辑
//            Log.e("Statusbar","setStatusBarLightMode: "+ isLightMode +" activity: "+ isActivityLightMode);
//            isMainActivity = activity != null && activity.getClass().getSimpleName().equalsIgnoreCase("SmartWeatherActivity");
//            if (isMainActivity && isLightMode == isActivityLightMode) {
//                return;
//            }
//            if (isMainActivity) {
//                isActivityLightMode = isLightMode;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStatusBarLightMode(activity.getWindow(), isLightMode);
    }

    /**
     * Set the status bar's light mode.
     *
     * @param window      The window.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    public static void setStatusBarLightMode(@NonNull final Window window,
                                             final boolean isLightMode) {
//        Log.e("Statusbar","setStatusBarLightMode: "+ isLightMode );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (isLightMode) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            View view = createTranslucentStatusBarView(activity, statusBarAlpha);
            if (view != null) {
                contentView.addView(view);
            }
        }
    }

    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color) {
        return createStatusBarView(activity, color, 0);
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        if (activity == null) {
            return null;
        }
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 设置透明
     */
    public static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null || context.getResources() == null) {
            return 0;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static final String DEFAULT_NAVIGATION_COLOR = "#FF212121";

    public static final String DEFAULT_STATUS_COLOR = "#FF212121";

    public static final int COLOR_DARK = 33;

    /**
     * 设置虚拟按键底部背景色，如果虚拟按键背景色偏灰白色，设置虚拟按键颜色为table底色
     */
    public static void setNavigationColor(Activity activity) {
        try {
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = window.getNavigationBarColor();
                int alpha = (color & 0xff000000) >>> 24;
                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = (color & 0x000000ff);

                if (red > COLOR_DARK || red > COLOR_DARK || red > COLOR_DARK) {
                    //虚拟按键背景色偏灰白色，此时设置虚拟按键颜色为table底色
                    window.setNavigationBarColor(Color.parseColor(DEFAULT_NAVIGATION_COLOR));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    /**
     * 进入界面时，先设置界面为沉浸式显示
     *
     * @param context
     */
    private static void setFullScreen(Activity context) {
        try {
            // 使用最新方法，优先使用代码逻辑处理隐藏和显示
            hideSystemUi(context);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * 为了兼容乐视头条sdk开屏样式，乐视上动态隐藏显示状态栏/市场版暂时不变。
     */
    public static void setStatusBarTranslucent(Activity activity) {

        try {
            // 使用最新方法，优先使用代码逻辑处理隐藏和显示
            showSystemUi(activity);
            try {
                StatusBarUtil.setTransparentForImageView(activity, null);
                // 必须要设置，否则容易出现界面底部留白
                if (Build.VERSION.SDK_INT >= 20) {
                    activity.getWindow().getDecorView().requestApplyInsets();
                } else {
                    activity.getWindow().getDecorView().requestFitSystemWindows();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    StatusBarUtil.setColorNoTranslucent(activity, Color.parseColor("#F1F1F6"));
                    StatusBarUtil.setStatusBarLightMode(activity, true);
                } else {
                    StatusBarUtil.setColor(activity, Color.parseColor("#F1F1F6"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示状态栏，大于P必须设置window属性，否则显示会出现问题或者压根无法生效
     */
    private static void showSystemUi(Activity activity) {
        try {
            // 需要设置该属性才能设置状态栏颜色
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
                activity.getWindow().setAttributes(lp);
            }
            hasStatusHide = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 状态栏是否全屏显示被隐藏
    static boolean hasStatusHide = false;

    /**
     * 隐藏状态栏，大于P必须设置window属性，否则显示会出现问题或者压根无法生效
     */
    public static void hideSystemUi(Activity activity) {
        try {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
            }
            hasStatusHide = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWhiteStatusBar(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorNoTranslucent(context, Color.WHITE);
            StatusBarUtil.setStatusBarLightMode(context, true);
        } else {
            StatusBarUtil.setColor(context, Color.WHITE);
        }
    }

    //Color.parseColor("#ffffff")
    public static void setStatusBarColor(Activity context,int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorNoTranslucent(context, color);
            StatusBarUtil.setStatusBarLightMode(context, true);
        } else {
            StatusBarUtil.setColor(context, color);
        }
    }

}