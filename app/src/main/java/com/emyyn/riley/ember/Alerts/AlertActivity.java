package com.emyyn.riley.ember.Alerts;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;

import java.util.ArrayList;
import java.util.Set;

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
        private Uri mUri;
        private String TAG = this.getClass().getSimpleName();

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
            mAlertAdapter = new AlertAdapter(getActivity(), null, 0);
            View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
            ListView lv = (ListView) rootView.findViewById(R.id.alert_list);
            lv.setAdapter(mAlertAdapter);
            return rootView;
        }

        void onLocationChanged(String s) {
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
            Uri uri = Uri.EMPTY;
            if (args != null) {
                String arg = String.valueOf(args.get(ARG_SECTION_NUMBER));
                uri = EmberContract.MedicationOrderEntry.buildMedicationOrderWithPatientId(arg);
            }else {
                uri = getActivity().getIntent().getData();
            }
            Log.i(TAG, String.valueOf(uri));
            //Uri uri = EmberContract.RelationEntry.buildFamilyUri(parentId);
            Loader<Cursor> lc = new CursorLoader(getActivity(),
                    uri,
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

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.tv_fragment_dashboard);
            textView.setText(ARG_SECTION_NUMBER);
            Log.i(this.getClass().getSimpleName(), this.getClass().getSimpleName() + " after text view assignment");
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> children;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            SharedPreferences settings = getSharedPreferences("ChildrenArray", 0);
            Set<String> set = settings.getStringSet("Children", null);
            children = new ArrayList<String>(set);
           // Log.i("Children Pref Size", String.valueOf(children.size()));
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.i("Children Id", String.valueOf(children.get(position)));
            switch (position){
                case 0: {
                    return AlertActivity.AlertFragment.newInstance(children.get(position+1));
                }
                case 1: {
                    return PlaceholderFragment.newInstance((position+1));
                }
                case 2: {
                    return AlertActivity.AlertFragment.newInstance(children.get(position+1));
                }
                case 3: {
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
            Log.i("Children Size", String.valueOf(children.size()));
            return children.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            for (int i = 0; i < getCount(); i++) {
                if (position == i) {
                    return children.get(i) + " " + i;
                }
            }
            return null;
        }
    }
}