package com.example.xingdadong.accountbook;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddItem extends Fragment {
    private float left;
    private float top;
    ImageView img;
    public Fragment_AddItem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_add_item, container, false);
        RecyclerView myRecyclerView=(RecyclerView)rootView.findViewById(R.id.cardList);
        myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        final CategoryRecyclerAdapter adp=new CategoryRecyclerAdapter(Activity_ViewPage.category.getCategoryList());
        myRecyclerView.setAdapter(adp);
        final ImageView icon=(ImageView)rootView.findViewById(R.id.ic_type);
        final TextView type= (TextView)rootView.findViewById(R.id.type);
        final int[] arr=new int[1];
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        img=(ImageView)rootView.findViewById(R.id.ic_type);
        adp.SetOnItemClickListener(new CategoryRecyclerAdapter.OnItemClickListener() {
            //@Override
            public void onItemClick(View view, int position) {
                img.setVisibility(View.INVISIBLE);
                icon.setImageResource((int) (Activity_ViewPage.category.getItem(position).get("icon")));
                type.setText((String) (Activity_ViewPage.category.getItem(position).get("type")));
                arr[0] = position;
                left=view.getX();
                top=view.getY();
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(100, 100);
                layoutParams.setMargins((int)left,(int)top, 0, 0);
                img.setLayoutParams(layoutParams);
                TranslateAnimation anim = new TranslateAnimation(0,(-1)*left,0f,height-top-300);
                anim.setInterpolator(new  BounceInterpolator());
                anim.setDuration(1500);
                ScaleAnimation anim2 = new ScaleAnimation(1.55f,1f,1.55f,1f);
                anim2.setDuration(1000);
                AnimationSet animSet= new AnimationSet(false);
                animSet.addAnimation(anim2);
                animSet.addAnimation(anim);
                animSet.setFillAfter(true);
                animSet.setRepeatCount(0);
                img.setVisibility(View.VISIBLE);
                img.startAnimation(animSet);
            }
        });
        final EditText edittext = (EditText)rootView.findViewById(R.id.amount);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                float amount;
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    amount = Float.valueOf(edittext.getText().toString());
                    // Perform action on key press
                    if (arr[0] == 8) {
                        Activity_ViewPage.data.add(arr[0], System.currentTimeMillis(), (-1) * amount, false);
                        Activity_ViewPage.category.update(Activity_ViewPage.category_file, arr[0], (-1) * amount);

                    } else {
                        Activity_ViewPage.data.add(arr[0], System.currentTimeMillis(), amount, false);
                        Activity_ViewPage.category.update(Activity_ViewPage.category_file, arr[0], amount);
                    }
                    Activity_ViewPage.data.writeToFile(Activity_ViewPage.filename);
                    Fragment_FrontPage.adp.notifyItemInserted(Activity_ViewPage.data.getSize());
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }
}
