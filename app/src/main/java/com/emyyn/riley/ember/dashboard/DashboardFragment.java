package com.emyyn.riley.ember.dashboard;

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
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.medication.MedicationDetails;


public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int DASHBOARD_LOADER = 1;
    private final String TAG = this.getClass().getSimpleName();
    private static final String[] DASHBOARD_COLUMNS = Utility.getDashboardColumns();

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


    private PatientRelationsAdapter adpater;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView;
        adpater = new PatientRelationsAdapter(getActivity(), null, 0);
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
        getLoaderManager().initLoader(DASHBOARD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String parentId = getActivity().getString(R.string.pref_patient_default);

        Uri uri = EmberContract.RelationEntry.buildFamilyUri(parentId);
        return new CursorLoader(getActivity(),
                uri,
                DASHBOARD_COLUMNS,
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
