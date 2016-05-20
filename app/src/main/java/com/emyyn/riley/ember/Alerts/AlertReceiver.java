package com.emyyn.riley.ember.Alerts;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.emyyn.riley.ember.MainActivity;

import layout.Notifications;

/**
 * Created by riley on 5/19/2016.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlertReciever", "Recieved");
        generateNotification(context, "Medication Reminder");

    }

    private void generateNotification(Context context, String msg) {
        //PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        Notifications.notify(context, msg, 0);
    }
}
