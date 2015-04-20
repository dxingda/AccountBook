package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kevin on 7/6/2014.
 */
public class Fragment_FancyRotaryKnob extends Fragment {
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment_FancyRotaryKnob newInstance(int sectionNumber) {
        Fragment_FancyRotaryKnob fragment = new Fragment_FancyRotaryKnob();
        return fragment;
    }

    public Fragment_FancyRotaryKnob() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fancyrotaryknob, container, false);
        final TextView status = (TextView) rootView.findViewById(R.id.status);

        FancyRotaryKnobView rotaryKnobView =
                 (FancyRotaryKnobView) rootView.findViewById(R.id.fancyRotaryKnobView);
        rotaryKnobView.setKnobListener(new FancyRotaryKnobView.RotaryKnobListener() {
            @Override
            public void onKnobChanged(int arg) {
                status.setText("Value: " +Integer.toString(arg));
            }
        });




        return rootView;
    }
}