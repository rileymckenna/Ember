package com.emyyn.riley.ember.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;

import java.text.ParseException;

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
    private  String dose_value;
    private  String dose_code;
    private   String timing_period;
    private  String method;
    private  String timing_frequency;
    private String child_id;
    private String parent_id;
    private String start;
    private String last;
    private String status;
    private int freq = 0;
    private String next_dose;


    private void convertCursorRowToUXFormat(Cursor cursor) throws ParseException {
        //Log.i("Columns: " , Utility.queryColumns(Utility.getDashboardColumns()));
         patientName = cursor.getString(DashboardFragment.COLUMN_NAME_GIVEN);
         prescription = cursor.getString(DashboardFragment.COLUMN_PRODUCT);
         instructions_text = cursor.getString(DashboardFragment.COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
        child_id = cursor.getString(DashboardFragment.COLUMN_CHILD_ID);
        parent_id = cursor.getString(DashboardFragment.COLUMN_PARENT_ID);
        start = cursor.getString(DashboardFragment.COLUMN_VALID_START);
        last = cursor.getString(DashboardFragment.COLUMN_LAST_TAKEN);
        status = cursor.getString(DashboardFragment.COLUMN_STATUS);
        running_total= cursor.getString(DashboardFragment.COLUMN_RUNNING_TOTAL);

        instructions_text = instructions_text.substring(5,6);
       // Log.i("instructions text: " , String.valueOf(Integer.decode(instructions_text)));


        try {
            if (Integer.decode(instructions_text) > 0) {
                freq = Integer.decode(instructions_text);
            }
            if (last == null) {
                next_dose = Utility.getNextDose(start);
            } else {
                next_dose = Utility.getNextDose(last);
            }
        } catch (Exception e){
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
        TextView dashboard_name = (TextView) view.findViewById(R.id.dashboard_name);
        TextView details_dosage = (TextView) view.findViewById(R.id.details_dosage);
        TextView details_dose_tv = (TextView) view.findViewById(R.id.details_dose_tv);
        TextView details_refills_tv = (TextView) view.findViewById(R.id.details_refills_tv);
        TextView details_dose = (TextView) view.findViewById(R.id.details_dose);

        //details_dosage.setText("Take " + freq);
        details_dose_tv.setText(next_dose);
        details_refills_tv.setText(running_total);
        details_dose.setText(getStatus((status) ));
        details_dosage.setText(prescription );
        dashboard_name.setText(patientName );
    }
    public String getStatus(String b){
        if (b.contains("true")) {return "Active";}
        else if (b.contains("false")) {return "Suspended";}
        return b;
    }

}
