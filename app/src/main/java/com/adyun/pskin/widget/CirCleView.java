package com.adyun.pskin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.adyun.pskin.R;
import com.adyun.skin.core.SkinViewSupport;
import com.adyun.skin.core.utils.SkinResources;

/**
 * Created by Zachary
 * on 2019/2/11.
 */
public class CirCleView extends View implements SkinViewSupport {
    private AttributeSet attrs;
    // 画笔
    private Paint mTextPaint;
    // 半径
    private int radius;

    private int circleColorResId;


    public CirCleView(Context context) {
        this(context,null,0);
    }

    public CirCleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CirCleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirCleView);
        int color = typedArray.getColor(R.styleable.CirCleView_circleColor, 0);
        circleColorResId = typedArray.getResourceId(R.styleable.CirCleView_circleColor,0);
        typedArray.recycle();

        mTextPaint = new Paint();

        mTextPaint.setColor(color);
        // 开启抗锯齿 平滑文字和圆弧的边缘
        mTextPaint.setAntiAlias(true);

        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取宽度的一半
        int width = getWidth() / 2;
        // 获取高度一半
        int height = getHeight() / 2;
        // 设置圆的半径为宽高的最小值
        radius = Math.min(width,height);
        // 利用canvas 画一个圆
        canvas.drawCircle(width,height,radius,mTextPaint);
    }
    public void setCircleColor(@ColorInt int color){
        mTextPaint.setColor(color);
        invalidate();

    }

    @Override
    public void applySkin() {
        if (circleColorResId!=0){
            int color = SkinResources.getInstance().getColor(circleColorResId);
            setCircleColor(color);
        }
    }
}
