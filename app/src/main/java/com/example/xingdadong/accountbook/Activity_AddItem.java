package com.example.xingdadong.accountbook;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;



public class Activity_AddItem extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Fragment_AddItem())
                    .commit();
        }
    }

}
