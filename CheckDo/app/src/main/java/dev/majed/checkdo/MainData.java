package dev.majed.checkdo;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.paperdb.Paper;

import static dev.majed.checkdo.CreateAList.exListAdapter;
import static dev.majed.checkdo.CreateAList.tadp;

public class MainData {


    public static ArrayList<MyList> allArrayList ;
    public static ArrayList<ArrayList<SingleEntry>> callendarList;
    public static ArrayList<String> callendarDatesList;
    public static ArrayList<ArrayList<SingleEntry>> checkedList;
    public static Adapter adapter;

    public static Adapter getAdapter() {

        return adapter;
    }

    static Context ctx;

    public MainData(Context ctx) {
        this.ctx = ctx;
        Paper.init(ctx);
        allArrayList = new ArrayList<>();
        checkedList = new ArrayList<>();
        fetch();
        adapter = new Adapter(allArrayList);
    }

    public static void fetch() {

        Paper.init(ctx);
        allArrayList = Paper.book().read("AllList");
        if(allArrayList==null){
            allArrayList = new ArrayList<>();
        }
        checkedList = Paper.book().read("CheckedList");
    }

    public static void save(){
        Log.e("List Saved",String.valueOf(allArrayList.size()));
        Paper.book().write("AllList", allArrayList);
        fetch();
       // adapter.notifyDataSetChanged();
       // if(tadp!=null){tadp.notifyDataSetChanged();}
      //  if(exListAdapter!=null){exListAdapter.notifyDataSetChanged();}
    }

    public static void fetchCalenderList(){
        callendarList = new ArrayList<>();
        callendarDatesList = new ArrayList<>();
        fetch();
       ArrayList<SingleEntry> singleList = new ArrayList<>();
        for(int i=0;i<allArrayList.size();i++){
            for(int j=0;j<allArrayList.get(i).getItemList().size();j++)
            { singleList.add(allArrayList.get(i).getItemList().get(j));
        }
        }
for(int i=0;i<singleList.size();i++){
    Log.e("unsorted",singleList.get(i).getTaskName());
}
        Collections.sort(singleList, new CustomComparator());
        for(int i=0;i<singleList.size();i++){
            Log.e("sorted",singleList.get(i).getTaskName());
        }
        for(int i=0;i<singleList.size();i++){
                    String dateStr = singleList.get(i).getTaskDay();
                    if(!callendarDatesList.contains(dateStr)){
                        callendarDatesList.add(dateStr);
                        ArrayList<SingleEntry> emptyList = new ArrayList<>();
                        callendarList.add(emptyList);
                    }
                        int index = callendarDatesList.indexOf(dateStr);
                    callendarList.get(index).add(singleList.get(i));
            }
        }


    public static ArrayList<String> getCallendarDatesList() {
        return callendarDatesList;
    }

    public static void notifyDataChanged(){
        exListAdapter.notifyDataSetChanged();
        tadp.notifyDataSetChanged();
        adapter.notifyDataSetChanged();

    }

    public static void setTaskChecked(int index,Long id,boolean isChecked) {
        fetch();
        for(int i=0;i<allArrayList.get(index).getItemList().size();i++){
                    if(allArrayList.get(index).getItemList().get(i).getTaskId().equals(id)){
                allArrayList.get(index).getItemList().get(i).setDone(isChecked);
             }
        }
        save();
        notifyDataChanged();
    }

    public static void deleteTask(int index,Long id) {
        for(int i=0;i<allArrayList.get(index).getItemList().size();i++){
            if(allArrayList.get(index).getItemList().get(i).getTaskId().equals(id)){
                Log.e("removedName", allArrayList.get(index).getItemList().get(i).getTaskName());
               allArrayList.get(index).getItemList().remove(i);
            }
        }
        save();
        notifyDataChanged();

    }

    public static void removeAllChecked(int index) {
        for(int i=0;i<allArrayList.get(index).getItemList().size();i++){
            if(allArrayList.get(index).getItemList().get(i).getDone()){
                allArrayList.get(index).getItemList().remove(i);
            }
        }
        save();
        notifyDataChanged();

    }
    public static class CustomComparator implements Comparator<SingleEntry>{
        @Override
        public int compare(SingleEntry singleEntry, SingleEntry t1) {
            return singleEntry.getTaskTime().compareTo(t1.getTaskTime());
        }
    }
}
