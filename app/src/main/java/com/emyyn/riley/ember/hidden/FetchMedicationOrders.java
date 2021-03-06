package com.emyyn.riley.ember.hidden;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.emyyn.riley.ember.medication.MedicationOrderAdpater;
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.data.EmberContract.PatientEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_CODE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_METHOD;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ROUTE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TEXT;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_LAST_UPDATED_AT;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_MED_KEY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_PATIENT_KEY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_PRESCRIBER_KEY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_VALID_END;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_VALID_START;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.CONTENT_URI;

/**
 * Created by Riley on 5/6/2016.
 */
public class FetchMedicationOrders extends AsyncTask<String, Void, Void> {
    private final Context mContext;
    private final String TAG = this.getClass().getSimpleName();

    //    IGenericClient client = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");//"http://fhirtest.uhn.ca/baseDstu2");//"http://spark-dstu2.furore.com/fhir");
    FhirContext otx; //= new FhirContext().forDstu2();
    IGenericClient client; // = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");

    private ArrayList<MedicationOrder> medicationOrderArrayList;
    private Medication medication;
    private ArrayList<Medication> medicationArray = new ArrayList<>();
    private MedicationOrderAdpater mAdapter;
    private Patient patient;
    private Bundle bundle;
    private Bundle patientBundle;

    public FetchMedicationOrders(Context activity) {
        mContext = activity;
    }

    long addPatient(String id, String status, String language, String ethic, String given, String race, String zip, String state, String city, String address, String family, String active, String gender, String dob, String provider_key, String med_key) {
        long patientId;
        Cursor patientCursor = mContext.getContentResolver().query(CONTENT_URI, new String[]{PatientEntry.COLUMN_PATIENT_ID}, null, null, null);

        if (patientCursor.moveToFirst()) {
            int patientIndex = patientCursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_ID);
            patientId = patientCursor.getLong(patientIndex);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PatientEntry.COLUMN_PROVIDER_KEY, provider_key);
            contentValues.put(PatientEntry.COLUMN_DOB, dob);
            contentValues.put(PatientEntry.COLUMN_GENDER, gender);
            contentValues.put(PatientEntry.COLUMN_ACTIVE, active);
            contentValues.put(PatientEntry.COLUMN_PATIENT_ID, id);
            contentValues.put(PatientEntry.COLUMN_NAME_FAMILY, family);
            contentValues.put(PatientEntry.COLUMN_NAME_GIVEN, given);
            contentValues.put(PatientEntry.COLUMN_ADDRESS, address);
            contentValues.put(PatientEntry.COLUMN_CITY, city);
            contentValues.put(PatientEntry.COLUMN_STATE, state);
            contentValues.put(PatientEntry.COLUMN_ZIP, zip);
            contentValues.put(PatientEntry.COLUMN_RACE, race);
            contentValues.put(PatientEntry.COLUMN_ETHINICITY, ethic);
            contentValues.put(PatientEntry.COLUMN_LANGUAGE, language);
            contentValues.put(PatientEntry.COLUMN_MARRIED_STATUS, status);

