package com.emyyn.riley.ember.dashboard;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emyyn.riley.ember.Alerts.AlertActivity;
import com.emyyn.riley.ember.Alerts.AlertReceiver;
import com.emyyn.riley.ember.MainActivity;
import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.data.FakeMedicationOrders;
import com.emyyn.riley.ember.hidden.FetchPatientData;
import com.emyyn.riley.ember.medication.MedicationDetails;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import layout.Notifications;


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
    ArrayList<PendingIntent> intentArray;
    Animation RightSwipe;
    Animation LeftSwipe;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView;
        RightSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right);
        LeftSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left);
        adpater = new PatientRelationsAdapter(getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.content_main, parent, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adpater);
        SharedPreferences sharedPreferences = getActivity().getPreferences(R.string.alerts_key);
        boolean notifications = sharedPreferences.getBoolean(String.valueOf(R.string.alerts_key), Boolean.TRUE);
        if (notifications) {
            try {
                setAlarm();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Log.i(TAG, "adapter Set");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String child = cursor.getString(COLUMN_CHILD_ID);
                    //Log.i("Child", child);
                    Intent intent = new Intent(getActivity(), AlertActivity.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(child));
                    parent.startAnimation(RightSwipe);
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
                    parent.startAnimation(LeftSwipe);
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
        SharedPreferences sharedPreferences = getActivity().getPreferences(R.string.sync_mode_key);
        boolean offline = sharedPreferences.getBoolean(String.valueOf(R.string.sync_mode_key), Boolean.FALSE);
        if (offline) {
            try {
                FakeMedicationOrders orders = new FakeMedicationOrders(getActivity());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FetchPatientData fetchPatientData = new FetchPatientData(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO this is the shared preferences being drawn in
        SharedPreferences settings = getActivity().getSharedPreferences("parent_id", 0);
        String parentId = settings.getString("parent_id", null);
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

    public void setAlarm() throws ParseException {
        //TODO shared preferences
        SharedPreferences settings = getActivity().getSharedPreferences("parent_id", 0);
        String parentId = settings.getString("parent_id", null);
        Uri uri = EmberContract.RelationEntry.buildFamilyMedicationUri(parentId);
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (intentArray == null) {
            intentArray = new ArrayList<PendingIntent>();
        } else {
            intentArray.clear();
        }

        Long alertTime;
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        if (cursor.moveToFirst()) {
            do {
                final int _id = (int) System.currentTimeMillis();
                int id = cursor.getColumnIndexOrThrow("next_dose");
                String next_dose = cursor.getString(id);
                alertTime = Utility.dateToLong(Utility.formatDate(next_dose));
                //Intent and Pending Intent
                Intent intent = new Intent(getContext(), AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), _id, intent, 0);
//alarm Manager
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        alertTime,
                        pendingIntent);
                intentArray.add(pendingIntent);
            } while ((cursor.moveToNext()));
        }

    }
}
