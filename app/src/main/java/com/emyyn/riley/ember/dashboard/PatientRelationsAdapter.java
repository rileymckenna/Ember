package com.emyyn.riley.ember.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Riley on 5/6/2016.
 */
public class PatientRelationsAdapter extends CursorAdapter {


    public PatientRelationsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public String getPatientName() {
        return patientName;
    }

    private String running_total;
    private String patientName;
    private String prescription;
    private String instructions_text;
    private String dose_value;
    private String dose_code;
    private String timing_period;
    private String method;
    private String timing_frequency;
    private String period_unit;
    private String child_id;
    private String parent_id;
    private String start;
    private String last;
    private String ds_value;   //total number of pills dispensed
    private String status;
    private int freq = 1;
    private String next_dose;

    public static ArrayList<String> getChildrenList() {
        return childrenList;
    }

    private static ArrayList<String> childrenList = new ArrayList<>();


    private void convertCursorRowToUXFormat(Cursor cursor) throws ParseException {

        //Log.i("Columns: " , Utility.queryColumns(Utility.getDashboardColumns()));
        patientName = cursor.getString(DashboardFragment.COLUMN_NAME_GIVEN);
        prescription = cursor.getString(DashboardFragment.COLUMN_PRODUCT);
        instructions_text = cursor.getString(DashboardFragment.COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
        start = cursor.getString(DashboardFragment.COLUMN_VALID_START);
        last = cursor.getString(DashboardFragment.COLUMN_LAST_TAKEN);
        status = cursor.getString(DashboardFragment.COLUMN_STATUS);
        running_total = cursor.getString(DashboardFragment.COLUMN_RUNNING_TOTAL);
        ds_value = cursor.getString(DashboardFragment.COLUMN_DISPENSE_QUANTITY);
        timing_period = cursor.getString(DashboardFragment.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD);
        period_unit = cursor.getString(DashboardFragment.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS);

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
                    next_dose = "Past Due " + Math.abs(s);
                else {
                    int i = Utility.getNextDoseMin(start);
                    next_dose = String.valueOf(i) + " minutes";
                }
            } else {
                int s = Math.abs(Integer.parseInt(Utility.getNextDoseDate(last, Integer.parseInt(timing_period))));
                next_dose = String.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false);
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
        TextView dashboard_name = (TextView) view.findViewById(R.id.alert_name);
        TextView details_status = (TextView) view.findViewById(R.id.alert_status);
        TextView details_next_dose = (TextView) view.findViewById(R.id.details_next_dose);
        TextView remaining = (TextView) view.findViewById(R.id.remaining);
        TextView details_text = (TextView) view.findViewById(R.id.dashboard_text);
        ProgressBar details_refill_progress = (ProgressBar) view.findViewById(R.id.dashboard_progress);
        details_refill_progress.setMax(Integer.parseInt(ds_value));

        //details_dosage.setText("Take " + freq);
        details_next_dose.setText(next_dose);
        remaining.setText(running_total);
        details_status.setText(Utility.getStatus((status)));
        details_text.setText("Take " + instructions_text + getDoseText() + prescription);
        dashboard_name.setText(patientName);
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
