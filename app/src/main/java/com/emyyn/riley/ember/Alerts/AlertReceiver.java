package com.emyyn.riley.ember.Alerts;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import layout.Notifications;

/**
 * Created by riley on 5/19/2016.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notifications.notify(context, "Medication Reminder", 0);
    }
}
