package com.emyyn.riley.ember.Alerts;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.dashboard.DashboardFragment;

import java.text.ParseException;

/**
 * Created by riley on 5/17/2016.
 */
public class AlertAdapter extends CursorAdapter{
    public AlertAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    final static int _id=0;
    final static int medication_order_id=1;
    final static int medication_id=2;
    final static int patient_id=3;
    final static int prescriber_id=4;
    final static int COLUMN_DISPENSE_SUPPLY_VALUE=5;
    final static int date_written=6;
    final static int COLUMN_DISPENSE_SUPPLY_UNIT=7;
    final static int COLUMN_DS_CODE=8;
    final static int COLUMN_DISPENSE_QUANTITY=9;
    final static int COLUMN_VALID_START=10;
    final static int COLUMN_VALID_END=11;
    final static int COLUMN_DOSAGE_INSTRUCTIONS_TEXT=12;
    final static int di_asneeded=13;
    final static int d_route=14;
    final static int di_method=15;
    final static int COLUMN_DOSAGE_INSTRUCTIONS_FREQUENCY=16;
    final static int di_period=17;
    final static int di_period_units=18;
    final static int di_start=19;
    final static int di_end=20;
    final static int di_dose_value=21;
    final static int di_dose_code=22;
    final static int COLUMN_LAST_UPDATED_AT=23;
    final static int reason_given=24;
    final static int COLUMN_STATUS=25;
    final static int COLUMN_LAST_TAKEN=26;
    final static int COLUMN_RUNNING_TOTAL=27;
    final static int COLUMN_PRODUCT=28;
    final static int COLUMN_NAME_GIVEN=29;
    final static int COLUMN_NAME_FAMILY=30;



    private String running_total;
    private String patientName;
    private String prescription;
    private String instructions_text;
    private String dose_value;
    private String dose_code;
    private String timing_period;
    private String method;
    private String timing_frequency;
    private String child_id;
    private String parent_id;
    private String start;
    private String last;
    private String ds_value;   //total number of pills dispensed
    private String statuss;
    private int freq = 1;
    private String next_dose;


    private void convertCursorRowToUXFormat(Cursor cursor) throws ParseException {

        //Log.i("Columns: " , Utility.queryColumns(Utility.getDashboardColumns()));
        patientName = cursor.getString(COLUMN_NAME_GIVEN);
        prescription = cursor.getString(COLUMN_PRODUCT);
        instructions_text = cursor.getString(COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
        start = cursor.getString(COLUMN_VALID_START);
        last = cursor.getString(COLUMN_LAST_TAKEN);
        statuss = cursor.getString(COLUMN_STATUS);
        running_total = cursor.getString(COLUMN_RUNNING_TOTAL);
        ds_value = cursor.getString(COLUMN_DISPENSE_SUPPLY_VALUE);

        instructions_text = instructions_text.substring(5, 6);
        // Log.i("instructions text: " , String.valueOf(Integer.decode(instructions_text)));


        try {
            if (Integer.decode(instructions_text) > 0) {
                freq = Integer.decode(instructions_text);
            }
            if (last == null) {
                int s = (Integer.parseInt(Utility.getNextDose(start)));
                if (s > 0) {
                    next_dose = String.valueOf(s);
                } else if (s < 0)
                    next_dose = "Past Due " + Math.abs(s) + " hours";
                else {
                    int i = Utility.getNextDoseMin(start);
                    next_dose = String.valueOf(i) + " minutes";
                }
            } else {
                int s = Math.abs(Integer.parseInt(Utility.getNextDose(last)));
                next_dose = String.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.alert_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            convertCursorRowToUXFormat(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.i("Adpater", patientName);
        TextView medication, status, instructions;

        medication = (TextView) view.findViewById(R.id.alert_name);
        status = (TextView) view.findViewById(R.id.alert_status);
        instructions = (TextView) view.findViewById(R.id.alert_text);

        ProgressBar details_refill_progress = (ProgressBar) view.findViewById(R.id.alerts_progress);
        details_refill_progress.setMax(Integer.parseInt(ds_value));

        //details_dosage.setText("Take " + freq);
        status.setText(Utility.getStatus((statuss)));
        instructions.setText("take " + instructions_text +getDoseText() + prescription + ".");
        medication.setText(patientName);
        details_refill_progress.setProgress(Integer.parseInt(running_total));
    }

    private String getDoseText() {
        if (Integer.parseInt(instructions_text) > 1) {
            return " doses of ";
        } else {
            return " dose of ";
        }
    }
}
