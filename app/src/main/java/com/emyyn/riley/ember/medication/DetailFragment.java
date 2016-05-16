/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emyyn.riley.ember.medication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private static final int DETAIL_LOADER = 4;

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    static final int COLUMN_MED_KEY = 0;
    static final int COLUMN_MEDICATION_ORDER_ID = 1;
    static final int COLUMN_PATIENT_KEY = 2;
    static final int COLUMN_DISPENSE_SUPPLY_VALUE = 3;
    static final int COLUMN_DISPENSE_SUPPLY_UNIT = 4;
    static final int COLUMN_DISPENSE_QUANTITY = 5;
    static final int COLUMN_VALID_START = 5;
    static final int COLUMN_VALID_END = 6;
    static final int COLUMN_LAST_UPDATED_AT = 7;
    static final int COLUMN_STATUS = 8;
    static final int COLUMN_LAST_TAKEN = 9;
    static final int COLUMN_RUNNING_TOTAL = 10;
    static final int COLUMN_PRODUCT = 11;
    static final int COLUMN_NAME_GIVEN = 12;
    static final int COLUMN_NAME_FAMILY = 13;
    static final int COLUMN_DS_VALUE = 14;
    static final int COLUMN_DS_CODE = 15;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TEXT = 16;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_FREQUENCY = 17;
    static final int COLUMN_PROVIDER_NAME = 18;
    static final int COLUMN_REASON = 19;


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

    public DetailFragment() {
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.content_medication_details, container, false);
         details_name = (TextView) rootView.findViewById(R.id.details_name);
         details_status = (TextView) rootView.findViewById(R.id.details_status);
         extra = (TextView) rootView.findViewById(R.id.details_extra) ;
         next_doses = (TextView) rootView.findViewById(R.id.next_dose);
         last_dose = (TextView) rootView.findViewById(R.id.last_dose);
         prescriptions = (TextView) rootView.findViewById(R.id.prescription);
         reasons = (TextView) rootView.findViewById(R.id.reason);
         prescriber = (TextView) rootView.findViewById(R.id.prescriber);
         directions = (TextView) rootView.findViewById(R.id.directions);
         remaining_prescription = (TextView) rootView.findViewById(R.id.remaining_prescription);

        details_refill_progress = (ProgressBar) rootView.findViewById(R.id.details_refill_progress);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
       inflater.inflate(R.menu.detail, menu);
//
//        // Retrieve the share menu item
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//
//        // Get the provider and hold onto it to set/change the share intent.
//       mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
//        if (mForecast != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"text");
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onLocationChanged( String s ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            String id = EmberContract.MedicationOrderEntry.getPatientId(uri);
            Uri updatedUri = EmberContract.MedicationOrderEntry.buildMedicationOrderUri(s);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                   Utility.getDashboardColumns(),
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            // Read weather condition ID from cursor
            running_total = cursor.getString(COLUMN_RUNNING_TOTAL);
            patientName = cursor.getString(COLUMN_NAME_GIVEN);
            prescription = cursor.getString(COLUMN_PRODUCT);
            dose_code = cursor.getString(COLUMN_DS_CODE);
            dose_value = cursor.getString(COLUMN_DS_VALUE);
            last = cursor.getString(COLUMN_LAST_TAKEN);
            reason = cursor.getString(COLUMN_REASON);
            status = cursor.getString(COLUMN_STATUS);
            instructions_text = cursor.getString(COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
            provider_name = cursor.getString(COLUMN_PROVIDER_NAME);
            ds_value = cursor.getString(COLUMN_DISPENSE_SUPPLY_VALUE);

            details_refill_progress.setMax(Integer.parseInt(ds_value));

//            try {
//                if (Integer.decode(instructions_text) > 0) {
//                    freq = Integer.decode(instructions_text);
//                }
//                if (last == null) {
//                    int s = (Integer.parseInt(Utility.getNextDose(start)));
//                    if (s > 0) {
//                        next_dose = String.valueOf(s);
//                    } else if (s < 0)
//                        next_dose = "Past Due " + Math.abs(s) + " hours";
//                    else {
//                        int i = Utility.getNextDoseMin(start);
//                        next_dose = String.valueOf(i) + " minutes";
//                    }
//                } else {
//                    int s = Math.abs(Integer.parseInt(Utility.getNextDose(last)));
//                    next_dose = String.valueOf(s);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            //details_dosage.setText("Take " + freq);
            details_name.setText(patientName);
            extra.setText( "(" + dose_value + " " + dose_code +")");
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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}