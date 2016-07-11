package yyl.yungirl.TimeRemind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import yyl.yungirl.R;
import yyl.yungirl.ui.activity.MainActivity;

/**
 * Created by yinyiliang on 2016/7/5 0005.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder  = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher));
        builder.setContentTitle("YunGirl");
        builder.setContentText("你定制的妹子已经准备好了，请签收~");

        notificationManager.notify(9999,builder.build());
    }
}
