package com.example.xingdadong.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_FrontPage extends Fragment {
    private static final int REQUEST_CODE=0;
    public static final FrontPageRecyclerAdapter adp=new FrontPageRecyclerAdapter(Activity_ViewPage.data.getCostList());
    public Fragment_FrontPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_front_page, container, false);

        RecyclerView myRecyclerView=(RecyclerView)rootView.findViewById(R.id.cardList);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageButton btn=(ImageButton)rootView.findViewById(R.id.btn1);
        myRecyclerView.setAdapter(adp);
        adp.SetOnItemClickListener(new FrontPageRecyclerAdapter.OnItemClickListener() {
            //@Override
            public void onItemClick(View view, int position) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Activity_AddItem.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


}
