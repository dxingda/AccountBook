package com.example.xingdadong.accountbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Dios on 4/19/2015.
 */
public class Fragment_Weekly extends Fragment {
    private String selectedDate;
    private long time;
    private Date day1;
    private Date currentDate = Calendar.getInstance().getTime();
    private int start = 0;
    private int end = 0;

    private WeeklyChartView chartView;
    private CalendarView calendar;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    public Fragment_Weekly() { };

    /////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////



    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);

/*
       Activity_ViewPage.data.add(1, 1429761600000L,300,false);
        Activity_ViewPage.category.update(Activity_ViewPage.category_file,1,300);
        Activity_ViewPage.data.add(2, 1429675200000L,400,false);
        Activity_ViewPage.category.update(Activity_ViewPage.category_file,2,400);
        Activity_ViewPage.data.add(3, 1429588800000L,500,false);
        Activity_ViewPage.category.update(Activity_ViewPage.category_file,3,500);
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        final View rootView = inflater.inflate(R.layout.layout_weekly,container,false);

        calendar = (CalendarView) rootView.findViewById(R.id.calendar);

        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        Calendar calen = Calendar.getInstance();
        int weekNumber = calen.get(Calendar.WEEK_OF_YEAR);
        int cYear = calen.get(Calendar.YEAR);
        calen.clear();
        calen.set(Calendar.WEEK_OF_YEAR,weekNumber);
        calen.set(Calendar.YEAR,cYear);

        chartView = (WeeklyChartView) rootView.findViewById(R.id.weeklyCanvas);
        chartView.setDay(calen.getTime());


        //////////////////////////////////////////////////////////////


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month++;
                //Toast.makeText(getActivity(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                String sy, sm, sd;
                sy = "" + year;
                if (month < 10) {
                    sm = "0" + month;
                } else {
                    sm = "" + month;
                }
                if (day < 10) {
                    sd = "0" + day;
                } else {
                    sd = "" + day;
                }
                selectedDate = "" + sy + sm + sd;

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();////////////////////////////////////////////////
                try{
                    date = df.parse(selectedDate);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int week = cal.get(Calendar.WEEK_OF_YEAR);

                cal.clear();
                cal.set(Calendar.WEEK_OF_YEAR,week);
                cal.set(Calendar.YEAR,year);
                day1=cal.getTime();

                chartView = (WeeklyChartView) rootView.findViewById(R.id.weeklyCanvas);
                chartView.setDay(day1);
                System.out.println(date.getTime());
            /////////////////////////////////////////////////////////////////////////////////////////////////////
                int indexLength = Activity_ViewPage.data.getSize();
                time = date.getTime();
                //System.out.println(""+date);
                int flag1=0;
                start=0;
                for (int i=0; i <indexLength; i++) {
                    start = i;
                    if ((long) Activity_ViewPage.data.getItem(i).get("date") >= time) {
                        flag1=1;
                        break;
                    }
                }
                if(flag1==0){
                    start++;
                }
                end=0;
                int flag2=0;
                for (int j = start; j < indexLength; j++) {
                    end = j;
                    if ((long) Activity_ViewPage.data.getItem(j).get("date") > time + 1 * 24 * 60 * 60 * 1000) {
                        flag2 = 1;
                        break;
                    }
                }
                if(flag2==0) end++;
                mRecyclerViewAdapter.start=start;
                mRecyclerViewAdapter.end=end;
                System.out.println("start: " +start+" end: "+end);
                if (start==end) {Toast.makeText(getActivity(), "No Expense on the Selected Day", Toast.LENGTH_LONG).show();}
                if (start>end) {Toast.makeText(getActivity(), "The Date Yet to Come", Toast.LENGTH_LONG).show();}
                mRecyclerViewAdapter.notifyDataSetChanged();

                /////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        //Date d = new Date(time);
        //System.out.println(""+d);

        calen = Calendar.getInstance();
//////////////////////////////////////////////////////////////
        int ri = calen.get(Calendar.DATE);
        int yue = calen.get(Calendar.MONTH)+1;
        int nian = calen.get(Calendar.YEAR);

        String sy2, sm2, sd2;
        sy2 = "" + nian;
        if (yue < 10) {
            sm2 = "0" + yue;
        } else {
            sm2 = "" + yue;
        }
        if (ri < 10) {
            sd2 = "0" + ri;
        } else {
            sd2 = "" + ri;
        }
        String defaultday = "" + sy2 + sm2 + sd2;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date();////////////////////////////////////////////////
        try{
            d = sdf.parse(defaultday);
        }catch (Exception e){
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////////////////

        time =  d.getTime();
        System.out.println(""+d);

        int indexLength = Activity_ViewPage.data.getSize();
        int flag1=0;
        for (int i=0; i <indexLength; i++) {
            start = i;
            if ((long) Activity_ViewPage.data.getItem(i).get("date") >= time) {
                flag1=1;
                break;
            }
        }
        if(flag1==0){
            start++;
        }
        int flag2=0;
        for (int j = start; j < indexLength; j++) {
            end = j;
            if ((long) Activity_ViewPage.data.getItem(j).get("date") > time + 1 * 24 * 60 * 60 * 1000) {
                flag2 = 1;
                break;
            }
        }
        if(flag2==0) end++;
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.costView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(Activity_ViewPage.data.getCostList(),start,end );
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        System.out.println("start: " +start+" end: "+end);
        return rootView;
    }
}
