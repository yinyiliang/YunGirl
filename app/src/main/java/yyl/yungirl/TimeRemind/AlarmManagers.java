package yyl.yungirl.TimeRemind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.TimeZone;

import yyl.yungirl.ui.activity.MainActivity;

/**
 * Created by yinyiliang on 2016/7/5 0005.
 */
public class AlarmManagers {

    public static void register(Context context) {
        Calendar calendar = Calendar.getInstance();
        // 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        Logger.e(String.valueOf(calendar.getTime()));

        Intent intent = new Intent("me.drakeet.meizhi.alarm");
        intent.setClass(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                520,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);

    }
}
