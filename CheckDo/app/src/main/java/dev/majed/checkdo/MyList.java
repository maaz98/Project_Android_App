package dev.majed.checkdo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyList implements Serializable {
    int numberOfItems;
    ArrayList<SingleEntry> itemList;

    String itemName;

    public MyList( ArrayList<SingleEntry> itemList, String itemName) {
        this.numberOfItems = itemList.size();
        this.itemList = itemList;
        this.itemName = itemName;

    }

    public int getNumberOfItems() {
        return itemList.size();
    }



    public List<SingleEntry> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<SingleEntry> itemList) {
        this.itemList = itemList;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
