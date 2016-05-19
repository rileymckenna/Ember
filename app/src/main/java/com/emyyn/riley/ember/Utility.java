package com.emyyn.riley.ember;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.emyyn.riley.ember.data.EmberContract;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;

/**
 * Created by Riley on 5/6/2016.
 */
public class Utility {

    public static String getNextDose(String start) {
        Calendar c = Calendar.getInstance();
        String next_dose = "none";
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatDate(start);
            d2 = c.getTime();
            DateTime dt1 = new DateTime(d1);
            DateTime dt2 = new DateTime(d2);
            next_dose = Hours.hoursBetween(dt2, dt1).getHours() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return next_dose;
    }

    public static String getTimeNow() throws ParseException {
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        return String.valueOf(d);
    }

    public static SimpleDateFormat getFormat() {
        return format;
    }

    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat format2 = new SimpleDateFormat("EEE MMMMMMMMM dd HH:mm:ss ZZZ yyyy");

    public static String getPerferredPatient(FragmentActivity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(activity.getString(R.string.pref_patient_key), activity.getString(R.string.pref_patient_default));
    }

    public static Date formatDate(String d) throws ParseException {
        //1985-08-01T00:00:00Z
        Date dt = format.parse(d);
        return dt;
    }

    public DateDt formatDateDt(String d) throws ParseException {
        //1985-08-01T00:00:00Z
        Date dt = format.parse(d);
        return new DateDt(dt);
    }

    public DateTimeDt formatDateTimeDt(String d) throws ParseException {
        //1985-08-01T00:00:00Z
        Date dt = format.parse(d);
        return new DateTimeDt(dt);
    }

    public static String[] getDashboardColumns() {
        return DASHBOARD_COLUMNS;
    }

    private static final String[] DASHBOARD_COLUMNS = {
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_MED_KEY,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_PATIENT_KEY,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT,
            EmberContract.MedicationOrderEntry.COLUMN_VALID_START,
            EmberContract.MedicationOrderEntry.COLUMN_VALID_END,
            EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT,
            EmberContract.MedicationOrderEntry.COLUMN_STATUS,
            EmberContract.MedicationOrderEntry.COLUMN_LAST_TAKEN,
            EmberContract.MedicationOrderEntry.COLUMN_RUNNING_TOTAL,
            EmberContract.MedicationEntry.TABLE_NAME + "." + EmberContract.MedicationEntry.COLUMN_PRODUCT,
            EmberContract.PatientEntry.TABLE_NAME + "." + EmberContract.PatientEntry.COLUMN_NAME_GIVEN,
            EmberContract.PatientEntry.TABLE_NAME + "." + EmberContract.PatientEntry.COLUMN_NAME_FAMILY,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TEXT,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY,
            EmberContract.ProviderEntry.TABLE_NAME + "." + EmberContract.ProviderEntry.COLUMN_NAME_GIVEN,
            EmberContract.MedicationOrderEntry.COLUMN_REASON_GIVEN,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS,
            EmberContract.MedicationOrderEntry.COLUMN_NEXT_DOSE,



            //Ids dont need to be counted
            EmberContract.MedicationEntry.TABLE_NAME + "." + EmberContract.MedicationEntry._ID,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry._ID,
            EmberContract.PatientEntry.TABLE_NAME + "." + EmberContract.PatientEntry._ID,
            EmberContract.ProviderEntry.TABLE_NAME + "." + EmberContract.ProviderEntry._ID,
            EmberContract.RelationEntry.TABLE_NAME + "." + EmberContract.RelationEntry.COLUMN_CHILD_ID,
            EmberContract.RelationEntry.TABLE_NAME + "." + EmberContract.RelationEntry.COLUMN_PATIENT_ID,

    };

    public static String[] getMedicationOrderColumns() {
        return MEDICATION_ORDER_COLUMNS;
    }

    private static final String[] MEDICATION_ORDER_COLUMNS = {
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry._ID,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_MED_KEY,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_PATIENT_KEY,
            EmberContract.MedicationOrderEntry.TABLE_NAME + "." + EmberContract.MedicationOrderEntry.COLUMN_PRESCRIBER_KEY,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE,
            EmberContract.MedicationOrderEntry.COLUMN_DATE_WRITTEN,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_CODE,
            EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY,
            EmberContract.MedicationOrderEntry.COLUMN_VALID_START,
            EmberContract.MedicationOrderEntry.COLUMN_VALID_END,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TEXT,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ROUTE,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_METHOD,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE,
            EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE,
            EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT,
            EmberContract.MedicationOrderEntry.COLUMN_REASON_GIVEN,
            EmberContract.MedicationOrderEntry.COLUMN_STATUS,
            EmberContract.MedicationOrderEntry.COLUMN_LAST_TAKEN,
            EmberContract.MedicationOrderEntry.COLUMN_RUNNING_TOTAL,
            EmberContract.MedicationEntry.TABLE_NAME + "." + EmberContract.MedicationEntry.COLUMN_PRODUCT,
            EmberContract.PatientEntry.TABLE_NAME + "." + EmberContract.PatientEntry.COLUMN_NAME_GIVEN,
            EmberContract.PatientEntry.TABLE_NAME + "." + EmberContract.PatientEntry.COLUMN_NAME_FAMILY

    };

    public static String queryColumns(String[] strings) {
        String temp = "";
        int last = strings.length;
        List<String> l = new ArrayList<>(last);
        l.add(strings[0]);
        for (int i = 1; i < (last); i++) {
            l.add(", " + strings[i]);
        }
        for (String s : l) {
            temp += s;
        }

        return temp;
    }

    public static int getNextDoseMin(String start) {
        Calendar c = Calendar.getInstance();
        int next_dose = 0;
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatDate(start);
            d2 = c.getTime();
            DateTime dt1 = new DateTime(d1);
            DateTime dt2 = new DateTime(d2);
            next_dose = Minutes.minutesBetween(dt2, dt1).getMinutes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return next_dose;
    }

    public static String getStatus(String b) {
        if (b.contains("true")) {
            return "active";
        } else if (b.contains("false")) {
            return "suspended";
        } else {
            return b+"";
        }
    }

    public static String getNextDose2(String start) {
        Calendar c = Calendar.getInstance();
        String next_dose = "none";
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatDate2(start);
            d2 = c.getTime();
            DateTime dt1 = new DateTime(d1);
            DateTime dt2 = new DateTime(d2);
            next_dose = Hours.hoursBetween(dt1, dt2).getHours() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return next_dose;
    }

    public static String getNextDoseDate(String last, int period) {
        Calendar c = Calendar.getInstance();
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatDate2(last);
            c.setTime(d1);
            c.add(Calendar.HOUR, period);
            d2 = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(d2);
    }

    public static Date formatDate2(String d) throws ParseException {
        Date dt = format2.parse(d);
        return dt;
    }

    public static String getSnoozeThisDose(String timeNow, int period) {
        Calendar c = Calendar.getInstance();
        String next_dose = "none";
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatDate2(timeNow);
            c.setTime(d1);
            c.add(Calendar.MINUTE, period);
            d2 = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(d2);
    }
}
