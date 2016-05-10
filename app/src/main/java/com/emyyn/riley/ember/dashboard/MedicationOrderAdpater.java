package com.emyyn.riley.ember.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emyyn.riley.ember.R;

/**
 * Created by Riley on 5/6/2016.
 */
public class MedicationOrderAdpater extends CursorAdapter {


    public MedicationOrderAdpater(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public String getPatientName() {
        return patientName;
    }
    private String running_total;
    private String patientName;
    private String prescription;
    private String instructions_text;
    private  String dose_value;
    private  String dose_code;
    private   String timing_period;
    private  String method;
    private  String timing_frequency;

    private void convertCursorRowToUXFormat(Cursor cursor) {

         patientName = cursor.getString(MedicationOrderFragment.COLUMN_NAME_GIVEN);
         prescription = cursor.getString(MedicationOrderFragment.COLUMN_PRODUCT);
         instructions_text = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
         dose_value = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE);
         dose_code = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE);
         timing_period = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD);
         method = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_METHOD);
         timing_frequency = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY);
        running_total= cursor.getString(MedicationOrderFragment.COLUMN_RUNNING_TOTAL);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        convertCursorRowToUXFormat(cursor);
        Log.i("Adpater", patientName);
        TextView dashboard_name = (TextView) view.findViewById(R.id.dashboard_name);
        TextView details_dosage = (TextView) view.findViewById(R.id.details_dosage);
        TextView details_dose_tv = (TextView) view.findViewById(R.id.details_dose_tv);
        TextView details_refills_tv = (TextView) view.findViewById(R.id.details_refills_tv);

        details_dosage.setText(instructions_text);
        details_dose_tv.setText(timing_frequency);
        details_refills_tv.setText(running_total);
        dashboard_name.setText(prescription);
    }
}
