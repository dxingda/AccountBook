package com.example.xingdadong.accountbook;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/21/15.
 */
public class FrontPageRecyclerAdapter extends RecyclerView.Adapter<FrontPageRecyclerAdapter.ViewHolder> {
    private List<Map<String,?>> myDataSet;
    public OnItemClickListener mItemClickListener;
    public int expandedPosition = -1;

    public FrontPageRecyclerAdapter(List<Map<String, ?>> mDataSet){
        myDataSet=mDataSet;
    }
    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
        public void onShareClick(View view,int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ic_type;
        public TextView type;
        public TextView amount;
        public ImageView discard;
        public ImageView share;
        public ViewHolder(View v) {
            super(v);
            ic_type=(ImageView)v.findViewById(R.id.ic_type);
            type=(TextView)v.findViewById(R.id.type);
            amount=(TextView)v.findViewById(R.id.amount);
            discard=(ImageView)v.findViewById(R.id.discard);
            share=(ImageView)v.findViewById(R.id.share);
            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });
            discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float amount=(Float)(myDataSet.get(getPosition()).get("amount"));
                    int pos=(Integer)myDataSet.get(getPosition()).get("type");
                    Activity_ViewPage.category.update(Activity_ViewPage.category_file,pos,(-1)*amount);
                    Fragment_FrontPage.expense.setText(String.format("%.2f",Activity_ViewPage.category.getExpense()));
                    Fragment_FrontPage.income.setText(String.format("%.2f",(Math.abs(Activity_ViewPage.category.getIncome()))));
                    myDataSet.remove(getPosition());
                    Activity_ViewPage.data.writeToFile(Activity_ViewPage.filename);
                    FrontPageRecyclerAdapter.this.notifyItemRemoved(getPosition());
                }
            });
            share.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onShareClick(v,getPosition());
                }
            });


        }

        public void bindData(Map<String,?> data){
            int i=(Integer)data.get("type");
            ic_type.setImageResource((Integer)(Activity_ViewPage.category.getItem(i).get("icon")));
            type.setText((String)(Activity_ViewPage.category.getItem(i).get("type")));
            amount.setText(String.format("%.2f",Math.abs((Float) data.get("amount"))));
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
        holder .bindData(myDataSet.get(position));
        if (position == expandedPosition) {
            holder.share.setVisibility(View.VISIBLE);
            holder.discard.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.GONE);
            holder.amount.setVisibility(View.GONE);

        } else {
            holder.share.setVisibility(View.GONE);
            holder.discard.setVisibility(View.GONE);
            holder.type.setVisibility(View.VISIBLE);
            holder.amount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }

}
