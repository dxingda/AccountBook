package com.example.xingdadong.accountbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/22/15.
 */
public class Category {
    List<Map<String,?>> categoryList;
    public Category(){
        categoryList=new ArrayList();
        createCategory();
    }
    public int getSize(){
        return categoryList.size();
    }
    public List<Map<String,?>> getCategoryList(){
        return categoryList;
    }
    public HashMap getItem(int i){
        if(i>=0 && categoryList.size()>i){
            return (HashMap)categoryList.get(i);
        }else
            return null;
    }
    private void createCategory(){
        HashMap category;
        category=new HashMap();
        category.put("id",0);
        category.put("icon",R.drawable.clothes);
        category.put("type","clothes");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",1);
        category.put("icon",R.drawable.dinning);
        category.put("type","dinning");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",2);
        category.put("icon",R.drawable.entertainment);
        category.put("type","entertainment");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",3);
        category.put("icon",R.drawable.online);
        category.put("type","online");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",4);
        category.put("icon",R.drawable.shopping);
        category.put("type","shopping");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",5);
        category.put("icon",R.drawable.snacks);
        category.put("type","snacks");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",6);
        category.put("icon",R.drawable.ticket);
        category.put("type","ticket");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",7);
        category.put("icon",R.drawable.travel);
        category.put("type","income");
        categoryList.add(category);

        category=new HashMap();
        category.put("id",8);
        category.put("icon",R.drawable.others);
        category.put("type","others");
        categoryList.add(category);
    }
}
