package com.appchallengers.appchallengers.helpers.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

import com.appchallengers.appchallengers.R;

/**
 * Created by MHMTNASIF on 22.02.2018.
 */

@SuppressLint("AppCompatCustomView")
public class ButtonSFUITextRegularComponent extends br.com.simplepass.loading_button_lib.customViews.CircularProgressButton{
    private Context mContext;

    public ButtonSFUITextRegularComponent(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ButtonSFUITextRegularComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ButtonSFUITextRegularComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ButtonSFUITextRegularComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/SFUIText-Regular.ttf");
        setTypeface(font);

    }
}
