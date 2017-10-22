package dev.majed.checkdo;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SuperList {
    ArrayList<MyList> arrayList = new ArrayList<>();
   ArrayList<String> stringArrayList = new ArrayList<>();


            Context ctx;

    public SuperList(Context ctx){this.ctx=ctx;
        retrive(ctx);
    }

    public void Add(MyList myList){
        Log.e("Add",String.valueOf(myList));
        retrive(ctx);
        arrayList.add(myList);
        stringArrayList.add(myList.getItemName());
        Save(ctx);
    }

    public void Save(Context ctx){

         HashMap<String, MyList> userHashMap=new HashMap<>();
        for(int i=0;i<stringArrayList.size();i++)
        {
            userHashMap.put(stringArrayList.get(i),arrayList.get(i));
        }
        storeUserMap(userHashMap,ctx);
    }

    public void AddChildListElement(int position,SingleEntry text){

        arrayList.get(position).getItemList().add(text);
        Save(ctx);
    }

    public void RemoveChildListElement(int position,Long id){
        for (int i=0;i<arrayList.get(position).getItemList().size();i++) {

             if (arrayList.get(position).getItemList().get(i).getTaskId().equals(id)) {
                arrayList.get(position).getItemList().remove(i);
            }

        }Save(ctx);
    }

    public void deleteList(int position){
        arrayList.remove(position);
        stringArrayList.remove(position);
        Save(ctx);
    }



    public void retrive(Context ctx){
       HashMap<String, MyList> hashMap= readUsersMap(ctx);
       arrayList.clear();
        ArrayList<SingleEntry>All= new ArrayList<>();

        for(int i=0;i<hashMap.size();i++){
       arrayList.add(hashMap.get(stringArrayList.get(i)));
        }

    }
    public HashMap<String, MyList> readUsersMap(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(NotesActivity.email, MODE_PRIVATE);
        String str=preferences.getString("key","");
        stringArrayList.clear();
         if(str!=""){
            List<String> list = Arrays.asList(str.split("\\s*,\\s*"));

            for(int i=0;i<list.size();i++){
                stringArrayList.add(i,list.get(i));
            }}
        try {
            String userMapRawString = preferences.getString("usersMap", "");
            if (userMapRawString.equals("")) {
                return new HashMap<>();
            }

            byte[] rawUserMap = Base64.decode(userMapRawString, Base64.DEFAULT);

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(rawUserMap);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            return (HashMap<String, MyList>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e("CHECKDO", e.getMessage());
            return null;
        }

    }

    public  void storeUserMap(HashMap<String, MyList> userHashMap, Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(NotesActivity.email, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String a ="";
        for(int i=0;i<stringArrayList.size();i++){
            a=a+stringArrayList.get(i)+",";
        }
        a=a.substring(0,a.length());
        editor.putString("key",a);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(userHashMap);
            String userMapRaw = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            editor.putString("usersMap", userMapRaw);
            editor.apply();
            Log.i("dee", "User map raw: " + userMapRaw);
        } catch (IOException e) {
            Log.e("CHECKDO", e.getMessage());
        }
    }
}
