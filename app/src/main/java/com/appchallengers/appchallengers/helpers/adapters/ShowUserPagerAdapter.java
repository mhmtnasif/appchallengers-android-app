package com.appchallengers.appchallengers.helpers.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserAcceptedChallengesFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.UserFriendFragment;
import com.appchallengers.appchallengers.fragments.show_user_activity_fragment.User_ChallengeFragment;

/**
 * Created by jir on 17.4.2018.
 */

public class ShowUserPagerAdapter extends FragmentStatePagerAdapter {
    private int mNoTabs;
    private Bundle mBundle;
    public ShowUserPagerAdapter(FragmentManager fm, int NumberOfTabs, Bundle bundle) {
        super(fm);
        this.mNoTabs=NumberOfTabs;
        this.mBundle=bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                User_ChallengeFragment user_challengeFragment=new User_ChallengeFragment();
                user_challengeFragment.setArguments(mBundle);
                return  user_challengeFragment;
            }

            case 1:{
                UserAcceptedChallengesFragment userAcceptedChallengesFragment =new UserAcceptedChallengesFragment();
                userAcceptedChallengesFragment.setArguments(mBundle);
                return userAcceptedChallengesFragment;
            }
            case 2:{
                UserFriendFragment userFriendFragment =new UserFriendFragment();
                userFriendFragment.setArguments(mBundle);
                return userFriendFragment;
            }
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return mNoTabs;
    }
}
