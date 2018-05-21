package com.appchallengers.appchallengers.fragments.login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.helpers.setpages.SetLoginPages;
import com.appchallengers.appchallengers.helpers.util.Utils;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.io.InputStream;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;

public class IntroFragment extends Fragment implements View.OnClickListener{

    private View mRootView;
    private SliderLayout mIntroSlider;
    private Button mCreateAccountButton;
    private SharedPreferences mSharedPreferences;
    private TextView mLoginTextview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_intro, container, false);
        initialView(mRootView);
        return mRootView;
    }

    private void initialView(View mRootView) {
        mIntroSlider=(SliderLayout)mRootView.findViewById(R.id.slider);
        mCreateAccountButton=(Button)mRootView.findViewById(R.id.intro_fragment_create_account_button);
        mLoginTextview=(TextView)mRootView.findViewById(R.id.intro_fragment_login_link_textview);
        mSharedPreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        initInroSlider();
        mCreateAccountButton.setOnClickListener(this);
        mLoginTextview.setOnClickListener(this);
        controlAuthorization();
    }

    private void initInroSlider() {


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("changlleShared", R.mipmap.changlleshared);
        file_maps.put("videotake", R.mipmap.videotake);
        file_maps.put("videoplay", R.mipmap.videoplay);
        for (String name : file_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            mIntroSlider.addSlider(textSliderView);
        }
        mIntroSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mIntroSlider.setCustomIndicator((PagerIndicator)mRootView.findViewById(R.id.custom_indicator));
        mIntroSlider.setCustomAnimation(new DescriptionAnimation());
        mIntroSlider.setDuration(4000);
    }

    @Override
    public void onPause() {
        mIntroSlider.stopAutoCycle();
        super.onPause();
    }

    @Override
    public void onResume() {
        mIntroSlider.startAutoCycle();
        super.onResume();
    }

    @Override
    public void onStop() {
        mIntroSlider.stopAutoCycle();
        super.onStop();
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.intro_fragment_create_account_button:{
                SetLoginPages.getInstance().constructor(getActivity(),3);
                break;
            }
            case R.id.intro_fragment_login_link_textview:{
                SetLoginPages.getInstance().constructor(getActivity(),2);
                break;
            }
        }
    }
    private void controlAuthorization() {
        Utils.sharedPreferences = mSharedPreferences;
        String control = Utils.getPref("token");
        if (control == null) {

        } else {
            SetLoginPages.getInstance().constructor(getActivity(), 4);
        }
    }

}
