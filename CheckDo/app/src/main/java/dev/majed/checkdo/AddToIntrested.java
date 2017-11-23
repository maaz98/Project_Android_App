package dev.majed.checkdo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class AddToIntrested {

    public static ArrayList<String> IntrestedEvent;
    public static ArrayList<String> NotificationId;

    Context ctx;
     public AddToIntrested(Context ctx) {
        this.ctx=ctx;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(String EventId, Date dateOfEvent, String Name){
        //Log.e("EventID",EventId);
        getSavedData(ctx);

        SharedPreferences notificationSp = ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2= notificationSp.edit();
        PingMe pm = new PingMe();
        int  idInt =  pm.setNotification("Event Reminder","Its time to "+Name, dateOfEvent,ctx);
        editor2.putInt(EventId,idInt);
        editor2.apply();

        IntrestedEvent.add(EventId);
        NotificationId.add(String.valueOf(idInt));

        String s = getStringFromList(IntrestedEvent);
        String aw = getStringFromList(NotificationId);


        SharedPreferences p = ctx.getSharedPreferences("Intrested", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= p.edit();
        editor.putString("intrested",s);
        editor.apply();

        SharedPreferences notificationSp2 = ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor21= notificationSp2.edit();
        editor.putString("notify",aw);
        editor21.apply();

        getSavedData(ctx);

    }
    public void remove(String EventId){
        getSavedData(ctx);

        IntrestedEvent.remove(EventId);
        NotificationId.remove(EventId);

        SharedPreferences a =ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        int id= a.getInt(EventId,0);

        PingMe pm = new PingMe();
        pm.cancel(ctx,id);

        String s = getStringFromList(IntrestedEvent);
        String aw = getStringFromList(NotificationId);


        SharedPreferences p = ctx.getSharedPreferences("Intrested", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= p.edit();
        editor.putString("intrested",s);
        editor.apply();

        SharedPreferences notificationSp = ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2= notificationSp.edit();
        editor.putString("notify",aw);
        editor2.apply();
        getSavedData(ctx);

    }
    private void getSavedData(Context ctx) {
        SharedPreferences p = ctx.getSharedPreferences("Intrested", Context.MODE_PRIVATE);
        String h= p.getString("intrested","");
        IntrestedEvent= new ArrayList(Arrays.asList(h.split("\\s*,\\s*")));
        SharedPreferences a = ctx.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        String ad= a.getString("notify","");
        NotificationId= new ArrayList(Arrays.asList(ad.split("\\s*,\\s*")));
        Log.e("FetchedListForNoti", String.valueOf(IntrestedEvent));
    }

    public boolean isEventAlreadyPresent(String EventID){
        if(IntrestedEvent==null){
            getSavedData(ctx);
        }
        if(IntrestedEvent.contains(EventID)){return true;}
        return false;
    }
    private String getStringFromList(ArrayList<String> arrayList){

        String aw = "";
        for (String i: arrayList){
            aw = aw.concat(i+ ",");
        }

        return aw;
    }
}
