package com.emyyn.riley.ember;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MedicationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);
        final View v = findViewById(R.id.details_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
