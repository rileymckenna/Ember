package com.emyyn.riley.ember.dashboard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emyyn.riley.ember.MedicationDetails;
import com.emyyn.riley.ember.R;

/**
 * Created by Riley on 5/9/2016.
 */


public class PatientMedicationOrdersAdapter extends CursorRecyclerViewAdapter<PatientMedicationOrdersAdapter.ViewHolder> {
    private Context context;

    public PatientMedicationOrdersAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView dashboard_name;
        public  TextView details_dosage;
        public   TextView details_dose_tv;
        public   TextView details_refills_tv;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            dashboard_name = (TextView) view.findViewById(R.id.dashboard_name);
            details_dosage = (TextView) view.findViewById(R.id.details_dosage);
            details_dose_tv = (TextView) view.findViewById(R.id.details_dose_tv);
            details_refills_tv = (TextView) view.findViewById(R.id.details_refills_tv);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MedicationDetails.class);
            context.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder view, Cursor cursor) {

        view.details_dosage.setText(cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TEXT));
        view.details_dose_tv.setText(cursor.getString(MedicationOrderFragment.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD));
        view.details_refills_tv.setText(cursor.getString(MedicationOrderFragment.COLUMN_RUNNING_TOTAL));
        view.dashboard_name.setText(cursor.getString(MedicationOrderFragment.COLUMN_PRODUCT));
    }
}

