package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dxingda on 4/19/2015.
 */
public class Fragment_front_test extends Fragment{
    public Fragment_front_test(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_front_test,container,false);
        return rootView;
    }

}
