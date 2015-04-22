package com.example.xingdadong.accountbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class Activity_ViewPage extends ActionBarActivity {
    ViewPager mViewPager;
    MyFragmentStatePagerAdapter myPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        myPagerAdapter=new MyFragmentStatePagerAdapter(getSupportFragmentManager(),2);
        mViewPager=(ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setPageTransformer(false,new ViewPager.PageTransformer(){
            public void transformPage(View page,float position){
                final float normalized_position=Math.abs(Math.abs(position)-1);
                page.setAlpha(normalized_position);
                page.setScaleX(normalized_position/2+0.5f);
                page.setScaleY(normalized_position/2+0.5f);
            }
        });
    }
    public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        int count;
        public MyFragmentStatePagerAdapter(FragmentManager fm,int count) {
            super(fm);
            this.count=count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return new Fragment_FrontPage();
                case 0:
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
}
