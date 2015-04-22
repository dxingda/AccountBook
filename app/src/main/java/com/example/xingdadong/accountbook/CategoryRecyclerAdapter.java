package com.example.xingdadong.accountbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/22/15.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>  {
    private List<Map<String,?>> category;
    public OnItemClickListener mItemClickListener;

    public CategoryRecyclerAdapter(List<Map<String,?>> mCategory){
        category=mCategory;
    }
    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.additem_cell, parent, false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(category.get(position));
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ic_type;
        public ViewHolder(View v) {
            super(v);
            ic_type=(ImageView)v.findViewById(R.id.ic_type);
            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });
        }
        public void bindData(Map<String,?> data){
            int i=(Integer)data.get("id");
            ic_type.setImageResource((Integer)(category.get(i).get("icon")));
        }
    }
}
