package com.emyyn.riley.ember.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.emyyn.riley.ember.data.EmberContract.*;
import static com.emyyn.riley.ember.data.EmberContract.MedicationEntry;
import static com.emyyn.riley.ember.data.EmberContract.PatientEntry;

/**
 * Created by Riley on 4/30/2016.
 */
public class EmberDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 19;
    static final String DATABASE_NAME = "ember.db";

    private static final String TEXT = " TEXT, ";
    private static final String REAL = " REAL, ";
    private static final String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    private static final String DOUBLE = " DOUBLE, ";
    private static final String INTEGER = " INTEGER, ";
    private static final String OPEN = " (";
    private static final String CLOSE = " );";

    public EmberDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PATIENT_TABLE = "CREATE TABLE " + PatientEntry.TABLE_NAME + OPEN +
                PatientEntry._ID + PRIMARY_KEY +
                PatientEntry.COLUMN_PATIENT_ID + TEXT +
                //Name
                PatientEntry.COLUMN_NAME_FAMILY + TEXT +
                PatientEntry.COLUMN_NAME_GIVEN + TEXT +
                PatientEntry.COLUMN_ACTIVE + TEXT +
                //Address
                PatientEntry.COLUMN_ADDRESS + TEXT +
                PatientEntry.COLUMN_CITY + TEXT +
                PatientEntry.COLUMN_STATE + TEXT +
                PatientEntry.COLUMN_ZIP + INTEGER +
                //Characteristics
                PatientEntry.COLUMN_DOB + TEXT +
                PatientEntry.COLUMN_ETHINICITY + TEXT +
                PatientEntry.COLUMN_GENDER + TEXT +
                PatientEntry.COLUMN_LANGUAGE + TEXT +
                PatientEntry.COLUMN_MARRIED_STATUS + TEXT +
                PatientEntry.COLUMN_RACE + TEXT +
                //Foreign Keys
                PatientEntry.COLUMN_PROVIDER_KEY + TEXT +

                " FOREIGN KEY (" + PatientEntry.COLUMN_PROVIDER_KEY + ") REFERENCES " +
                ProviderEntry.TABLE_NAME + " (" + ProviderEntry._ID + "), " +
                " UNIQUE (" + PatientEntry.COLUMN_PATIENT_ID + ") ON CONFLICT REPLACE" + CLOSE;
        Log.i("CREATE_Patient", SQL_CREATE_PATIENT_TABLE);


        final String SQL_CREATE_MEDICATION_TABLE = "CREATE TABLE " + MedicationEntry.TABLE_NAME + OPEN +
                MedicationEntry._ID + PRIMARY_KEY +
                MedicationEntry.COLUMN_DISPLAY + TEXT +
                MedicationEntry.COLUMN_PRODUCT + TEXT +
                MedicationEntry.COLUMN_CODE_TEXT + TEXT +

                " UNIQUE (" + MedicationEntry._ID + ") ON CONFLICT REPLACE" +
                CLOSE;
        Log.i("CREATE_MEDICATION", SQL_CREATE_MEDICATION_TABLE);

        final String SQL_CREATE_RELATION_TABLE = "CREATE TABLE " + RelationEntry.TABLE_NAME + OPEN +
                EmberContract.RelationEntry._ID + PRIMARY_KEY +
                EmberContract.RelationEntry.COLUMN_PATIENT_ID + TEXT +
                EmberContract.RelationEntry.COLUMN_CHILD_ID + TEXT +
                " FOREIGN KEY (" + EmberContract.RelationEntry.COLUMN_PATIENT_ID + ") REFERENCES " +
                PatientEntry.TABLE_NAME + " (" + PatientEntry.COLUMN_PATIENT_ID + "), " +
                " UNIQUE (" + EmberContract.RelationEntry.COLUMN_PATIENT_ID + ", " +
                EmberContract.RelationEntry.COLUMN_CHILD_ID + ") ON CONFLICT REPLACE" +
                CLOSE;
        Log.i("CREATE_RELATIONS", SQL_CREATE_MEDICATION_TABLE);

        final String SQL_CREATE_MEDICATION_ORDER_TABLE = "CREATE TABLE " + MedicationOrderEntry.TABLE_NAME + OPEN +
                MedicationOrderEntry._ID + PRIMARY_KEY +
                MedicationOrderEntry.COLUMN_MED_KEY + INTEGER +
                MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID + INTEGER +
                MedicationOrderEntry.COLUMN_PATIENT_KEY + INTEGER +
                MedicationOrderEntry.COLUMN_PRESCRIBER_KEY + INTEGER +
                MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_VALUE + INTEGER +
                MedicationOrderEntry.COLUMN_DATE_WRITTEN + TEXT +
                MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_UNIT + TEXT +
                MedicationOrderEntry.COLUMN_DISPENSE_SUPPLY_CODE + TEXT +
                MedicationOrderEntry.COLUMN_DISPENSE_QUANTITY + INTEGER +
                MedicationOrderEntry.COLUMN_VALID_START + TEXT +
                MedicationOrderEntry.COLUMN_VALID_END + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TEXT + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_ROUTE + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_METHOD + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY + DOUBLE +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD + INTEGER +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END + TEXT +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE + INTEGER +
                MedicationOrderEntry.COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE + TEXT +
                MedicationOrderEntry.COLUMN_LAST_UPDATED_AT + INTEGER +
                MedicationOrderEntry.COLUMN_REASON_GIVEN + TEXT +
                MedicationOrderEntry.COLUMN_STATUS + TEXT +
                MedicationOrderEntry.COLUMN_LAST_TAKEN + TEXT +
                MedicationOrderEntry.COLUMN_RUNNING_TOTAL + TEXT +

                " FOREIGN KEY (" + MedicationOrderEntry.COLUMN_MED_KEY + ") REFERENCES " +
                MedicationEntry.TABLE_NAME + " (" + MedicationEntry._ID + "), " +
                " FOREIGN KEY (" + MedicationOrderEntry.COLUMN_PATIENT_KEY + ") REFERENCES " +
                PatientEntry.TABLE_NAME + " (" + PatientEntry._ID + "), " +
                " FOREIGN KEY (" + MedicationOrderEntry.COLUMN_PRESCRIBER_KEY + ") REFERENCES " +
                ProviderEntry.TABLE_NAME + " (" + ProviderEntry._ID + "), " +
                " UNIQUE (" + MedicationOrderEntry.COLUMN_MEDICATION_ORDER_ID+ ") ON CONFLICT REPLACE" +
                CLOSE;
        Log.i("CREATE_MEDICATION_ORDER", SQL_CREATE_MEDICATION_ORDER_TABLE);

        final String SQL_CREATE_MEDICATION_ADMINISTRATION_TABLE = "CREATE TABLE " + MedicationAdministrationEntry.TABLE_NAME + OPEN +
                MedicationAdministrationEntry._ID + PRIMARY_KEY +
                //foreign keys
                MedicationAdministrationEntry.COLUMN_MED_KEY + INTEGER +
                MedicationAdministrationEntry.COLUMN_MED_ORDER_KEY + INTEGER +
                MedicationAdministrationEntry.COLUMN_PATIENT_KEY + INTEGER +
                MedicationAdministrationEntry.COLUMN_PRESCRIBER_KEY + INTEGER +

                MedicationAdministrationEntry.COLUMN_REASON_GIVEN + TEXT +
                MedicationAdministrationEntry.COLUMN_SITE + TEXT +
                MedicationAdministrationEntry.COLUMN_TIME_ADMINISTERED + TEXT +
                MedicationAdministrationEntry.COLUMN_DOSAGE_ROUTE + TEXT +
                MedicationAdministrationEntry.COLUMN_DOSAGE_METHOD + TEXT +
                MedicationAdministrationEntry.COLUMN_DOSAGE_RATE_RATIO + TEXT +
                MedicationAdministrationEntry.COLUMN_DOSAGE_RATE_RANGE + TEXT +

                " FOREIGN KEY (" + MedicationAdministrationEntry.COLUMN_MED_KEY + ") REFERENCES " +
                MedicationEntry.TABLE_NAME + " (" + MedicationEntry._ID + "), " +
                " FOREIGN KEY (" + MedicationAdministrationEntry.COLUMN_PATIENT_KEY + ") REFERENCES " +
                PatientEntry.TABLE_NAME + " (" + PatientEntry._ID + "), " +
                " FOREIGN KEY (" + MedicationAdministrationEntry.COLUMN_PRESCRIBER_KEY + ") REFERENCES " +
                ProviderEntry.TABLE_NAME + " (" + ProviderEntry._ID + "), " +
                " FOREIGN KEY (" + MedicationAdministrationEntry.COLUMN_MED_ORDER_KEY + ") REFERENCES " +
                MedicationOrderEntry.TABLE_NAME + " (" + MedicationOrderEntry._ID + "), " +
                " UNIQUE (" + MedicationAdministrationEntry._ID + ") ON CONFLICT REPLACE" +
                CLOSE;
        Log.i("CREATE_MEDICATION_Admin", SQL_CREATE_MEDICATION_ADMINISTRATION_TABLE);

        final String SQL_CREATE_PROVIDER_TABLE = "CREATE TABLE " + ProviderEntry.TABLE_NAME + OPEN +
                ProviderEntry._ID + PRIMARY_KEY +
                //Name
                ProviderEntry.COLUMN_NAME_FAMILY + TEXT +
                ProviderEntry.COLUMN_NAME_GIVEN + TEXT +
                ProviderEntry.COLUMN_ACTIVE + TEXT +
                //Contact
                ProviderEntry.COLUMN_ADDRESS + TEXT +
                ProviderEntry.COLUMN_CITY + TEXT +
                ProviderEntry.COLUMN_STATE + TEXT +
                ProviderEntry.COLUMN_ZIP + INTEGER +
                ProviderEntry.COLUMN_TELE + TEXT +
                //Characteristics
                ProviderEntry.COLUMN_DOB + TEXT +
                ProviderEntry.COLUMN_ETHINICITY + TEXT +
                ProviderEntry.COLUMN_GENDER + TEXT +
                ProviderEntry.COLUMN_LANGUAGE + TEXT +
                ProviderEntry.COLUMN_RACE + TEXT +

                //Roles
                ProviderEntry.COLUMN_ROLE + TEXT +
                ProviderEntry.COLUMN_SPECIALTY + TEXT +

                " UNIQUE (" + ProviderEntry._ID + ") ON CONFLICT REPLACE" + CLOSE;
        Log.i("CREATE_PROVIDER", SQL_CREATE_PROVIDER_TABLE);

        db.execSQL(SQL_CREATE_MEDICATION_ADMINISTRATION_TABLE);
        db.execSQL(SQL_CREATE_MEDICATION_ORDER_TABLE);
        db.execSQL(SQL_CREATE_MEDICATION_TABLE);
        db.execSQL(SQL_CREATE_PATIENT_TABLE);
        db.execSQL(SQL_CREATE_PROVIDER_TABLE);
        db.execSQL(SQL_CREATE_RELATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProviderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationOrderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationAdministrationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RelationEntry.TABLE_NAME);
        onCreate(db);
    }
}
