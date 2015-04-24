package com.example.xingdadong.accountbook;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dios on 4/19/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Map<String, ?>> mDataset;
    int start;
    int end;

    public RecyclerViewAdapter(List<Map<String, ?>> myDataset,int start,int end){
        mDataset = myDataset;
        this.start=start;
        this.end=end;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView vIcon;
        public TextView vCatalog;
        public TextView vDate;
        public TextView vAmount;
        public ViewHolder(View v){
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.icon);
            vCatalog = (TextView) v.findViewById(R.id.catalog);
            vAmount = (TextView) v.findViewById(R.id.amount);
        }

        public void bindData(Map<String, ?> item){
            int i = (Integer)item.get("type");
            int res= (Integer)(Activity_ViewPage.category.getItem(i).get("icon"));
            String t = Activity_ViewPage.category.getItem(i).get("type").toString();
            vIcon.setImageResource(res);
            vCatalog.setText(t);
            vAmount.setText( item.get("amount").toString());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int n){
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> item = mDataset.get(position+start);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() { return end-start; }


    public Object getItem(int position) { return mDataset.get(position); }
}
