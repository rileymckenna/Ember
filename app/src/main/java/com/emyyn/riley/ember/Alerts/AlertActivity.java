package com.emyyn.riley.ember.Alerts;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

    public static Fragment newInstance(int position) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        args.putInt("Args", position);
        fragment.setArguments(args);
        return fragment;
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
        private int ALERT_LOADER = 13;
        private String[] DASHBOARD_COLUMNS = Utility.getDashboardColumns();
        static final String DETAIL_URI = "URI";
        private Uri mUri;

        public AlertFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AlertFragment newInstance(int sectionNumber) {
            AlertFragment fragment = new AlertFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mUri = arguments.getParcelable(AlertFragment.DETAIL_URI);
            }
            mAlertAdapter = new AlertAdapter(getActivity(), null, 0);
            View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
            ListView lv = (ListView) rootView.findViewById(R.id.alert_list);
            lv.setAdapter(mAlertAdapter);
            return rootView;
        }

        void onLocationChanged( String s ) {
            // replace the uri, since the location has changed
            Uri uri = mUri;
            if (null != uri) {
                String id = EmberContract.MedicationOrderEntry.getPatientId(uri);
                Uri updatedUri = EmberContract.MedicationOrderEntry.buildMedicationOrderUri(s);
                mUri = updatedUri;
                getLoaderManager().restartLoader(ALERT_LOADER, null, this);
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(ALERT_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String parentId = getActivity().getString(R.string.pref_patient_default);

            Uri uri = EmberContract.RelationEntry.buildFamilyUri(parentId);
            Loader<Cursor> lc = new CursorLoader(getActivity(),
                    mUri,
                    DASHBOARD_COLUMNS,
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
}