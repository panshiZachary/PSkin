package com.adyun.skin.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import com.adyun.skin.core.R;

/**
 * Created by Zachary
 * on 2019/2/15.
 */
public class SkinThemeUtils {

    private static int[] TYPEFACE_ATTR = {
            R.attr.skinTypeface
    };

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr
            .navigationBarColor};

    public static int[] getResId(Context context,int[] attr){
        int[] resId = new int[attr.length];

        TypedArray typedArray = context.obtainStyledAttributes(attr);
        for (int i = 0; i < typedArray.length(); i++) {
            resId[i] = typedArray.getResourceId(i,0);
        }
        return resId;

    }

    public static void updateStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int[] resIds = getResId(activity, STATUSBAR_COLOR_ATTRS);
        /**
         * 修改状态栏颜色
         */
        // 如果没有配置属性 为0
        if (resIds[0] == 0){
            int statusBarColorId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if (statusBarColorId!=0){
                activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarColorId));
            }
        }else {
            activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(resIds[0]));
        }
        /**
         * 修改底部虚拟按键的颜色
         *
         */
        if (resIds[1]!=0){
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor(resIds[1]));
        }



    }

    /**
     * 获得字体
     * @param activity
     * @return
     */
    public static Typeface getSkinTypeface(Activity activity) {
        int[] resIds = getResId(activity, TYPEFACE_ATTR);
        Typeface typeface = SkinResources.getInstance().getTypeface(resIds[0]);
        return typeface;
    }
}
