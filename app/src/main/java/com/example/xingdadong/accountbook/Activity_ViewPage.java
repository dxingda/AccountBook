package com.example.xingdadong.accountbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class Activity_ViewPage extends ActionBarActivity {
    public final static String filename = "/data/data/com.example.xingdadong.accountbook/files/data";
    public final static String category_file="/data/data/com.example.xingdadong.accountbook/files/category";
    public static final Data data=new Data(filename);
    public static final Category category = new Category(category_file);
    ViewPager mViewPager;
    public MyFragmentStatePagerAdapter myPagerAdapter;
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

}
