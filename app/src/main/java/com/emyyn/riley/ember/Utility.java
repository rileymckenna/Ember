package com.emyyn.riley.ember;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Riley on 5/6/2016.
 */
public class Utility {


    public static String getPerferredPatient(FragmentActivity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(activity.getString(R.string.pref_patient_key), activity.getString(R.string.pref_patient_default));
    }
}
