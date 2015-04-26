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
        public ImageView like;
        public ImageView share;
        public ImageView favorite;
        public ViewHolder(View v) {
            super(v);
            ic_type=(ImageView)v.findViewById(R.id.ic_type);
            type=(TextView)v.findViewById(R.id.type);
            amount=(TextView)v.findViewById(R.id.amount);
            like=(ImageView)v.findViewById(R.id.like);
            share=(ImageView)v.findViewById(R.id.share);
            favorite=(ImageView)v.findViewById(R.id.favorite);
            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    mItemClickListener.onItemClick(v,getPosition());

                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean prev=(Boolean)myDataSet.get(getPosition()).get("like");
                    ((HashMap<String,Boolean>)(myDataSet.get(getPosition()))).put("like",!prev);
                    FrontPageRecyclerAdapter.this.notifyDataSetChanged();
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
            if((Boolean)data.get("like")){
                favorite.setVisibility(View.VISIBLE);
            }else{
                favorite.setVisibility(View.INVISIBLE);
            }
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
            holder.like.setVisibility(View.VISIBLE);
            holder.favorite.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
            holder.amount.setVisibility(View.GONE);

        } else {
            holder.share.setVisibility(View.GONE);
            holder.like.setVisibility(View.GONE);
            holder.type.setVisibility(View.VISIBLE);
            holder.amount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }

}
