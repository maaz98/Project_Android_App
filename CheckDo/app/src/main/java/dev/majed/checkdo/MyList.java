package dev.majed.checkdo;

import java.io.Serializable;
import java.util.ArrayList;


public class MyList implements Serializable {
    int numberOfItems;
    ArrayList<SingleEntry> itemList;
    String listName;

    public MyList( ArrayList<SingleEntry> itemList, String listName) {
        this.numberOfItems = itemList.size();
        this.itemList = itemList;
        this.listName = listName;

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
