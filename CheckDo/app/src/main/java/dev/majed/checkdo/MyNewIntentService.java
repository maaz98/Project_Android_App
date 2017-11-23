package dev.majed.checkdo;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;



public class MyNewIntentService extends IntentService {


    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        Calendar calendar = Calendar.getInstance();
        String title =intent.getStringExtra("title");
        String desc= intent.getStringExtra("desc");
        int idInt  =intent.getIntExtra("id",0);
        Log.e("intent id", String.valueOf(idInt));
        builder.setContentTitle(title);
        builder.setContentText(desc);
        builder.setSmallIcon(R.drawable.bell);
        Intent notifyIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, idInt, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(idInt, notificationCompat);
    }
}
