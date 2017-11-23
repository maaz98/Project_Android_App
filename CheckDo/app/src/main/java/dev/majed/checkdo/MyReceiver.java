package dev.majed.checkdo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;




public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title =intent.getStringExtra("title");
        String desc= intent.getStringExtra("desc");
        int idInt  =intent.getIntExtra("id",0);

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        intent1.putExtra("title",title);
        intent1.putExtra("desc",desc);
        intent1.putExtra("id",idInt);
        context.startService(intent1);
    }
}
