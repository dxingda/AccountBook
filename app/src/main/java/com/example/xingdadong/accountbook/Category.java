package com.example.xingdadong.accountbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/22/15.
 */
public class Category {
    List<Map<String,?>> categoryList;
    public Category(String filename){
        categoryList=new ArrayList();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(filename);
            ObjectInputStream temp = new ObjectInputStream(inputStream);
            categoryList = (ArrayList<Map<String,?>>)temp.readObject();
            inputStream.close();
        } catch (Exception e) {
            createCategory();
            System.out.println("file not exist, creating new category file");
        }
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
    public void update(String filename,int i,float amount){
        float prev=(Float)categoryList.get(i).get("amount");
        ((HashMap<String,Float>)(categoryList.get(i))).put("amount",amount+prev);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filename, false);
            ObjectOutputStream temp=new ObjectOutputStream(outputStream);
            temp.writeObject(categoryList);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public float getExpense(){
        int i;
        float expense=0;
        HashMap category;
        for(i=0;i<getSize()-1;i++){
            category=(HashMap)categoryList.get(i);
            expense+=(Float)category.get("amount");

        }
        return expense;
    }
    public float getIncome(){
        float income=0;
        HashMap category;
        category=(HashMap)categoryList.get(8);
        income+=(Float)category.get("amount");
        return income;
    }
    private void createCategory(){
        HashMap category;
        category=new HashMap();
        category.put("id",0);
        category.put("icon",R.drawable.clothes);
        category.put("type","clothes");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",1);
        category.put("icon",R.drawable.dinning);
        category.put("type","dinning");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",2);
        category.put("icon",R.drawable.entertainment);
        category.put("type","entertain");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",3);
        category.put("icon",R.drawable.online);
        category.put("type","online");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",4);
        category.put("icon",R.drawable.shopping);
        category.put("type","shopping");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",5);
        category.put("icon",R.drawable.snacks);
        category.put("type","snacks");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",6);
        category.put("icon",R.drawable.ticket);
        category.put("type","ticket");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",7);
        category.put("icon",R.drawable.travel);
        category.put("type","travel");
        category.put("amount",(float)0.0);
        categoryList.add(category);

        category=new HashMap();
        category.put("id",8);
        category.put("icon",R.drawable.dollar_icon);
        category.put("type","income");
        category.put("amount",(float)0.0);
        categoryList.add(category);
    }
}
