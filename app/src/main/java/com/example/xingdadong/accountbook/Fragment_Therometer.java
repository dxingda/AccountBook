package com.example.xingdadong.accountbook;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Therometer extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int num = 0;
    private TextView income, expense, balance;
    private TextView cate,amnt;
    private ImageView label;
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
        cate = (TextView)rootView.findViewById(R.id.lable1);
        amnt = (TextView)rootView.findViewById(R.id.lable2);
        label = (ImageView)rootView.findViewById(R.id.thero_icon);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.piechart_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // int n = thermometer.getCurrentPosition();
                if(Activity_ViewPage.data.getSize()==0){
                    cate.setText("category");
                    amnt.setText("0.0");
                    Toast.makeText(getActivity(),"No data entry!",Toast.LENGTH_SHORT).show();
                }else{
                    int i;
                    index %= 9;
                    for(i=0;(i<Activity_ViewPage.category.getSize())&&
                            ((float)Activity_ViewPage.category.getItem(index).get("amount")<=0)
                            ;i++){
                        index++;
                        index %= (float)Activity_ViewPage.category.getSize();
                    }
                    if(i<(float)Activity_ViewPage.category.getSize()) {
                        cate.setText(Activity_ViewPage.category.getItem(index).get("type").toString());
                        amnt.setText(Activity_ViewPage.category.getItem(index).get("amount").toString());
                        label.setImageResource((int) Activity_ViewPage.category.getItem(index).get("icon"));
                        thermometer.setCurrentPosition(index);
                        thermometer.clearStartDegree();
                        thermometer.setAngle(index);
                        thermometer.setHandNeedsToMove();
                    }
                    index = (++index)%9;

                    //thermometer.setAngle(thermometer.getAngle() + 1);
                    //thermometer.animate().setDuration(100);
                    //thermometer.animate().rotation(-10.f * num++);

                    //thermometer.startAnimation( AnimationUtils.loadAnimation(getActivity(), R.anim.ani_rotation));
                }
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
        income.setText(Float.toString(Math.abs(in)));
        expense.setText(Float.toString(out));
        balance.setText(Float.toString(in+out));
        for(int i = 0; i<9;i++)
        {
            thermometer.setAmount(i, (float)Activity_ViewPage.category.getItem(i).get("amount"));
        }
        thermometer.setTotal(Activity_ViewPage.category.getExpense());
        int flag=0;
        index=0;
        if(Activity_ViewPage.data.getSize()==0){
            cate.setText("category");
            amnt.setText("0.0");
        }else{
            while((float)Activity_ViewPage.category.getItem(index).get("amount")<=0)
            {
                ++index;

                if(index>Activity_ViewPage.category.getSize()-1){
                    flag=1;
                    break;
                }
            }
            if(flag==0) {
                cate.setText(Activity_ViewPage.category.getItem(index).get("type").toString());
                amnt.setText(Activity_ViewPage.category.getItem(index).get("amount").toString());
                label.setImageResource((int) Activity_ViewPage.category.getItem(index).get("icon"));
                thermometer.setStartDegree(180*(Float)(Activity_ViewPage.category.getItem(index).get("amount"))/
                        Activity_ViewPage.category.getExpense());

                index++;
            }
        }
    }
}