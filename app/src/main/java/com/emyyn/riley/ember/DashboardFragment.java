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

import com.emyyn.riley.ember.dashboard.FakeMedicationOrders;
import com.emyyn.riley.ember.dashboard.MedicationOrderAdpater;
import com.emyyn.riley.ember.data.EmberContract;

import org.json.JSONException;

import java.text.ParseException;


public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int MEDICATION_ORDER_LOADER = 0;
    private final String TAG = this.getClass().getSimpleName();
    private static final String[] MEDICATION_ORDER_COLUMNS = Utility.getMedicationOrderColumns();

    static final int COLUMN_MED_KEY = 1;
    static final int COLUMN_MEDICATION_ORDER_ID = 2;
    static final int COLUMN_PATIENT_KEY = 3;
    static final int COLUMN_PRESCRIBER_KEY = 4;
    static final int COLUMN_DISPENSE_SUPPLY_VALUE = 5;
    static final int COLUMN_DATE_WRITTEN = 6;
    static final int COLUMN_DISPENSE_SUPPLY_UNIT = 7;
    static final int COLUMN_DISPENSE_SUPPLY_CODE = 8;
    static final int COLUMN_DISPENSE_QUANTITY = 9;
    static final int COLUMN_VALID_START = 10;
    static final int COLUMN_VALID_END = 11;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TEXT = 12;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED = 13;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_ROUTE = 14;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_METHOD = 15;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY = 16;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD = 17;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS = 18;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START = 19;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END = 20;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE = 21;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE = 22;
    static final int COLUMN_LAST_UPDATED_AT = 23;
    static final int COLUMN_REASON_GIVEN = 24;
    static final int COLUMN_STATUS = 25;
    static final int COLUMN_LAST_TAKEN = 26;
    static final int COLUMN_RUNNING_TOTAL = 27;
    static final int COLUMN_PRODUCT = 28;
    static final int COLUMN_NAME_GIVEN = 29;
    static final int COLUMN_NAME_FAMILY = 30;


    private MedicationOrderAdpater adpater;

    public DashboardFragment() {

    }

    private void updateMedicationOrders() throws JSONException, ParseException {
        //FetchMedicationOrders medicationOrders = new FetchMedicationOrders(getActivity());
        FakeMedicationOrders orders = new FakeMedicationOrders(getActivity());
        //String patientId = Utility.getPerferredPatient(getActivity());
        //medicationOrders.execute(patientId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView;
        Log.i(TAG, "onCreate");
        try {
            updateMedicationOrders();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        adpater = new MedicationOrderAdpater(getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.content_main, parent, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adpater);
        // Log.i(TAG, "adapter Set");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {

                    Long medicationOrderId = cursor.getLong(COLUMN_MEDICATION_ORDER_ID);
                    Intent intent = new Intent(getActivity(), MedicationDetails.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderUri(medicationOrderId));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MEDICATION_ORDER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String patientId = getActivity().getString(R.string.pref_patient_default);

        Uri medicationOrderForPatientUri = EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(patientId);
        Log.i("Uri", medicationOrderForPatientUri.toString());
        return new CursorLoader(getActivity(),
                medicationOrderForPatientUri,
                MEDICATION_ORDER_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adpater.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adpater.swapCursor(null);
    }

    public static DashboardFragment newInstance(int position) {

        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt("Args",position);
        fragment.setArguments(args);
        return fragment;
    }
}
