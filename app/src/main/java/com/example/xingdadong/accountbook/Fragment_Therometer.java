package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by kevin on 7/6/2014.
 */
public class Fragment_Therometer extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int num = 0;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment_Therometer newInstance(int sectionNumber) {
        Fragment_Therometer fragment = new Fragment_Therometer();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Therometer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_thermometer, container, false);
        final ThermometerView thermometer = (ThermometerView) rootView.findViewById(R.id.thermometer);


        ImageView imageView = (ImageView) rootView.findViewById(R.id.piechart_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int n = thermometer.getCurrentPosition();

                thermometer.setCurrentPosition(((n+1)>8)? 0 : n+1);

                System.out.println("Listener :" +thermometer.getCurrentPosition());

                thermometer.animate().setDuration(100);
                thermometer.animate().x(65).y(130)
                        .rotation(10.f*num++);
            }
        });

        return rootView;
    }
}