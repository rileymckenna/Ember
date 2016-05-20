package com.emyyn.riley.ember.Alerts;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.medication.MedicationDetails;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Set;

import static com.emyyn.riley.ember.data.EmberContract.PATH_MEDICATIONORDER;

public class AlertActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_alert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        String id = EmberContract.MedicationOrderEntry.getPatientId(getIntent().getData());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), id);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AlertFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private AlertAdapter mAlertAdapter;
        private static int ALERT_LOADER = 13;
        private static String[] DASHBOARD_COLUMNS = Utility.getDashboardColumns();
        private static Uri mUri;
        private static String TAG = "AlertFragment";
        private View collapsable;
        private static Context mContext;
        private static AlertFragment mAlertFragment;

        public AlertFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param sectionNumber
         */
        public static AlertFragment newInstance(String sectionNumber) {
            AlertFragment fragment = new AlertFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mContext = getContext();
            mAlertAdapter = new AlertAdapter(getActivity(), null, 0);
            View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
            ListView lv = (ListView) rootView.findViewById(R.id.alert_list);
            lv.setAdapter(mAlertAdapter);
            mAlertFragment = this;
            return rootView;
        }


        public static void onTimingChanged(AlertFragment t, String s, int total, int pills, int period, String next, String status) throws ParseException {
            // replace the uri, since the location has changed
            Uri uri = mUri;
            Cursor c;
            String mSelectionClause = EmberContract.MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID + " = ?";
            String[] mSelectionArgs = new String[]{s};
            Uri u = EmberContract.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATIONORDER).appendPath(s).build();
            if (null != uri) {
                if (status == AlertAdapter.SNOOZE) {
                    ContentValues values = new ContentValues();
                    String timeNow = next;
                    String next_dose = Utility.getSnoozeThisDose(timeNow, period);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT, timeNow);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_NEXT_DOSE, next_dose);
                    final int update = mContext.getContentResolver().update(u, values, mSelectionClause, mSelectionArgs);
                } else if (status == AlertAdapter.SKIP) {
                    ContentValues values = new ContentValues();
                    String timeNow = next;
                    String next_dose = Utility.getNextDoseDate(timeNow, period);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT, timeNow);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_NEXT_DOSE, next_dose);
                    final int update = mContext.getContentResolver().update(u, values, mSelectionClause, mSelectionArgs);
                } else if (status == AlertAdapter.TAKE) {
                    ContentValues values = new ContentValues();
                    String running_total = String.valueOf(total - pills);
                    String timeNow = Utility.getTimeNow();
                    String next_dose = Utility.getNextDoseDate(timeNow, period);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_RUNNING_TOTAL, running_total);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT, timeNow);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_LAST_TAKEN, timeNow);
                    values.put(EmberContract.MedicationOrderEntry.COLUMN_NEXT_DOSE, next_dose);
                    final int update = mContext.getContentResolver().update(u, values, mSelectionClause, mSelectionArgs);
                }
            }
            Uri updatedUri = EmberContract.MedicationOrderEntry.buildMedicationOrderUri(s);
            mUri = updatedUri;
            t.getLoaderManager().restartLoader(ALERT_LOADER, null, t);
        }

        public void refreshFragment(){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }


        static void snoozeMedication(View v) throws ParseException {
            View parent = (View) v.getParent();
            TextView id, running_total, pills, last_taken, last_updated, next_dose;
            id = (TextView) parent.findViewById(R.id.alert_medication_id);
            running_total = (TextView) parent.findViewById(R.id.alert_total);
            pills = (TextView) parent.findViewById(R.id.alert_pills);
            next_dose = (TextView) parent.findViewById(R.id.time_text);
            String total, pill, s, next;
            next = (String) next_dose.getText();
            total = (String) running_total.getText();
            pill = (String) pills.getText();
            s = (String) id.getText();

            Toast.makeText(mContext, s + " Snoozed",
                    Toast.LENGTH_SHORT).show();
            onTimingChanged(mAlertFragment, s, Integer.parseInt(total), Integer.parseInt(pill), 15, next, AlertAdapter.SNOOZE);
        }

        static void takeMedication(View v) throws ParseException {
            View parent = (View) v.getParent();
            TextView id, running_total, pills, last_taken, last_updated, frequency;

            id = (TextView) parent.findViewById(R.id.alert_medication_id);
            running_total = (TextView) parent.findViewById(R.id.alert_total);
            pills = (TextView) parent.findViewById(R.id.alert_pills);
            frequency = (TextView) parent.findViewById(R.id.alert_frequency);
            String total, pill, s, freq;
            total = (String) running_total.getText();
            pill = (String) pills.getText();
            freq = (String) frequency.getText();
            s = (String) id.getText();

            Toast.makeText(mContext, s + " Taken",
                    Toast.LENGTH_SHORT).show();
            onTimingChanged(mAlertFragment, s, Integer.parseInt(total), Integer.parseInt(pill), Integer.parseInt(freq), "", AlertAdapter.TAKE);
        }

        static void skipMedication(View v) throws ParseException {
            View parent = (View) v.getParent();
            TextView id, running_total, pills, last_taken, last_updated, next_dose, frequency;
            id = (TextView) parent.findViewById(R.id.alert_medication_id);
            running_total = (TextView) parent.findViewById(R.id.alert_total);
            pills = (TextView) parent.findViewById(R.id.alert_pills);
            next_dose = (TextView) parent.findViewById(R.id.time_text);
            frequency = (TextView) parent.findViewById(R.id.alert_frequency);

            String total, pill, s, next, freq;
            next = (String) next_dose.getText();
            total = (String) running_total.getText();
            pill = (String) pills.getText();
            s = (String) id.getText();
            freq = (String) frequency.getText();

            Toast.makeText(mContext, s + " Skipped",
                    Toast.LENGTH_SHORT).show();
            onTimingChanged(mAlertFragment, s, Integer.parseInt(total), Integer.parseInt(pill), Integer.parseInt(freq), next, AlertAdapter.SKIP);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(ALERT_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            if (this.getArguments() != null) {
                String arg = String.valueOf(this.getArguments().get(ARG_SECTION_NUMBER));
                mUri = EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(arg);
            } else {
                mUri = getActivity().getIntent().getData();
            }
            Log.i(TAG, String.valueOf(mUri));
            //Uri uri = EmberContract.RelationEntry.buildFamilyUri(parentId);
            Loader<Cursor> lc = new CursorLoader(getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null);

            return lc;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAlertAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAlertAdapter.swapCursor(null);
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> children;
        String firstChild;

        public SectionsPagerAdapter(FragmentManager fm, String childid) {
            super(fm);
            SharedPreferences settings = getSharedPreferences("ChildrenArray", 0);
            Set<String> set = settings.getStringSet("Children", null);
            children = new ArrayList<String>(set);
            int fc = children.indexOf(childid);
            firstChild = children.get(fc);
            ArrayList<String> stringArray = new ArrayList<>();
            stringArray.add(firstChild);
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) != firstChild) {
                    stringArray.add(children.get(i));
                }
            }
            children = stringArray;

            // Log.i("Children Pref Size", String.valueOf(children.size()));
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //Log.i("Children Id", String.valueOf(children.get(position)));
            switch (position) {
                case 0: {
                    // Log.i("Children Id", String.valueOf(children.get(position)));
                    return AlertActivity.AlertFragment.newInstance(children.get(position));
                }
                case 1: {
                    // Log.i("Children Id", String.valueOf(children.get(position)));
                    return AlertActivity.AlertFragment.newInstance(children.get(position));
                }
                case 2: {
                    //  Log.i("Children Id", String.valueOf(children.get(position)));
                    return AlertActivity.AlertFragment.newInstance(children.get(position));
                }
                case 3: {
                    //  Log.i("Children Id", String.valueOf(children.get(position)));
                    return AlertActivity.AlertFragment.newInstance(children.get(position));
                }
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return children.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return children.get(position);
                case 1:
                    return children.get(position);
                case 2:
                    return children.get(position);
                case 3:
                    return children.get(position);
                default:
                    return "Alert Manager";
            }
        }
    }
}