package com.emyyn.riley.ember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.emyyn.riley.ember.data.EmberContract;

import java.util.ArrayList;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FhirContext otx = new FhirContext().forDstu2();
    IGenericClient client = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");//"http://fhirtest.uhn.ca/baseDstu2");//"http://spark-dstu2.furore.com/fhir");
    private ArrayList<String> patientIds = null;
    private final Context mContext = this;
    private Patient patient;
    private MyArrayAdapter mAdapter;
    private ArrayList<MedicationOrder> medicationOrderArrayList;
    private Medication medicationName;
    private ArrayList<Medication> medicationArray = new ArrayList<>();

    //ArrayList<Patient> children = new ArrayList<>(4); //3 would be replaced by a lookup on the number of related fields to the parent patient. Something along the lines of parent.getRelationships("guardian, parent, spouse, etc")
    //Children would also be represented as Ids and not as plain text to ensure they are looked up accordiningly. I will arbitrarily assign children until FHIR data supports this functionality.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPatientList();
        load();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView lv = (RecyclerView) findViewById(R.id.listView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(mLayoutManager);
        mAdapter = new MyArrayAdapter(this);
        lv.setAdapter(mAdapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_child1) {

        } else if (id == R.id.nav_child2) {

        } else if (id == R.id.nav_child3) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void load() {
        new AsyncTask<Void, Void, ca.uhn.fhir.model.dstu2.resource.Bundle>(
        ) {
            String[] columns = EmberContract.MedicationOrderEntry.getColumns();
            long addMedicationOrder(String medicationId) {
                long medicationOrderId = 0;

                Cursor medicationOrderCursor = mContext.getContentResolver().query(
                        EmberContract.MedicationOrderEntry.CONTENT_URI,
                        new String[]{EmberContract.MedicationOrderEntry._ID}, EmberContract.MedicationOrderEntry._ID +
                                " = ?", new String[]{medicationId}, null
                );

                if (medicationOrderCursor.moveToFirst()) {
                    int medicationOrderIdIndex = medicationOrderCursor.getColumnIndex(EmberContract.MedicationOrderEntry._ID);
                    medicationOrderId = medicationOrderCursor.getLong(medicationOrderIdIndex);
                }else {
                    ContentValues medicationOrderValues = new ContentValues();
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_MED_KEY, columns[1]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_PATIENT_KEY, columns[2]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_PRESCRIBER_KEY, columns[3]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE, columns[4]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DATE_WRITTEN, columns[5]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT, columns[6]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_CODE, columns[7]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY, columns[8]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_VALID_START, columns[9]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_VALID_END, columns[10]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TEXT, columns[11]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED, columns[12]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ROUTE, columns[13]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_METHOD, columns[14]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY, columns[15]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD, columns[16]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS, columns[17]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START, columns[18]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END, columns[19]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE, columns[20]);
                    medicationOrderValues.put(EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE, columns[21]);

                }
                return  medicationOrderId;
            };




            @Override
            protected ca.uhn.fhir.model.dstu2.resource.Bundle doInBackground(Void... params) {
                ca.uhn.fhir.model.dstu2.resource.Bundle dstu2Bundle = client.search().forResource(MedicationOrder.class)
                        .where(new StringClientParam("patient._id").matches().value("Tbt3KuCY0B5PSrJvCu2j-PlK.aiHsu2xUjUM8bWpetXoB")) //5476
                        .where(new TokenClientParam("status").exactly().code("active"))
                        .include(new Include("MedicationOrder:medication"))
                        .include(new Include("MedicationOrder:patient"))
                        .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                        .execute();
                return dstu2Bundle;
            }

            @Override
            protected void onPostExecute(ca.uhn.fhir.model.dstu2.resource.Bundle dstu2Bundle) {
                super.onPostExecute(dstu2Bundle);
                medicationOrderArrayList = new ArrayList<MedicationOrder>(dstu2Bundle.getEntry().size());
                for (int i = 0; i < dstu2Bundle.getEntry().size(); i++) {
                    ca.uhn.fhir.model.dstu2.resource.Bundle.Entry entry = dstu2Bundle.getEntry().get(i);
                    try {
                        Log.i("Entry", i + " entry ");
                        if (entry.getResource() instanceof MedicationOrder) {
                            MedicationOrder medicationOrder = (MedicationOrder) entry.getResource();
                            medicationOrderArrayList.add(medicationOrder);
                            MedicationOrder.DosageInstruction firstDosage = medicationOrder.getDosageInstructionFirstRep();
                            Log.i("DosageInstructions", firstDosage.getText());
                            //medicationOrder.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod().toString()
                            notNull(firstDosage.getRoute().getText(), "Route");
                            notNull(firstDosage.getMethod().getText(), "Method");
                            notNull(firstDosage.getTiming().getRepeat().getFrequency().toString(), "Frequency");
                            notNull(firstDosage.getTiming().getRepeat().getPeriod().toString(), "Period");
                            notNull(firstDosage.getTiming().getRepeat().getPeriodUnits().toString(), "Units");
                            notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getStart().toString(), "Start");
                            notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getEnd().toString(), "End");
                            notNull(medicationOrder.getDispenseRequest().getQuantity().getValue().toString(), "Q");

                        }
                        // + firstDosage.getRoute() + firstDosage.getText());
                        else if (entry.getResource() instanceof Patient) {
                            patient = (Patient) entry.getResource();
                            Log.i("Patient", patient.getId().getIdPart() + patient.getNameFirstRep().getFamilyFirstRep() + " , " + patient.getNameFirstRep().getGivenFirstRep());
                        } else if (entry.getResource() instanceof Medication) {
                            medicationName = (Medication) entry.getResource();
                            medicationArray.add(medicationName);
                            Log.i("Medication Name", medicationName.getCode().getCoding().get(0).getCode());
                        } else {
                            Log.i("Exit Else", "no items in bundle");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                mAdapter.setData(medicationOrderArrayList, patient, medicationArray);
            }
        }.execute();
    }

    public void initPatientList() {
        String TAG = this.getClass().getSimpleName();
        patientIds = new ArrayList<String>(6);
        patientIds.add("5149");
        patientIds.add("5401");
        patientIds.add("5413");
        patientIds.add("5425");
        patientIds.add("5437");
        patientIds.add("5476");
    }

    public Boolean notNull(String s, String title) {
        if (s != null) {
            Log.i(title + " ", s);
            return true;
        } else {
            Log.i("Null", title);
            return false;
        }
    }
}