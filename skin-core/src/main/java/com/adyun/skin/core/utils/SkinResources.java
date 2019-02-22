package com.adyun.skin.core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by Zachary
 * on 2019/2/18.
 */
public class SkinResources {
    private static SkinResources instance;

    private Resources mSkinResources;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;

    private Resources mAppResources;

    public static void init(Context context){
        if (instance == null){
            synchronized (SkinResources.class){
                if (instance == null){
                    instance = new SkinResources(context);
                }
            }
        }
    }

    public static SkinResources getInstance() {
        return instance;
    }

    private SkinResources(Context context) {
       mAppResources = context.getResources();
    }

    /**
     * 重置 还原
     */
    public void reset(){
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }
    public void applySkin(Resources resources,String pkgName){
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        // 是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    public int getIdentifier(int resId){
        if (isDefaultSkin){
            return resId;
        }
        // 在皮肤包中不一定就是当前程序的Id
        // 获取id 在当前的名称
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName,resType,mSkinPkgName);
        return skinId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId){
        if (isDefaultSkin){
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0){
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(resId);
    }

    public Drawable getDrawable(int resId){
        if (isDefaultSkin){
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0){
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(resId);
    }

    public Object getBackground(int resId){
        String typeName = mAppResources.getResourceTypeName(resId);
        if (typeName.equals("color")){
            return getColor(resId);
        }else {
            return getDrawable(resId);
        }

    }

    public String getString(int resId){
        try{
            if (isDefaultSkin){
                return mAppResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0){
                return mAppResources.getString(resId);
            }
            return mSkinResources.getString(resId);
        }catch (Exception e){
            return null;
        }
    }

    public Typeface getTypeface(int resId){
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)){
            return Typeface.DEFAULT;
        }
        try{
            Typeface typeface;
            if (isDefaultSkin){
                typeface = Typeface.createFromAsset(mAppResources.getAssets(),skinTypefacePath);
            }else {
                typeface = Typeface.createFromAsset(mAppResources.getAssets(),skinTypefacePath);
            }
            return typeface;
        }catch (RuntimeException e){

        }
        return Typeface.DEFAULT;

    }



}
