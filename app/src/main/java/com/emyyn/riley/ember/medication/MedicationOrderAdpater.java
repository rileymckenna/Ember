package com.emyyn.riley.ember.medication;

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

/**
 * Created by Riley on 5/6/2016.
 */
public class MedicationOrderAdpater extends CursorAdapter {




    public MedicationOrderAdpater(Context context, Cursor c, int flags) {
        super(context, c, flags);
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
    private String start;
    private String last;
    private String ds_value;   //total number of pills dispensed
    private String status;
    private int freq = 1;
    private String next_dose;
    private String provider_name;
    private String reason;

    TextView details_name;
    TextView details_status;
    TextView extra;
    TextView next_doses;
    TextView last_dose;
    TextView prescriptions;
    TextView reasons;
    TextView prescriber;
    TextView directions;
    TextView remaining_prescription;
    ProgressBar details_refill_progress;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.content_medication_details, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        running_total = cursor.getString(MedicationOrderFragment.COLUMN_RUNNING_TOTAL);
        patientName = cursor.getString(MedicationOrderFragment.COLUMN_NAME_GIVEN);
        prescription = cursor.getString(MedicationOrderFragment.COLUMN_PRODUCT);
        dose_code = cursor.getString(MedicationOrderFragment.COLUMN_DS_CODE);
        dose_value = cursor.getString(MedicationOrderFragment.COLUMN_DS_VALUE);
        last = cursor.getString(MedicationOrderFragment.COLUMN_LAST_TAKEN);
        reason = cursor.getString(MedicationOrderFragment.COLUMN_REASON);
        status = cursor.getString(MedicationOrderFragment.COLUMN_STATUS);
        instructions_text = cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
        provider_name = cursor.getString(MedicationOrderFragment.COLUMN_PROVIDER_NAME);
        ds_value = cursor.getString(MedicationOrderFragment.COLUMN_DISPENSE_SUPPLY_VALUE);

        details_refill_progress.setMax(Integer.parseInt(ds_value));

        details_name = (TextView) view.findViewById(R.id.details_name);
        details_status = (TextView) view.findViewById(R.id.details_status);
        //extra = (TextView) view.findViewById(R.id.details_extra) ;
        next_doses = (TextView) view.findViewById(R.id.next_dose);
        last_dose = (TextView) view.findViewById(R.id.last_dose);
        prescriptions = (TextView) view.findViewById(R.id.alert_status);
        reasons = (TextView) view.findViewById(R.id.reason);
        prescriber = (TextView) view.findViewById(R.id.prescriber);
        directions = (TextView) view.findViewById(R.id.directions);
        remaining_prescription = (TextView) view.findViewById(R.id.remaining_prescription);

        details_refill_progress = (ProgressBar) view.findViewById(R.id.alerts_progress);

        details_name.setText(prescription +  " (" + dose_value + " " + dose_code +")");
        //extra.setText( "(" + dose_value + " " + dose_code +")");
        details_status.setText(Utility.getStatus(status));
        next_doses.setText(next_dose);
        last_dose.setText(last);
        prescriptions.setText(prescription);
        reasons.setText(reason);
        prescriber.setText(provider_name);
        directions.setText(instructions_text);
        remaining_prescription.setText(running_total);

        details_refill_progress.setProgress(Integer.parseInt(running_total));

    }
}
