package com.emyyn.riley.ember;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Riley on 5/6/2016.
 */
public class MedicationOrderAdpater extends CursorAdapter {
    public MedicationOrderAdpater(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    private String convertCursorRowToUXFormat(Cursor cursor) {

        String patientName = cursor.getString(MedicationOrderFragment.COLUMN_PATIENT_KEY);
        Log.i("Name", patientName);
        return patientName;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_medication, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }

}
