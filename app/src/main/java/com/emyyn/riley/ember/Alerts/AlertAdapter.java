package com.emyyn.riley.ember.Alerts;

import android.content.Context;
import android.database.Cursor;
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

    static final int COLUMN_MED_KEY = 0;
    static final int COLUMN_MEDICATION_ORDER_ID = 1;
    static final int COLUMN_PATIENT_KEY = 2;
    //number of days prescription is supposed to last
    static final int COLUMN_DISPENSE_SUPPLY_VALUE = 3;
    //D, hr, associated with the suppose to last
    static final int COLUMN_DISPENSE_SUPPLY_UNIT = 4;
    //dispense supply * di_frequency
    //static final int COLUMN_DISPENSE_QUANTITY = 5;
    //used to calculate the period to administer the drugs
    static final int COLUMN_VALID_START = 5;
    //expiration date
    static final int COLUMN_VALID_END = 6;
    //used to calculate when to async with the server
    static final int COLUMN_LAST_UPDATED_AT = 7;
    //determines wheter or not the drug is active on the child or not for alerting and reminder purposes only. the expiration will determine when the drug will fall off the list unless it is all used up
    static final int COLUMN_STATUS = 8;
    //Used in conjunction with start to determine when the next dose will be used
    static final int COLUMN_LAST_TAKEN = 9;
    //Used to calculate the number of days before you run out. Running total = running total - di_text.getSubstring(5,5).
    static final int COLUMN_RUNNING_TOTAL = 10;
    //name of the drugs
    static final int COLUMN_PRODUCT = 11;
    //patients name
    static final int COLUMN_NAME_GIVEN = 12;
    static final int COLUMN_NAME_FAMILY = 13;
    static final int COLUMN_CHILD_ID = 14;
    static final int COLUMN_PARENT_ID = 15;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TEXT = 16;

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
        child_id = cursor.getString(COLUMN_CHILD_ID);
        parent_id = cursor.getString(COLUMN_PARENT_ID);
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
        instructions.setText("take " + instructions_text + getDoseText() + prescription + ".");
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
