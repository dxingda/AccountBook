package com.example.xingdadong.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Date;
import java.util.HashMap;

public class Fragment_FrontPage extends Fragment{
    public static final FrontPageRecyclerAdapter adp=new FrontPageRecyclerAdapter(Activity_ViewPage.data.getCostList());
    public static TextView income;
    public static TextView expense;
    static View v;
    private RecyclerView myRecyclerView;
    public Fragment_FrontPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_front_page, container, false);

        myRecyclerView=(RecyclerView)rootView.findViewById(R.id.cardList);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ImageButton btn=(ImageButton)rootView.findViewById(R.id.btn1);
        final TextView income=(TextView)rootView.findViewById(R.id.income);
        final TextView expense=(TextView)rootView.findViewById(R.id.expense);
        this.income=income;
        this.expense=expense;
        myRecyclerView.setAdapter(adp);
        myRecyclerView.scrollToPosition(Activity_ViewPage.data.getSize()-1);
        adp.SetOnItemClickListener(new FrontPageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int prev=-1;
                // Check for an expanded view, collapse if you find one
                prev = adp.expandedPosition;
                view.findViewById(R.id.share).animate().setDuration(500).translationXBy(250);
                view.findViewById(R.id.like).animate().setDuration(500).translationXBy(-250);
                if(prev==position){
                    adp.expandedPosition=-1;
                }else {
                    // Set the current position to "expanded"
                    adp.expandedPosition = position;
                }
                if(prev==-1){
                }else{
                    v.findViewById(R.id.share).animate().setDuration(500).translationXBy(-250);
                    v.findViewById(R.id.like).animate().setDuration(500).translationXBy(250);
                }
                adp.notifyItemChanged(position);
                adp.notifyItemChanged(prev);
                v=view;
            }
            public void onShareClick(View view,int position){
                Intent intentShare=new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                HashMap item=Activity_ViewPage.data.getItem(position);
                int type=(Integer)item.get("type");
                Date date=new Date((Long)item.get("date"));
                HashMap category=Activity_ViewPage.category.getItem(type);
                String s="On "+ date.toString()+", I spend $"+
                        String.format("%.2f",item.get("amount"))+" on "+category.get("type").toString()+".";
                intentShare.putExtra(Intent.EXTRA_TEXT,s);
                startActivity(Intent.createChooser(intentShare, "How do you want to share?"));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Activity_AddItem.class);
                startActivity(i);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    public void onResume(){
        super.onResume();
        income.setText(String.format("%.2f",(Math.abs(Activity_ViewPage.category.getIncome()))));
        expense.setText(String.format("%.2f", (Activity_ViewPage.category.getExpense())));
        myRecyclerView.scrollToPosition(Activity_ViewPage.data.getSize()-1);
    }
}
