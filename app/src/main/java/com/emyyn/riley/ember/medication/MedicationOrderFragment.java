package com.emyyn.riley.ember.medication;

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

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.FakeMedicationOrders;
import com.emyyn.riley.ember.data.EmberContract;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by Riley on 5/6/2016.
 */
public class MedicationOrderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int MEDICATION_ORDER_LOADER = 0;
    private final String TAG = this.getClass().getSimpleName();
    private static final String[] MEDICATION_ORDER_COLUMNS = Utility.getDashboardColumns();

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



    private MedicationOrderAdpater mMedicationOrderAdapter;

    public MedicationOrderFragment() {

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


        mMedicationOrderAdapter = new MedicationOrderAdpater(getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.content_main, parent, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(mMedicationOrderAdapter);
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

        Uri medicationOrderForPatientUri = EmberContract.MedicationOrderEntry.buildFamilyMedicationOrderForPatientId(patientId);
        Log.i("Medication OF Uri", medicationOrderForPatientUri.toString());
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

    public static MedicationOrderFragment newInstance(int position) {

        MedicationOrderFragment medicationFragment = new MedicationOrderFragment();
        Bundle args = new Bundle();
        args.putInt("Args",position);
        medicationFragment.setArguments(args);
        return medicationFragment;
    }
}
