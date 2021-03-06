/*
 * Copyright 2016. Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sarangnamu.common.ui.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.sarangnamu.common.frgmt.FrgmtManager;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2016. 3. 18.. <p/>
 */
public class BkTab extends RadioGroup implements RadioGroup.OnCheckedChangeListener {
    private static final org.slf4j.Logger mLog = org.slf4j.LoggerFactory.getLogger(BkTab.class);

    private int mPadding;
    private int mTargetViewId;
    private FrgmtManager mFrgmtManager;

    public BkTab(Context context) {
        super(context);
        initLayout();
    }
    
    public BkTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    protected void initLayout() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(0xFFFFFFFF);
//        setScrollContainer(true);
//        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public int dpToPixel(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }

    public void setButtonPadding(int dp) {
        mPadding = dpToPixel(dp);
    }

    public void setData(ArrayList<BkData> dataList) {
        setOnCheckedChangeListener(this);

        int i = 0;
        for (BkData data : dataList) {
            View view;

            if (data.clazz == null) {
                ImageButton imgbtn = new ImageButton(getContext());

                if (data instanceof BkImageData) {
                    imgbtn.setImageResource(data.getResId());
                }

                imgbtn.setOnClickListener(data.click);
                view = imgbtn;
            } else {
                BkRadioButton bkbtn = new BkRadioButton(getContext());

                if (data instanceof BkImageData) {
                    bkbtn.setButtonDrawable(data.getResId());
                    bkbtn.setText(null);
                } else {
                    BkTextData txtData = (BkTextData) data;
                    bkbtn.setTextResId(data.getResId(), txtData.getTextColorId());
//                    bkbtn.setButtonDrawable();
                }

                if (data.clazz == null) {
                    bkbtn.setTag(data.click);
                } else {
                    bkbtn.setTag(data.clazz);
                }

                bkbtn.setGravity(Gravity.CENTER_VERTICAL);

                view = bkbtn;
            }

            view.setId(i++);
            view.setPadding(0, mPadding, 0, mPadding);
            view.setLayoutParams(getRadioButtonLayoutParams());

            if (data.getBackgroundColor() != 0) {
                view.setBackgroundColor(data.getBackgroundColor());
            }

            view.setDuplicateParentStateEnabled(true);

            addView(view);
        }
    }

    private RadioGroup.LayoutParams getRadioButtonLayoutParams() {
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.weight  = 1;
        lp.gravity = Gravity.CENTER;

        return lp;
    }

    public void setChecked(int check) {
        if (check < 0 && getChildCount() < check) {
            return ;
        }

        BkRadioButton btn = (BkRadioButton) getChildAt(check);
        if (btn == null) {
            return ;
        }

        btn.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mTargetViewId == 0) {
            return ;
        }

        BkRadioButton btn = (BkRadioButton) group.getChildAt(checkedId);
        if (btn == null) {
            mLog.error("btn == null");
            return ;
        }

        if (mFrgmtManager != null) {
            mFrgmtManager.add(mTargetViewId, (Class) btn.getTag());
        }
    }

    public void setFrgmtManager(@IdRes int resid, FrgmtManager manager) {
        mTargetViewId = resid;
        mFrgmtManager = manager;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DATA TYPE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class BkImageData extends BkData {
        public BkImageData(@DrawableRes int drawid, Class<?> clazz) {
            this.resid = drawid;
            this.clazz = clazz;
        }

        public BkImageData(@DrawableRes int drawid, @ColorRes int bgcolor, Class<?> clazz) {
            this.resid = drawid;
            this.clazz = clazz;
            this.bgcolor = bgcolor;
        }

        public BkImageData(@DrawableRes int drawid, View.OnClickListener click) {
            this.resid = drawid;
            this.clazz = null;
            this.click = click;
        }

        public BkImageData(@DrawableRes int drawid, @ColorRes int bgcolor, View.OnClickListener click) {
            this.resid = drawid;
            this.clazz = null;
            this.click = click;
            this.bgcolor = bgcolor;
        }
    }

    public static class BkTextData extends BkData {
        int textColorId;

        public BkTextData(@StringRes int strid, Class<?> clazz) {
            this.resid = strid;
            this.clazz = clazz;
        }

        public BkTextData(@StringRes int strid, View.OnClickListener click) {
            this.resid = strid;
            this.clazz = null;
            this.click = click;
        }

        public void setTextColorId(@DrawableRes int resid) {
            textColorId = resid;
        }

        public @DrawableRes int getTextColorId() {
            return textColorId;
        }

        public void setBackgroundColorId(@DrawableRes int resid) {
            this.bgcolor = resid;
        }

        public @DrawableRes int getBackgroundColorId() {
            return this.bgcolor;
        }
    }

    public static class BkData {
        protected int resid;
        protected Class<?> clazz;
        protected View.OnClickListener click;
        protected int bgcolor = 0;

        public int getResId() {
            return resid;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public View.OnClickListener getClickListener() {
            return click;
        }

        public void setBackgroundColor(@ColorRes int bgcolor) {
            this.bgcolor = bgcolor;
        }

        public int getBackgroundColor() {
            return bgcolor;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // BkRadioButton
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class BkRadioButton extends RadioButton {
        private Drawable mDrawable;
        private TextPaint mTextPaint;

        public BkRadioButton(Context context) {
            super(context);
            initLayout();
        }
        
        public BkRadioButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            initLayout();
        }
        
        public BkRadioButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initLayout();
        }
        
        protected void initLayout() {
//            setScrollContainer(true);
//            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        public void setTextResId(@StringRes int resid, int colorid) {
            setButtonDrawable(new Drawable() {
                @Override
                public void draw(Canvas canvas) { }
                @Override
                public void setAlpha(int alpha) { }
                @Override
                public void setColorFilter(ColorFilter colorFilter) { }
                @Override
                public int getOpacity() { return 0; }
            });

            setText(resid);

            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(getTextSize());
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.density = getResources().getDisplayMetrics().density;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextColor(getResources().getColorStateList(colorid, null));
            } else {
                setTextColor(getResources().getColorStateList(colorid));
            }
        }

        @Nullable
        @Override
        public void setButtonDrawable(int resid) {
            mDrawable = getDrawable(resid);
        }

        private Drawable getDrawable(int resid) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                return getContext().getResources().getDrawable(resid, null);
            } else {
                return getContext().getResources().getDrawable(resid);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (getText() != null && getText().length() != 0) {
//                super.onDraw(canvas);
                int w = this.getWidth() / 2;
                int h = this.getHeight() / 2 + (int) (mTextPaint.getTextSize() / 2);

                mTextPaint.drawableState = getDrawableState();
                mTextPaint.setColor(getCurrentTextColor());
                canvas.drawText(getText().toString(), w, h, mTextPaint);

                return ;
            }

            // @see http://blog.mohitkanwal.com/blog/2013/10/02/making-a-custom-centre-aligned-radio-button-with-a-state-list-drawable/

            if (mDrawable != null) {
                mDrawable.setState(getDrawableState());
                final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
                final int height = mDrawable.getIntrinsicHeight();

                int y = 0;

                switch (verticalGravity) {
                    case Gravity.BOTTOM:
                        y = getHeight() - height;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        y = (getHeight() - height) / 2;
                        break;
                }

                int buttonWidth = mDrawable.getIntrinsicWidth();
                int buttonLeft = (getWidth() - buttonWidth) / 2;
                mDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
                mDrawable.draw(canvas);
            }
        }
    }
}
