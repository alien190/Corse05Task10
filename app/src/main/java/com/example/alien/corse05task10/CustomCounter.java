package com.example.alien.corse05task10;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomCounter extends View {

    private static final String SEPARATOR = " / ";
    private static final long ANIMATION_DURATION = 500;

    private int mMaxValue;
    private int mValue;
    private int mOldValue;
    private String mOldValueString;
    private int mWidthSpecSize;
    private int mHeightSpecSize;
    private float mWidth;
    private float mHeight;
    private Paint mValuePaint;
    private Paint mMaxValuePaint;
    private Rect mMaxValueBounds;
    private Rect mValueBounds;
    private Rect mMaxValueBoundsWithSep;
    private String mMaxValueString;
    private String mValueString;
    private ValueAnimator mValueAnimator;
    private float mAnimatedPosition;

    public CustomCounter(Context context) {
        super(context);
        init(context, null);
    }

    public CustomCounter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCounter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray mainTypedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomCounter, 0, R.style.DefaultCustomIndicator);
        setMaxValue(mainTypedArray.getInteger(R.styleable.CustomCounter_maxValue, 0));
        setValue(mainTypedArray.getInteger(R.styleable.CustomCounter_value, 0));

        mValuePaint = new Paint();
        mValuePaint.setTextAlign(Paint.Align.RIGHT);
        setValueTextColor(mainTypedArray.getColor(R.styleable.CustomCounter_valueTextColor,
                Color.BLACK));
        mMaxValuePaint = new Paint();
        mMaxValuePaint.setTextAlign(Paint.Align.LEFT);
        setMaxValueTextColor(mainTypedArray.getColor(R.styleable.CustomCounter_maxValueTextColor,
                Color.BLACK));

        setTextStyle(mainTypedArray.getInteger(R.styleable.CustomCounter_textStyle, 0));
        setTextSize(mainTypedArray.getDimension(R.styleable.CustomCounter_textSize, 18));

        mainTypedArray.recycle();

        mMaxValueBounds = new Rect();
        mValueBounds = new Rect();
        mMaxValueBoundsWithSep = new Rect();

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(ANIMATION_DURATION);
        mValueAnimator.addUpdateListener(this::onAnimationUpdate);

    }

    private void onAnimationUpdate(ValueAnimator valueAnimator) {
        mAnimatedPosition = (float) valueAnimator.getAnimatedValue();
        invalidate();
    }

    private void setTextSize(float dimension) {
        mValuePaint.setTextSize(dimension);
        mMaxValuePaint.setTextSize(dimension);
        invalidate();
    }

    private void setTextStyle(int style) {
        Typeface typeface = Typeface.create(Typeface.DEFAULT, style);
        mValuePaint.setTypeface(typeface);
        mMaxValuePaint.setTypeface(typeface);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        mMaxValuePaint.getTextBounds(mMaxValueString, 0, mMaxValueString.length(), mMaxValueBounds);
        mMaxValuePaint.getTextBounds(SEPARATOR + mMaxValueString, 0, mMaxValueString.length() + 3, mMaxValueBoundsWithSep);
        mValuePaint.getTextBounds(mMaxValueString, 0, mMaxValueString.length(), mValueBounds);

        mWidth = (mMaxValueBoundsWithSep.width() + mValueBounds.width()) * 1.1f;
        mHeight = Math.max(mValueBounds.height(), mMaxValueBounds.height()) * 1.1f;
        mAnimatedPosition = mValueBounds.height();
        mValueAnimator.setFloatValues(0f, mValueBounds.height());

        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mOldValue < mValue) {
            canvas.drawText(mValueString, mValueBounds.width(), mAnimatedPosition, mValuePaint);
            canvas.drawText(mOldValueString, mValueBounds.width(), mAnimatedPosition + mValueBounds.height() * 1.1f, mValuePaint);
        } else {
            canvas.drawText(mOldValueString, mValueBounds.width(), mAnimatedPosition - mValueBounds.height() * 0.1f, mValuePaint);
            canvas.drawText(mValueString, mValueBounds.width(), mAnimatedPosition + mValueBounds.height(), mValuePaint);
        }
        canvas.drawText(SEPARATOR + mMaxValueString, mValueBounds.width(), mMaxValueBounds.height(), mMaxValuePaint);
    }

    public void setValue(int value) {
        if (value >= 0 && value <= mMaxValue) {
            mValue = value;
        } else if (value < 0) {
            mValue = 0;
        } else {
            mValue = mMaxValue;
        }
        mValueString = String.valueOf(mValue);
        mOldValueString = mValueString;
        invalidate();
    }

    public void setMaxValue(int value) {
        mMaxValue = value >= 0 ? value : 0;
        mMaxValueString = String.valueOf(mMaxValue);
    }

    public void setValueTextColor(int color) {
        mValuePaint.setColor(color);
        invalidate();
    }

    public void setMaxValueTextColor(int color) {
        mMaxValuePaint.setColor(color);
        invalidate();
    }

    public void increase() {
        mOldValue = mValue;
        mOldValueString = String.valueOf(mOldValue);
        mValue += 1;
        mValueString = String.valueOf(mValue);
        mValueAnimator.start();
    }

    public void decrease() {
        mOldValue = mValue;
        mOldValueString = String.valueOf(mOldValue);
        mValue -= 1;
        mValueString = String.valueOf(mValue);
        mValueAnimator.reverse();
    }
}
