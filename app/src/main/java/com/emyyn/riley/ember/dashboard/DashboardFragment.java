package com.emyyn.riley.ember.dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.emyyn.riley.ember.Alerts.AlertActivity;
import com.emyyn.riley.ember.MainActivity;
import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.data.FakeMedicationOrders;
import com.emyyn.riley.ember.medication.MedicationDetails;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int DASHBOARD_LOADER = 1;
    private final String TAG = this.getClass().getSimpleName();
    private static final String[] DASHBOARD_COLUMNS = Utility.getDashboardColumns();

    static final int COLUMN_MED_KEY = 0;
    static final int COLUMN_MEDICATION_ORDER_ID = 1;
    static final int COLUMN_PATIENT_KEY = 2;
    static final int COLUMN_DISPENSE_SUPPLY_VALUE = 3;
    static final int COLUMN_DISPENSE_SUPPLY_UNIT = 4;

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
    static final int COLUMN_DISPENSE_QUANTITY = 20;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD = 21;
    static final int COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS = 22;
    static final int COLUMN_NEXT_DOSE = 23;
    static final int COLUMN_CHILD_ID = 28;
    static final int COLUMN_PARENT_ID = 29;


    private PatientRelationsAdapter adpater;
    private ArrayList<String> childrenList = new ArrayList<>();

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView;

        adpater = new PatientRelationsAdapter(getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.content_main, parent, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adpater);
        try {
            setAlarm(rootView);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Log.i(TAG, "adapter Set");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String child = cursor.getString(COLUMN_CHILD_ID);
                    Log.i("Child", child);
                    Intent intent = new Intent(getActivity(), AlertActivity.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(child));
                    startActivity(intent);

                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Long medicationOrderId = cursor.getLong(COLUMN_MEDICATION_ORDER_ID);
                    Intent intent = new Intent(getActivity(), MedicationDetails.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderUri(medicationOrderId));
                    startActivity(intent);
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DASHBOARD_LOADER, null, this);

        //Creates database
        try {
            FakeMedicationOrders orders = new FakeMedicationOrders(getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        childrenList.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String val = cursor.getString(DashboardFragment.COLUMN_CHILD_ID);
                if (!childrenList.contains(val)) {
                    childrenList.add(val);
                }
            }
        }
        if (!childrenList.isEmpty()) {
            SharedPreferences settings = getContext().getSharedPreferences("ChildrenArray", 0);
            SharedPreferences.Editor editor = settings.edit();
            Set<String> set = new HashSet<String>(childrenList);
            editor.putStringSet("Children", set);

            // Commit the edits!
            editor.commit();
        }


        adpater.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adpater.swapCursor(null);
    }

    public static DashboardFragment newInstance(int position) {

        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt("Args", position);
        fragment.setArguments(args);
        return fragment;
    }

    public void setAlarm(View view) throws ParseException {
        String parentId = getActivity().getString(R.string.pref_patient_default);
        Uri uri = EmberContract.RelationEntry.buildFamilyMedicationUri(parentId);
        Cursor cursor = getContext().getContentResolver().query(uri, null,null,null,null);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getContext().ALARM_SERVICE);
        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
        Long alertTime;
        while (cursor.moveToNext()){
            int i = cursor.getColumnIndexOrThrow("next_dose");
            String next_dose = cursor.getString(i);
            Log.i("DashboardFrag", next_dose.toString());
            alertTime = Utility.dateToLong(Utility.formatDate(next_dose));
            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(getActivity(), 1, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }
}
