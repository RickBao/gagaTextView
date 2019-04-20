package com.nick.gagatext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

@SuppressLint("AppCompatCustomView")
public class gagaText extends TextView {

    private int mDuration = 200;

    private boolean mIsStart;

    private CharSequence mText;

    private int mPosition;

    private int mSelectedColor = 0xffff00ff;

    private OnDynamicListener mOnDynamicListener;

    private DynamicStyle mDynamicStyle = DynamicStyle.NORMAL;

    public enum DynamicStyle {
        NORMAL(0),
        TYPEWRITING(1),
        CHANGE_COLOR(2);

        private int mValue;
        DynamicStyle(int value) {
            this.mValue = value;
        }

        private static DynamicStyle getFromInt(int value) {
            for (DynamicStyle style : DynamicStyle.values()) {
                if (style.mValue == value) {
                    return style;
                }
            }

            return DynamicStyle.NORMAL;
        }
    }


    public gagaText(Context context) {
        super(context);
    }

    public gagaText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public gagaText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.gagaTextView);
        mText = array.getText(R.styleable.gagaTextView_dynamicText);
        mDuration = array.getInt(R.styleable.gagaTextView_duration, mDuration);
        mSelectedColor = array.getColor(R.styleable.gagaTextView_selectedColor, mSelectedColor);
        mDynamicStyle = DynamicStyle.getFromInt(array.getInt(R.styleable.gagaTextView_dynamicStyle, 0));
        array.recycle();
    }

    public void start() {
        if (mIsStart) {
            return;
        }

        if (TextUtils.isEmpty(mText)) {
            mText = getText();
        }

        mPosition = 0;

        if (!TextUtils.isEmpty(mText)) {
            mIsStart = true;
            post(mRunnable);
        } else {
            mIsStart = false;
            if (mOnDynamicListener != null) {
                mOnDynamicListener.onCompile();
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (DynamicStyle.TYPEWRITING == mDynamicStyle) {
                setText(mText.subSequence(0, mPosition));
            } else if (DynamicStyle.CHANGE_COLOR == mDynamicStyle) {
                setChangeColorText(mPosition);
            } else {
                setText(mText);
                mIsStart = false;
                if (mOnDynamicListener != null) {
                    mOnDynamicListener.onCompile();
                }
                return;
            }

            if (mPosition < mText.length()) {
                if (mOnDynamicListener != null) {
                    mOnDynamicListener.onChange(mPosition);
                }

                mPosition++;

                postDelayed(mRunnable, mDuration);
            } else {
                if (mOnDynamicListener != null) {
                    mOnDynamicListener.onChange(mPosition);
                }

                mIsStart = false;

                if (mOnDynamicListener != null) {
                    mOnDynamicListener.onCompile();
                }
            }
        }
    };

    private void setChangeColorText(int position) {
        SpannableString spannableString = new SpannableString(mText);
        spannableString.setSpan(new ForegroundColorSpan(mSelectedColor), 0, mPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        setText(spannableString);
    }

    public void setDynamicText(@StringRes int resId) {
        this.mText = getResources().getText(resId);
    }

    public void setDynamicText(CharSequence text) {
        this.mText = text;
    }

    public CharSequence getDynamicText() {
        return mText;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }

    public DynamicStyle getDynamicStyle() {
        return mDynamicStyle;
    }

    public void setDynamicStyle(DynamicStyle dynamicStyle) {
        this.mDynamicStyle = dynamicStyle;
    }

    public void setOnDynamicListener(OnDynamicListener listener) {
        this.mOnDynamicListener = listener;
    }

    public void setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
    }

    public void setSelectedColorResource(@ColorRes int resId) {
        this.mSelectedColor = ContextCompat.getColor(getContext(), resId);
    }
    public interface OnDynamicListener {
        void onChange(int position);
        void onCompile();
    }

}
