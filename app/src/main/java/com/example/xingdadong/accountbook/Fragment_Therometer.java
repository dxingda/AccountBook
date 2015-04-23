package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView income, expense, balance;
    private ThermometerView thermometer;
    private int index = 0;



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
        thermometer = (ThermometerView) rootView.findViewById(R.id.thermometer);
        income = (TextView)rootView.findViewById(R.id.textview_income_number);
        expense= (TextView)rootView.findViewById(R.id.textview_expense_number);
        balance=(TextView)rootView.findViewById(R.id.textview_balance_number);
        final TextView cate = (TextView)rootView.findViewById(R.id.lable1);
        final TextView amnt = (TextView)rootView.findViewById(R.id.lable2);
        final ImageView label = (ImageView)rootView.findViewById(R.id.thero_icon);


        while((float)Activity_ViewPage.category.getItem(index).get("amount")<=0)
        {
            ++index;

            if(index>Activity_ViewPage.category.getSize()-1) index=0;
        }

        cate.setText(Activity_ViewPage.category.getItem(index).get("type").toString());
        amnt.setText(Activity_ViewPage.category.getItem(index).get("amount").toString());
        label.setImageResource((int)Activity_ViewPage.category.getItem(index).get("icon"));
        index++;


        ImageView imageView = (ImageView) rootView.findViewById(R.id.piechart_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int n = thermometer.getCurrentPosition();

                thermometer.setCurrentPosition(((n+1)>8)? 0 : n+1);


                while((float)Activity_ViewPage.category.getItem(index).get("amount")<=0)
                {
                    ++index;

                    if(index>Activity_ViewPage.category.getSize()-1) index=0;
                }

                cate.setText(Activity_ViewPage.category.getItem(index).get("type").toString());
                amnt.setText(Activity_ViewPage.category.getItem(index).get("amount").toString());
                label.setImageResource((int)Activity_ViewPage.category.getItem(index).get("icon"));

                System.out.println("Listener :" +thermometer.getCurrentPosition());

                thermometer.animate().setDuration(100);
                thermometer.animate().x(65).y(130)
                        .rotation(-10.f*num++);
                index++;
                if(index>Activity_ViewPage.category.getSize()-1) index=0;

            }
        });

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        float in=Activity_ViewPage.category.getIncome();
        float out = Activity_ViewPage.category.getExpense();
        income.setText(Float.toString(in*(-1)));
        expense.setText(Float.toString(out));
        balance.setText(Float.toString(in+out));
        for(int i = 0; i<9;i++)
        {
            thermometer.setAmount(i, (float)Activity_ViewPage.category.getItem(i).get("amount"));
            System.out.println("category="+i+" amount="+(float)Activity_ViewPage.category.getItem(i).get("amount"));
        }

        thermometer.setTotal(Activity_ViewPage.category.getExpense());
    }
}