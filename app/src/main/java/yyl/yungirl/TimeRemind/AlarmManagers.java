package yyl.yungirl.TimeRemind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import yyl.yungirl.ui.activity.MainActivity;

/**
 * Created by yinyiliang on 2016/7/5 0005.
 */
public class AlarmManagers {

    public static void register(Context context) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY,14);
        c.set(Calendar.MINUTE,25);
        c.set(Calendar.SECOND,10);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

    }
}
