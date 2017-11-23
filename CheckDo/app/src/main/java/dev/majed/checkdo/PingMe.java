package dev.majed.checkdo;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;



public class PingMe {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int setNotification(String title, String desc, Date date, Context ctx){
        Long sub = new Long("1505070904958");
        long id= (System.currentTimeMillis()-sub)/10;
        int idInt= Integer.parseInt(String.valueOf(id))/10;
        Log.e("id", String.valueOf(idInt));
        Intent alarmIntent = new Intent(ctx, MyReceiver.class);
        alarmIntent.putExtra("title",title);
        alarmIntent.putExtra("desc",desc);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,idInt , alarmIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime() , pendingIntent);

        return idInt;
    }
    public void cancel(Context ctx, int id){

        Log.e("CancelId", String.valueOf(id));
        NotificationManager notificationManager= (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.cancel(id);
        Intent alarmIntent = new Intent(ctx, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx.getApplicationContext(),
                id, alarmIntent, 0);
        AlarmManager alarmManager=(AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
