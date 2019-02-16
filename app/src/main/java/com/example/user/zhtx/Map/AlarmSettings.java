package com.example.user.zhtx.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class AlarmSettings {

    private static AlarmSettings alarmSettings;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    public static AlarmSettings newInstance() {
        if (alarmSettings == null) {
            alarmSettings = new AlarmSettings();
        }
        return alarmSettings;
    }

    public void startLocationService(Context context, int seconds,
                                     Class<?> clas, String ALARM_ACTION) {

        alarmManager = (AlarmManager)context
                .getSystemService(Context.ALARM_SERVICE);

        long triggerAtTime = SystemClock.elapsedRealtime();

        Intent intent = new Intent(context, clas);
        intent.setAction(ALARM_ACTION);

        alarmIntent = PendingIntent
                .getService(context, 0, intent, 0);

        Log.i("查看：", "123");

        alarmManager.set(AlarmManager.RTC, seconds*1000, alarmIntent);
    }

    public void stopLocationService() {
        alarmManager.cancel(alarmIntent);
        Log.e("123", "456");
    }
}
