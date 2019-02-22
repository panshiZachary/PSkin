package com.adyun.skin.core;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import com.adyun.skin.core.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zachary
 * on 2019/2/15.
 */
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    Map<Activity,SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /**
         * 更新状态栏
         */
        SkinThemeUtils.updateStatusBar(activity);
        /**
         * 字体
         */
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);

        // 每次将mFactorySet 设置成false
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置 facroty2
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity,typeface);
        // 这里使用兼容包
        LayoutInflaterCompat.setFactory2(layoutInflater,skinLayoutFactory);

        // 注册观察者
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mLayoutFactoryMap.put(activity,skinLayoutFactory);





    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // 删除观察者
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);

    }

    public void updateSkin(Activity activity){
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.get(activity);
        skinLayoutFactory.update(null,null);

    }
}
