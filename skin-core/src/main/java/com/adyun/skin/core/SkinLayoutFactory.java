package com.adyun.skin.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.adyun.skin.core.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Zachary
 * on 2019/2/15.
 */
public class SkinLayoutFactory implements LayoutInflater.Factory2,Observer {
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};


    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();
    private  Activity activity;
    SkinAttrbute skinAttrbute;

    public SkinLayoutFactory(Activity activity, Typeface typeface) {
        this.activity = activity;
        skinAttrbute = new SkinAttrbute(typeface);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 反射 classloader
        View view = createViewFromTag(name, context, attrs);
        if (view == null){
            view = createView(name,context,attrs);
        }
        // 筛选属于 属性的View
        skinAttrbute.load(view,attrs);

        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor==null){
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                sConstructorMap.put(name,constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (constructor!=null){
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        // 包含了 . 说明是自定义控件
        if (name.indexOf(".")!=-1){
            return null;
        }
        View view =null;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view = createView(mClassPrefixList[i] + name, context, attrs);
            if (view!=null){
                break;
            }
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBar(activity);

        Typeface skinTypeface = SkinThemeUtils.getSkinTypeface(activity);

        // 换肤
        skinAttrbute.applySkin(skinTypeface);

    }
}
