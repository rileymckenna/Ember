package com.emyyn.riley.ember.hidden;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.data.EmberContract.PatientEntry;

import java.util.Vector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;

import static android.provider.BaseColumns._ID;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.CONTENT_URI;


/**
 * Created by Riley on 5/6/2016.
 */
public class FetchPatientData extends AsyncTask<String, Void, Void> {
    private final Context mContext;
    private final String TAG = this.getClass().getSimpleName();

    //    IGenericClient client = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");//"http://fhirtest.uhn.ca/baseDstu2");//"http://spark-dstu2.furore.com/fhir");
    FhirContext otx; //= new FhirContext().forDstu2();
    IGenericClient client; // = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");
    private Patient patient;
    private Bundle bundle;

    public FetchPatientData(Context activity) {
        mContext = activity;
    }

    long addPatient(String id, String status, String language, String ethic, String given, String race, String zip, String state, String city, String address, String family, String active, String gender, String dob, String provider_key, String med_key) {
        long patientId;
        Cursor patientCursor = mContext.getContentResolver().query(CONTENT_URI, new String[]{PatientEntry._ID}, null, null, null);

        if (patientCursor.moveToFirst()) {
            int patientIndex = patientCursor.getColumnIndex(_ID);
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
                    PatientEntry.CONTENT_URI,
                    contentValues);
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
            return "trees";
        }
    }

    protected void getPatientFromBundle(Bundle bundle) {
        Vector<ContentValues> cVVector = new Vector<ContentValues>();
        long patientId = 0;
        for (int i = 0; i < bundle.getEntry().size(); i++) {
            Bundle.Entry entry = bundle.getEntry().get(i);
            try {
                if (entry.getResource() instanceof Patient) {
                    Patient patient = (Patient) entry.getResource();
                    String given;
                    String family;
                    String active;
                    String gender;
                    String id;
                    String dob;

                    given = patient.getNameFirstRep().getGivenFirstRep().getValue();
                    family = patient.getNameFirstRep().getFamilyFirstRep().getValue();
                    active = patient.getActive().toString();
                    gender = patient.getGender();
                    id = patient.getId().getIdPart();
                    dob = patient.getBirthDate().toString();

                    //patientId = addPatient(id, "status", "language", "ethinic", given, "race", "zip", "state", "city", "address", family, active, gender, dob, "provider_key", "med_key");
                    Log.i("Patient ID", Long.toString(patientId));
                    Log.i("Patient", family + " , " + given);
                } else {
                    Log.i("Exit Else", "no items in bundle");
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(EmberContract.MedicationOrderEntry.CONTENT_URI, cvArray);
        }
        //mAdapter.setData(medicationOrderArrayList, patient, medicationArray);
    }

    @Override
    protected Void doInBackground(String... params) {
        otx = new FhirContext().forDstu2();
        client = otx.newRestfulGenericClient("https://open-ic.epic.com/FHIR/api/FHIR/DSTU2");
        //client = otx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");

        bundle = client.search().forResource(Patient.class)
                .where(new StringClientParam("patient")
                        .matches().value(params[0]))   //5401
                .returnBundle(Bundle.class)
                .execute();
        Log.i(TAG, "Patient Bundle");
        getPatientFromBundle(bundle);
        return null;
    }
}