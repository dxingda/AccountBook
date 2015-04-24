package com.example.xingdadong.accountbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Data {
    List<Map<String,?>> costList;
    public Data(String filename){
        costList=new ArrayList();
        readFromFile(filename);
    }
    public int getSize(){
        return costList.size();
    }
    public List<Map<String,?>> getCostList(){
        return costList;
    }
    public HashMap getItem(int i){
        if(i>=0 && costList.size()>i){
            return (HashMap)costList.get(i);
        }else
            return null;
    }
    public void readFromFile(String filename){
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(filename);
            ObjectInputStream temp = new ObjectInputStream(inputStream);
            costList = (ArrayList<Map<String,?>>)temp.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeToFile(String filename){
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filename, false);
            ObjectOutputStream temp=new ObjectOutputStream(outputStream);
            temp.writeObject(costList);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void add(int type,long date,float amount,boolean like){
        HashMap cost = new HashMap();
        cost.put("type",type);
        cost.put("date",date);
        cost.put("amount",amount);
        cost.put("like",like);
        costList.add(cost);
    }
}
