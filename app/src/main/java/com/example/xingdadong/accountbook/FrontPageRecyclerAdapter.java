package com.example.xingdadong.accountbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/21/15.
 */
public class FrontPageRecyclerAdapter extends RecyclerView.Adapter<FrontPageRecyclerAdapter.ViewHolder> {
    private List<Map<String,?>> myDataSet;
    public OnItemClickListener mItemClickListener;

    public FrontPageRecyclerAdapter(List<Map<String, ?>> mDataSet){
        myDataSet=mDataSet;
    }
    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ic_type;
        public TextView type;
        public TextView amount;
        public ViewHolder(View v) {
            super(v);
            ic_type=(ImageView)v.findViewById(R.id.ic_type);
            type=(TextView)v.findViewById(R.id.type);
            amount=(TextView)v.findViewById(R.id.amount);
            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });
        }
        public void bindData(Map<String,?> data){
            int i=(Integer)data.get("type");
            ic_type.setImageResource((Integer)(Activity_ViewPage.category.getItem(i).get("icon")));
            type.setText((String)(Activity_ViewPage.category.getItem(i).get("type")));
            amount.setText(Float.toString(Math.abs((Float)data.get("amount"))));
        }
    }
    public Object getItem(int position){
        return myDataSet.get(position);
    }
    public int getItemViewType(int position){
        if((Float)(myDataSet.get(position).get("amount"))>0)
            return 1;
        else
            return 2;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_cell_expense, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_cell_income,parent,false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_cell_expense, parent, false);
                break;
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FrontPageRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindData(myDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }
}
