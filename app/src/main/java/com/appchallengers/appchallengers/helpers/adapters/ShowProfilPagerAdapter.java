package com.appchallengers.appchallengers.helpers.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilAcceptedChallangesFragment;
import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilChallangeFragment;
import com.appchallengers.appchallengers.fragments.show_profil_activity_fragment.ProfilFriendFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserAcceptedChallengesFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserFriendFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.User_ChallengeFragment;

/**
 * Created by jir on 18.4.2018.
 */

public class ShowProfilPagerAdapter extends FragmentStatePagerAdapter {
    private int mNoTabs;
    public ShowProfilPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoTabs=NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                ProfilChallangeFragment profilChallangeFragment=new ProfilChallangeFragment();
                return  profilChallangeFragment;
            }

            case 1:{
                ProfilAcceptedChallangesFragment profilAcceptedChallangesFragment =new ProfilAcceptedChallangesFragment();
                return profilAcceptedChallangesFragment;
            }
            case 2:{
                ProfilFriendFragment profilFriendFragment =new ProfilFriendFragment();
                return profilFriendFragment;
            }
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return mNoTabs;
    }
}

