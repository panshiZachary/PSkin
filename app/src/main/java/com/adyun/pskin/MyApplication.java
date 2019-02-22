package com.adyun.pskin;

import android.app.Application;

import com.adyun.skin.core.SkinManager;

/**
 * Created by Zachary
 * on 2019/2/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
