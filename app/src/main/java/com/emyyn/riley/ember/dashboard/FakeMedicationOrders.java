package com.emyyn.riley.ember.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.data.EmberContract.PatientEntry;
import com.emyyn.riley.ember.data.FakeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;

import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DATE_WRITTEN;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_CODE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE;
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
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_REASON_GIVEN;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_RUNNING_TOTAL;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_STATUS;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_VALID_END;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry.COLUMN_VALID_START;

/**
 * Created by Riley on 5/6/2016.
 */
public class FakeMedicationOrders {
    private final Context mContext;
    private String TAG;
    FakeData dh = new FakeData();
    Vector<ContentValues> cVVector = new Vector<ContentValues>();

    public FakeMedicationOrders(Context activity) throws JSONException, ParseException {
        mContext = activity;

        // setMedicationOrderData();
        //Log.i("", "SetMedicationOrderData");
        //  setMedicationData();
        //Log.i("", "SetMedicationData");
        //  setProviderData();
        //Log.i("", "SetProviderData");
        //  setPatientData();
        //Log.i("", "SetPatientData");
        //  setRelationData();

    }

    protected void bulkInsert(Uri uri) {
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(uri, cvArray);
        }
    }

    public void setMedicationOrderData() throws JSONException, ParseException {
        TAG = "setMedicationOrder";
        String medicationOrder_id;
        String dispense_supply_value;
        String dispense_quantity;
        String valid_start;
        String valid_end;
        String instructions_text;
        String method;
        String timing_period;
        String timing_frequency;
        String dose_value;
        String dose_code;
        String reason_given;
        String running_total;
        String provider_id;
        String patient_id;
        String medication_id;
        String last_taken;
        String status;


        String json = dh.getMedicationOrder();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            medicationOrder_id = object.getString("medicationOrder_id");
            dispense_supply_value = object.getString("dispense_supply_value");
            dispense_quantity = object.getString("dispense_quantity");
            valid_start = object.getString("valid_start");
            valid_end = object.getString("valid_end");
            instructions_text = object.getString("instructions_text");
            method = object.getString("method");
            timing_period = object.getString("timing_period");
            timing_frequency = object.getString("timing_frequency");
            dose_value = object.getString("dose_value");
            dose_code = object.getString("dose_code");
            reason_given = object.getString("reason_given");
            running_total = object.getString("running_total");
            provider_id = object.getString("provider_id");
            patient_id = object.getString("patient_id");
            medication_id = object.getString("medication_id");
            status = object.getString("status");
            String fullInstrucitons = instructions_text + "(" + dose_value + dose_code + ") every " + timing_period + "hrs " + method + " " + timing_frequency;
            //Log.i(TAG, fullInstrucitons);
            //Log.i("DosageInstructions", firstDosage.getText());
            //medicationOrder.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod().toString()
            //Enter into Database
            ContentValues medicationOrderValues = new ContentValues();

            medicationOrderValues.put(COLUMN_MED_KEY, medication_id);
            medicationOrderValues.put(COLUMN_MEDICATION_ORDER_ID, medicationOrder_id);
            medicationOrderValues.put(COLUMN_PATIENT_KEY, patient_id);   ///Long.valueOf(notNull(Long.toString(patientId), "patient"
            medicationOrderValues.put(COLUMN_PRESCRIBER_KEY, provider_id);
            medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_VALUE, dispense_supply_value);
            medicationOrderValues.put(COLUMN_DATE_WRITTEN, valid_start);
            medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_UNIT, "days");
            medicationOrderValues.put(COLUMN_DISPENSE_SUPPLY_CODE, "d");
            medicationOrderValues.put(COLUMN_DISPENSE_QUANTITY, dispense_quantity);
            medicationOrderValues.put(COLUMN_VALID_START, valid_start);
            medicationOrderValues.put(COLUMN_VALID_END, valid_end);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TEXT, fullInstrucitons);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED, "False");
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_ROUTE, "Oral");
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_METHOD, method);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY, timing_frequency);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD, timing_frequency);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS, "hrs");
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START, valid_start);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END, valid_end);
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE, dose_value);
            medicationOrderValues.put(COLUMN_LAST_UPDATED_AT, Calendar.getInstance().getTime().toString());
            medicationOrderValues.put(COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE, dose_code);
            medicationOrderValues.put(COLUMN_REASON_GIVEN, reason_given);
            medicationOrderValues.put(COLUMN_STATUS, status);
            medicationOrderValues.put(COLUMN_RUNNING_TOTAL, running_total);
            cVVector.add(medicationOrderValues);
            //end database
        }
        bulkInsert(EmberContract.MedicationOrderEntry.CONTENT_URI);
    }

    public void setPatientData() throws JSONException, ParseException {
        cVVector.clear();
        TAG = "setPatientData";
        String pt_id;
        String name_family;
        String name_given;
        String active;
        String dob;
        String medication_id;
        String provider_id;

        String json = dh.getPatient();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject patient = jsonArray.getJSONObject(i);
            pt_id = patient.getString("patient_id");
            name_family = patient.getString("name_family");
            name_given = patient.getString("name_given");
            active = patient.getString("active");
            dob = patient.getString("dob");
            provider_id = patient.getString("prescriber_id");
            String language = "EN";
            String status = "NSTR";
            String ethic = "O";
            String race = "NA";
            String zip = "10997";
            String state = "NY";
            String city = "New USA";
            String address = "123 Street Ct.";
            String gender = "female";

            //Sets up the patient Values
            ContentValues contentValues = new ContentValues();

            contentValues.put(PatientEntry.COLUMN_PROVIDER_KEY, provider_id);
            contentValues.put(PatientEntry.COLUMN_DOB, dob);
            contentValues.put(PatientEntry.COLUMN_GENDER, gender);
            contentValues.put(PatientEntry.COLUMN_ACTIVE, active);
            contentValues.put(PatientEntry.COLUMN_PATIENT_ID, pt_id);
            contentValues.put(PatientEntry.COLUMN_NAME_FAMILY, name_family);
            contentValues.put(PatientEntry.COLUMN_NAME_GIVEN, name_given);
            contentValues.put(PatientEntry.COLUMN_ADDRESS, address);
            contentValues.put(PatientEntry.COLUMN_CITY, city);
            contentValues.put(PatientEntry.COLUMN_STATE, state);
            contentValues.put(PatientEntry.COLUMN_ZIP, zip);
            contentValues.put(PatientEntry.COLUMN_RACE, race);
            contentValues.put(PatientEntry.COLUMN_ETHINICITY, ethic);
            contentValues.put(PatientEntry.COLUMN_LANGUAGE, language);
            contentValues.put(PatientEntry.COLUMN_MARRIED_STATUS, status);
            cVVector.add(contentValues);
        }
        bulkInsert(PatientEntry.CONTENT_URI);
    }

    public void setMedicationData() throws JSONException, ParseException {
        cVVector.clear();
        TAG = "setMedicationData";
        String product;
        String code_text;
        String medication_id;

        String json = dh.getMedication();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject patient = jsonArray.getJSONObject(i);
            product = patient.getString("product");
            code_text = patient.getString("code_text");
            medication_id = patient.getString("medication_id");

            ContentValues contentValues = new ContentValues();
            contentValues.put(EmberContract.MedicationEntry.COLUMN_PRODUCT, product);
            contentValues.put(EmberContract.MedicationEntry.COLUMN_CODE_TEXT, code_text);
            contentValues.put(EmberContract.MedicationEntry._ID, medication_id);

            cVVector.add(contentValues);
        }
        bulkInsert(EmberContract.MedicationEntry.CONTENT_URI);
    }

    public void setRelationData() throws JSONException, ParseException {
        cVVector.clear();
        TAG = "setRelationData";
        String patient_id;
        String child_id;

        String json = dh.getRelations();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            patient_id = object.getString("patient_id");
            child_id = object.getString("child_id");

            ContentValues contentValues = new ContentValues();
            contentValues.put(EmberContract.RelationEntry.COLUMN_PATIENT_ID, patient_id);
            contentValues.put(EmberContract.RelationEntry.COLUMN_CHILD_ID, child_id);

            cVVector.add(contentValues);
        }
        bulkInsert(EmberContract.RelationEntry.CONTENT_URI);
    }

    public void setProviderData() throws JSONException, ParseException {
        TAG = "setProviderData";
        cVVector.clear();
        String name_given;
        String provider_id;

        String json = dh.getProvider();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject provider = jsonArray.getJSONObject(i);

            name_given = provider.getString("name");
            provider_id = provider.getString("prescriber_id");

            ContentValues contentValues = new ContentValues();
            contentValues.put(EmberContract.ProviderEntry.COLUMN_NAME_GIVEN, name_given);
            contentValues.put(EmberContract.ProviderEntry._ID, provider_id);
            cVVector.add(contentValues);
        }
        bulkInsert(EmberContract.ProviderEntry.CONTENT_URI);
    }

}

