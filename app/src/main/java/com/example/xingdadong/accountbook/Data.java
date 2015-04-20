package com.example.xingdadong.accountbook;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdadong on 4/20/15.
 */
public class Data {
    List<Map<String,?>> costList;
    public Data(){
        costList=new ArrayList<Map<String,?>>();
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
    public void readFromFile(String filename,Context context){
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(filename);
            ObjectInputStream temp = new ObjectInputStream(inputStream);
            costList = (ArrayList<Map<String,?>>)temp.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeToFile(String filename,Context context){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream temp=new ObjectOutputStream(outputStream);
            temp.writeObject(costList);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
