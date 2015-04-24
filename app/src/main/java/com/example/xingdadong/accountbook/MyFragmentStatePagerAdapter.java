package com.example.xingdadong.accountbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by xingdadong on 4/23/15.
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    int count;
    public MyFragmentStatePagerAdapter(FragmentManager fm,int count) {
        super(fm);
        this.count=count;
    }
    public interface FragmentLifecycle {

        public void onPauseFragment();
        public void onResumeFragment();

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_FrontPage();
            case 1:
                return new Fragment_Therometer();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}
