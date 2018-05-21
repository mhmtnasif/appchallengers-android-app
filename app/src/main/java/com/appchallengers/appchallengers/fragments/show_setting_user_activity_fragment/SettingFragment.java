package com.appchallengers.appchallengers.fragments.show_setting_user_activity_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.appchallengers.appchallengers.MainActivity;
import com.appchallengers.appchallengers.R;
import com.appchallengers.appchallengers.ShowProfilActivity;
import com.appchallengers.appchallengers.WebViewActivity;
import com.appchallengers.appchallengers.helpers.setpages.SetSettingPages;
import com.appchallengers.appchallengers.helpers.util.Constants;


import io.reactivex.disposables.CompositeDisposable;


import static android.content.Context.MODE_PRIVATE;
import static com.appchallengers.appchallengers.helpers.util.Constants.MY_PREFS_NAME;


public class SettingFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {
    private ListView mListViewSetting;
    private ArrayAdapter<String> mArrayAdapter;
    private CompositeDisposable mCompositeDisposable;
    private View mRootView;
    private SharedPreferences mSharedPreferences;
    private String[] mUlkeler =
            {"Profil Ayarları","Şifreni Değiştir","E-mail Değiştir","Hakkımızda", "Çıkış Yap"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
        initialView(mRootView);
        return mRootView;
    }
    private void initialView(View mRootView) {
        mCompositeDisposable=new CompositeDisposable();
        mListViewSetting = (ListView) mRootView.findViewById(R.id.fragment_setting_listview);
        mSharedPreferences = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_setting, R.id.text_item, mUlkeler);
        mListViewSetting.setAdapter(mArrayAdapter);
        mListViewSetting.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: {//Profil setting
                SetSettingPages.getInstance().constructor(getActivity(),1);
                break;
            }
            case 1: {//Password setting
                SetSettingPages.getInstance().constructor(getActivity(),2);
                break;
            }
            case 2: {//Email setting
                SetSettingPages.getInstance().constructor(getActivity(),3);
                break;
            }
            case 3: {// About setting
                Intent intent=new  Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("web",Constants.TERMS_AND_COOKİE);
                getActivity().startActivity(intent);
                break;
            }
            case 4: {//App exit
                mSharedPreferences.edit().remove("token").commit();
                mSharedPreferences.edit().remove("fullName").commit();
                mSharedPreferences.edit().remove("imageUrl").commit();
                mSharedPreferences.edit().remove("email").commit();
                mSharedPreferences.edit().remove("active").commit();
                mSharedPreferences.edit().remove("status").commit();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_setting_back_Image:{
                startActivity(new Intent(getContext(), ShowProfilActivity.class));
                getActivity().finish();
            }
        }

    }
}
