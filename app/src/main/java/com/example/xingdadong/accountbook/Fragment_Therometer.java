package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevin on 7/6/2014.
 */
public class Fragment_Therometer extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        // textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

            /*
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Bitmap tempBitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(tempBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTypeface(Typeface.create("Helvetica", Typeface.BOLD));
            paint.setStrokeWidth(2);
            paint.setTextSize(50);
            canvas.drawText("Hello World", 0, 100, paint);

            imageView.setImageBitmap(tempBitmap);

            DrawTextView myCustomView = (DrawTextView) rootView.findViewById(R.id.mycustomview);
            myCustomView.setText("Hello World! This is an experiment.");
            */

        final ThermometerView thermometer = (ThermometerView) rootView.findViewById(R.id.thermometer);


       // thermometer.setHandTarget(40f);
        /*
        thermometer.setKnobListener(new ThermometerView.RotaryKnobListener() {
            @Override
            public void onKnobChanged(float arg) {
                thermometer1.setHandTarget(arg);
                thermometer2.setHandTarget(arg);
            }
        });
*/
        return rootView;
    }
}