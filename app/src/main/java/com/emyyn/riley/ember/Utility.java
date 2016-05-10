package com.emyyn.riley.ember;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.emyyn.riley.ember.data.EmberContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;

/**
 * Created by Riley on 5/6/2016.
 */
public class Utility {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getPerferredPatient(FragmentActivity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(activity.getString(R.string.pref_patient_key), activity.getString(R.string.pref_patient_default));
    }

    public DateDt formatDate(String d) throws ParseException {
        //1985-08-01T00:00:00Z
        Date dt = format.parse(d);
        return new DateDt(dt);
    }

    public DateTimeDt formatDateTime(String d) throws ParseException {
        //1985-08-01T00:00:00Z
        Date dt = format.parse(d);
        return new DateTimeDt(dt);
    }

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

    public static String queryColumns() {
        String temp = "";
        int last = MEDICATION_ORDER_COLUMNS.length;
        List<String> l = new ArrayList<>(last);
        l.add(MEDICATION_ORDER_COLUMNS[0]);
        for (int i = 1; i < (last); i++) {
            l.add(", " + MEDICATION_ORDER_COLUMNS[i]);
        }
        for (String s : l) {
            temp += s;
        }

        return temp;
    }
}
