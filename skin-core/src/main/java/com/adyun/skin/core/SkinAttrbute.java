package com.adyun.skin.core;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adyun.skin.core.utils.SkinPreference;
import com.adyun.skin.core.utils.SkinResources;
import com.adyun.skin.core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zachary
 * on 2019/2/15.
 */
public class SkinAttrbute {

    private static final List<String> mAttributes = new ArrayList<>();

    /**
     * 将要换肤的属性
     */
    static {
        mAttributes.add("background");
        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");

        mAttributes.add("skinTypeface");
    }

    private Typeface typeface;

    List<SkinView> skinViews = new ArrayList<>();

    public SkinAttrbute(Typeface typeface) {
        this.typeface = typeface;

    }


    public void load(View view, AttributeSet attrs) {
        List<SkinPairs> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)){
                String attributeValue = attrs.getAttributeValue(i);
                // 写死了 不进行换肤
                if (attributeValue.startsWith("#")){
                    continue;
                }
                int resId;
                if (attributeValue.startsWith("?")){
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(),new int[]{attrId})[0];
                }else {
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                // 表示可以被换肤
                if (resId!=0){
                    skinPairs.add(new SkinPairs(attributeName, resId)) ;
                }
            }
        }
        if (!skinPairs.isEmpty()|| view instanceof TextView || view instanceof SkinViewSupport){
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin(typeface);
            skinViews.add(skinView);
        }


    }

    /**
     * 换皮肤
     * @param skinTypeface
     */
    public void applySkin(Typeface skinTypeface) {
        for (SkinView skinView : skinViews) {
            skinView.applySkin(skinTypeface);
        }

    }

    static class SkinView{
        View view;
        List<SkinPairs> skinPairs;

        public SkinView(View view, List<SkinPairs> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface) {
            applySkinTypeface(typeface);
            applySkinSupport();
            for (SkinPairs skinPair : skinPairs) {
                Drawable left = null, top =null,right = null,bottom=null;
                switch (skinPair.attrbuteName){
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        // color
                        if (background instanceof Integer){
                            view.setBackgroundColor((Integer) background);
                        }else {
                           // 采用兼容包
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer){
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        }else {
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView)view).setTextColor(SkinResources.getInstance()
                                .getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "skinTypeface":
                        Typeface typeface1 = SkinResources.getInstance().getTypeface(skinPair.resId);
                        applySkinTypeface(typeface1);
                        break;
                    default:
                        break;
                }
                if (left !=null || right!=null || top!=null || bottom!=null){
                    ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(left,top,right,bottom);
                }
            }
        }

        private void applySkinSupport() {
            if (view instanceof SkinViewSupport){
                ((SkinViewSupport)view).applySkin();
            }

        }

        private void applySkinTypeface(Typeface typeface) {
            if (view instanceof TextView){
                ((TextView)view).setTypeface(typeface);
            }
        }
    }

    static class SkinPairs{
        String attrbuteName;
        int resId;

        public SkinPairs(String attrbuteName, int resId) {
            this.attrbuteName = attrbuteName;
            this.resId = resId;
        }
    }
}
