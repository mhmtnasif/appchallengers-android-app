package com.appchallengers.appchallengers.helpers.component;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

import com.appchallengers.appchallengers.R;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

@SuppressLint("AppCompatCustomView")
public class EditTextComponent extends EditText {
    private Context mContext;
    public EditTextComponent(Context context) {
        super(context);
        mContext=context;
        init();
    }

    public EditTextComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    public EditTextComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditTextComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/SFUIText-Regular.ttf");
        setTypeface(font);
        
    }
}
