package com.example.xingdadong.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddItem extends Fragment {


    public Fragment_AddItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add_item, container, false);
        RecyclerView myRecyclerView=(RecyclerView)rootView.findViewById(R.id.cardList);
        myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        final CategoryRecyclerAdapter adp=new CategoryRecyclerAdapter(Activity_ViewPage.category.getCategoryList());
        myRecyclerView.setAdapter(adp);
        final ImageView icon=(ImageView)rootView.findViewById(R.id.ic_type);
        final TextView type= (TextView)rootView.findViewById(R.id.type);
        final int[] arr=new int[1];
        adp.SetOnItemClickListener(new CategoryRecyclerAdapter.OnItemClickListener() {
            //@Override
            public void onItemClick(View view, int position) {
                icon.setImageResource((int)(Activity_ViewPage.category.getItem(position).get("icon")));
                type.setText((String)(Activity_ViewPage.category.getItem(position).get("type")));
                arr[0]=position;
            }
        });
        final EditText edittext = (EditText)rootView.findViewById(R.id.amount);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                float amount;
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    amount=Float.valueOf(edittext.getText().toString());
                    // Perform action on key press
                    if(arr[0]==8){
                        Activity_ViewPage.data.add(arr[0], 0,(-1)* amount);
                        Activity_ViewPage.category.update(Activity_ViewPage.category_file,arr[0],(-1)*amount);

                    }else {
                        Activity_ViewPage.data.add(arr[0], 0, amount);
                        Activity_ViewPage.category.update(Activity_ViewPage.category_file,arr[0],amount);
                    }
                    Activity_ViewPage.data.writeToFile(Activity_ViewPage.filename);
                    Fragment_FrontPage.adp.notifyDataSetChanged();
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }


}