            Uri insertedUri = mContext.getContentResolver().insert(
                    EmberContract.PatientEntry.CONTENT_URI,
                    contentValues);
            patientId = ContentUris.parseId(insertedUri);
        }
        return patientId;
    }

    long addMedication(String id, String family, String active, String name, String medication_id) {
        long patientId;
        Cursor patientCursor = mContext.getContentResolver().query(EmberContract.MedicationEntry.CONTENT_URI, new String[]{EmberContract.MedicationEntry._ID}, null, null, null);

        if (patientCursor.moveToFirst()) {
            int patientIndex = patientCursor.getColumnIndex(EmberContract.MedicationEntry._ID);
            patientId = patientCursor.getLong(patientIndex);
        } else {
            ContentValues medicationValues = new ContentValues();
            medicationValues.put(EmberContract.MedicationEntry.COLUMN_CODE_TEXT, name);
            medicationValues.put(EmberContract.MedicationEntry._ID, medication_id);

            Uri insertedUri = mContext.getContentResolver().insert(
                    EmberContract.MedicationEntry.CONTENT_URI,
                    medicationValues);
            patientId = ContentUris.parseId(insertedUri);
        }
        return patientId;
    }

    public String notNull(String s, String title) {
        if (s != null) {
            Log.i(title + " ", s);
            return s;
        } else {
            Log.i("Null", title);
            return "title";
        }
    }

    protected void getMedicationOrderFromBundle(Bundle bundle) {
        Vector<ContentValues> cVVector = new Vector<ContentValues>();
        long patientId = 0;
        medicationOrderArrayList = new ArrayList<MedicationOrder>(bundle.getEntry().size());
        for (int i = 0; i < bundle.getEntry().size(); i++) {
            ca.uhn.fhir.model.dstu2.resource.Bundle.Entry entry = bundle.getEntry().get(i);
            try {
                if (entry.getResource() instanceof MedicationOrder) {
                    MedicationOrder medicationOrder = (MedicationOrder) entry.getResource();
                    medicationOrderArrayList.add(medicationOrder);
                    MedicationOrder.DosageInstruction firstDosage = medicationOrder.getDosageInstructionFirstRep();
                    //Log.i("DosageInstructions", firstDosage.getText());
                    //medicationOrder.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod().toString()
                    //Enter into Database
                    ContentValues medicationOrderValues = new ContentValues();

                    medicationOrderValues.put(COLUMN_MED_KEY, notNull(medicationOrder.MEDICATION.getParamName(), "Medicaiton Id"));
                    medicationOrderValues.put(COLUMN_MEDICATION_ORDER_ID, Integer.toString((firstDosage.getText().hashCode() + medicationOrder.getPatient().getReference().getIdPart() + medicationOrder.getDispenseRequest().getValidityPeriod().getStart().toString()).hashCode()));
                    medicationOrderValues.put(COLUMN_PATIENT_KEY, notNull(medicationOrder.getPatient().getReference().getIdPart(), "Pid"));   ///Long.valueOf(notNull(Long.toString(patientId), "patient"
                    medicationOrderValues.put(COLUMN_PRESCRIBER_KEY, notNull(medicationOrder.getPrescriber().getReference().getIdPart(), "prescriber id"));
                    medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_VALUE, notNull(medicationOrder.getDispenseRequest().getExpectedSupplyDuration().getValue().toString(), "dispense Supply Value"));
//                    medicationOrderValues.put(COLUMN_DATE_WRITTEN, notNull(medicationOrder.getDateWritten().toString(), "date Written"));
                    medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_UNIT, notNull(medicationOrder.getDispenseRequest().getExpectedSupplyDuration().getUnit().toString(), "Supply Unit"));
                    medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_CODE, notNull(medicationOrder.getDispenseRequest().getExpectedSupplyDuration().getCode().toString(), "Supply Code"));
                    medicationOrderValues.put(COLUMN_DISPENSE_QUANTITY, notNull(medicationOrder.getDispenseRequest().getQuantity().getValue().toString(), "Quantity"));
                    medicationOrderValues.put(COLUMN_VALID_START, notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getStart().toString(), "Start Valid"));
                    medicationOrderValues.put(COLUMN_VALID_END, notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getEnd().toString(), "end Value"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TEXT, notNull(firstDosage.getText().toString(), "Instructions"));
 //                   medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED, notNull(firstDosage.getAsNeeded().toString(), "As needed?"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_ROUTE, notNull(firstDosage.getRoute().getText(), "Route"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_METHOD, notNull(firstDosage.getMethod().getText(), "Method"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY, notNull(firstDosage.getTiming().getRepeat().getFrequency().toString(), "Frequency"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD, notNull(firstDosage.getTiming().getRepeat().getPeriod().toString(), "Period"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS, notNull(firstDosage.getTiming().getRepeat().getPeriodUnits().toString(), "Units"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START, notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getStart().toString(), "Start"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END, notNull(medicationOrder.getDispenseRequest().getValidityPeriod().getEnd().toString(), "End"));
                    medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE, notNull(medicationOrder.getDispenseRequest().getQuantity().getValue().toString(), "Q"));
                    medicationOrderValues.put(COLUMN_LAST_UPDATED_AT, Calendar.getInstance().getTime().toString());
                    //medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE, notNull(medicationOrder.getDispenseRequest().getQuantity().getCode().toString(), "Dispense Code"));
                    cVVector.add(medicationOrderValues);
                    //end database

                }
                // + firstDosage.getRoute() + firstDosage.getText());
                else if (entry.getResource() instanceof Patient) {
                    Patient patient = (Patient) entry.getResource();
                    String given;
                    String family;
                    String active;
                    String gender;
                    String id;
                    String dob = Calendar.getInstance().getTime().toString();

                    given = patient.getNameFirstRep().getGivenFirstRep().getValue();
                    family = patient.getNameFirstRep().getFamilyFirstRep().getValue();
                    active = String.valueOf(getRandomBoolean());;//patient.getActive().toString();
                    gender = String.valueOf(getRandomBoolean());
                    id = patient.getId().getIdPart();
                    //dob = notNull( patient.getBirthDate().toString(), "dob");

                    patientId = addPatient(id, "status", "language", "ethinic", given, "race", "zip", "state", "city", "address", family, active, gender, dob, "provider_key", "med_key");
                    Log.i("Patient ID", Long.toString(patientId));
                    Log.i("Patient", family + " , " + given);
                } else if (entry.getResource() instanceof Medication) {
                    medication = (Medication) entry.getResource();
                    String medicationName = medication.getCode().getCoding().get(0).getCode();
                    addMedication(null, null, null, medicationName, medication.getId().getIdPart());
                    Log.i("Medication Name", medicationName + "ID: "+ medication.getId().getIdPart());
                } else {
                    Log.i("Exit Else", "no items in bundle");
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(EmberContract.MedicationOrderEntry.CONTENT_URI, cvArray);
        }
        //mAdapter.setData(medicationOrderArrayList, patient, medicationArray);
    }

    public boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }

    @Override
    protected Void doInBackground(String... params) {
        otx = new FhirContext().forDstu2();
        //client = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");
        client = otx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");

        bundle = client.search().forResource(MedicationOrder.class)
                .where(new StringClientParam("patient")
                        .matches().value("5401"))   //Tbt3KuCY0B5PSrJvCu2j-PlK.aiHsu2xUjUM8bWpetXoB
                .where(new TokenClientParam("status").exactly().code("active"))
                .include(new Include("MedicationOrder:medication"))
                .include(new Include("MedicationOrder:patient"))
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();
        Log.i(TAG, bundle.getResourceName().toString());
        Log.i("Do in background", "Doing in the background, get ready.");
        getMedicationOrderFromBundle(bundle);
        return null;
    }
}