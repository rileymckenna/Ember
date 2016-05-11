package com.emyyn.riley.ember.medication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.data.EmberProvider;

public class MedicationDetails extends AppCompatActivity  {

    private static final String TAG = "MedicationDetails";
    private String uri = "";
    private EmberProvider provider = new EmberProvider();
    private Cursor c;

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
    static final int COLUMN_PRODUCT = 25;
    static final int COLUMN_NAME_GIVEN = 26;
    static final int COLUMN_NAME_FAMILY = 27;
    static final int COLUMN_STATUS = 28;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);
        final TextView v = (TextView) findViewById(R.id.details_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String content  = "";
        Intent intent = getIntent();
        if (intent != null){
            content = intent.getDataString();
            uri = content;
            Log.i(TAG, content.toString());
        }
        Uri u = Uri.parse(uri);
        try {
            c = provider.query(u, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }



//        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab);
//        if (fabSpeedDial != null) {
//            fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
//                @Override
//                public boolean onPrepareMenu(NavigationMenu navigationMenu) {
//                    // TODO: Do something with yout menu items, or return false if you don't want to show them
//                    return true;
//                }
//                @Override
//                public boolean onMenuItemSelected(MenuItem menuItem) {
//                    //TODO: Start some activity
////                    Snackbar.make(v != null ? v : null, "Replace with your own action", Snackbar.LENGTH_LONG)
////                            .setAction("Action", null).show();
//                    return false;
//                }
//            });
//        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
