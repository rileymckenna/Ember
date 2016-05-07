package com.emyyn.riley.ember;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emyyn.riley.ember.data.EmberContract;

import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.getColumns;

/**
 * Created by Riley on 5/6/2016.
 */
public class MedicationOrderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEDICATION_ORDER_LOADER = 0;
    private final String TAG = this.getClass().getSimpleName();
    private static final String[] MEDICATION_ORDER_COLUMNS = getColumns();

    static final int COLUMN_MED_KEY =  1;
    static final int COLUMN_PATIENT_KEY =  2;
    static final int COLUMN_PRESCRIBER_KEY =  3;
    static final int COLUMN_DISPENSE_SUPPLY_VALUE =  4;
    static final int COLUMN_DATE_WRITTEN =  5;
    static final int COLUMN_DISPENSE_SUPPLY_UNIT =  6;
    static final int COLUMN_DISPENSE_SUPPLY_CODE =  7;
    static final int COLUMN_DISPENSE_QUANTITY =  8;
    static final int COLUMN_VALID_START =  9;
    static final int COLUMN_VALID_END =  10;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TEXT =  11;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED =  12;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_ROUTE =  13;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_METHOD =  14;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY =  15;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD =  16;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS =  17;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START =  18;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END =  19;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE =  20;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE =  21;


    private MedicationOrderAdpater mMedicationOrderAdapter;

    public MedicationOrderFragment() {

    }

    private void updateMedicationOrders() {
        FetchMedicationOrders medicationOrders = new FetchMedicationOrders(getActivity());
        String patientId = Utility.getPerferredPatient(getActivity());
        medicationOrders.execute(patientId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView;
        Log.i(TAG, "onCreate");
        updateMedicationOrders();
        Log.i(TAG, "Post Update");
        mMedicationOrderAdapter = new MedicationOrderAdpater(getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.fragment_main, parent, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(mMedicationOrderAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {

                    Long medicationOrderId = cursor.getLong(0);
                    Intent intent = new Intent(getActivity(), MedicationDetails.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderUri(medicationOrderId));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(MEDICATION_ORDER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String patientId = getActivity().getString(R.string.pref_patient_default);

        Uri medicationOrderForPatientUri = EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(patientId);
        return new CursorLoader(getActivity(),
                medicationOrderForPatientUri,
                MEDICATION_ORDER_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMedicationOrderAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMedicationOrderAdapter.swapCursor(null);
    }
}
