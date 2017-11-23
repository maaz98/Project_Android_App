package dev.majed.checkdo;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import static dev.majed.checkdo.LoginActivity.ctxx;


public class MyList implements Serializable {
    int numberOfItems;
    ArrayList<SingleEntry> itemList;
    String listName;
    long ListID;
    Context context;

    public MyList(ArrayList<SingleEntry> itemList, String listName) {
        this.numberOfItems = itemList.size();
        this.itemList = itemList;
        this.listName = listName;
        this.context = context;
        Calendar calendar = Calendar.getInstance();
        ListID = calendar.getTime().getTime()/1000;
        AddToList(ListID);
    }

    private void AddToList(long listID) {
        Hawk.init(ctxx).build();
        ArrayList<Long> ParentIDList = Hawk.get("ParentIDList",new ArrayList<Long>());
        ParentIDList.add(listID);
        Hawk.put("ParentIDList", ParentIDList);
    }

    public long getListID() {
        return ListID;
    }

    public void setListID(long listID) {
        ListID = listID;
    }

    public int getNumberOfItems() {
        return itemList.size();
    }



    public ArrayList<SingleEntry> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<SingleEntry> itemList) {
        this.itemList = itemList;
    }

    public String getItemName() {
        return listName;
    }

    public void setItemName(String itemName) {
        this.listName = itemName;
    }
}
