package com.emyyn.riley.ember.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.emyyn.riley.ember.data.EmberContract.CONTENT_AUTHORITY;
import static com.emyyn.riley.ember.data.EmberContract.MedicationAdministrationEntry;
import static com.emyyn.riley.ember.data.EmberContract.MedicationEntry;
import static com.emyyn.riley.ember.data.EmberContract.MedicationOrderEntry;
import static com.emyyn.riley.ember.data.EmberContract.PATH_MEDICATION;
import static com.emyyn.riley.ember.data.EmberContract.PATH_MEDICATIONORDER;
import static com.emyyn.riley.ember.data.EmberContract.PATH_PATIENT;
import static com.emyyn.riley.ember.data.EmberContract.PatientEntry;

/**
 * Created by Riley on 4/30/2016.
 */
public class EmberProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private EmberDbHelper mOpenHelper;
    private final String TAG = this.getClass().getSimpleName();


    static final int PATIENT = 100;
    static final int PATIENT_PROVIDER = 101;
    static final int PATIENT_MEDICATION_ORDER = 120;
    static final int PATIENT_MEDICATION_ADMINISTRATION = 150;
    static final int PROVIDER = 200;
    static final int MEDICATION = 300;
    static final int MEDICATION_ADMINISTRATION = 400;
    static final int MEDICATION_ORDER = 500;
    static final int MEDICATION_ORDER_PATIENT = 501;
    static final int MEDICATION_ORDER_MEDICATION = 502;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, PATH_PATIENT, PATIENT);
        matcher.addURI(authority, PATH_MEDICATION + "/*", MEDICATION);
        matcher.addURI(authority, PATH_MEDICATIONORDER, MEDICATION_ORDER);
        matcher.addURI(authority, PATH_MEDICATIONORDER + "/*", MEDICATION_ORDER);
        return matcher;
    }

    private static final SQLiteQueryBuilder sMedicationOrdersbyPatientSettingBuilder;

    static {
        sMedicationOrdersbyPatientSettingBuilder = new SQLiteQueryBuilder();
        sMedicationOrdersbyPatientSettingBuilder.setTables(
                MedicationOrderEntry.TABLE_NAME + " INNER JOIN " +
                        PatientEntry.TABLE_NAME +
                        " ON " + MedicationOrderEntry.TABLE_NAME + "." +
                        MedicationOrderEntry.COLUMN_PATIENT_KEY +
                        " = " + PatientEntry.TABLE_NAME + "." +
                        PatientEntry._ID
        );

    }

    private static final SQLiteQueryBuilder sMedicationbyMedicationOrderBuilder;

    static {
        sMedicationbyMedicationOrderBuilder = new SQLiteQueryBuilder();
        sMedicationbyMedicationOrderBuilder.setTables(
                MedicationOrderEntry.TABLE_NAME + " INNER JOIN " +
                        MedicationEntry.TABLE_NAME +
                        " ON " + MedicationOrderEntry.TABLE_NAME + "." +
                        MedicationOrderEntry.COLUMN_MED_KEY +
                        " = " + MedicationEntry.TABLE_NAME + "." +
                        MedicationEntry._ID
        );

    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new EmberDbHelper(getContext());
        Log.w("Create Helper", "Created");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        switch (sUriMatcher.match(uri)) {
            case PATIENT: {
                c = mOpenHelper.getReadableDatabase().query(PatientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MEDICATION_ORDER: {
                c = mOpenHelper.getReadableDatabase().query(MedicationOrderEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MEDICATION: {
                c = mOpenHelper.getReadableDatabase().query(MedicationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }case MEDICATION_ORDER_PATIENT: {
                c = sMedicationOrdersbyPatientSettingBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not able to query: " + uri);
        }

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PATIENT:
                return PatientEntry.CONTENT_TYPE;
            case MEDICATION:
                return MedicationEntry.CONTENT_TYPE;
            case MEDICATION_ORDER:
                return MedicationOrderEntry.CONTENT_TYPE;
            case MEDICATION_ADMINISTRATION:
                return MedicationAdministrationEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PATIENT: {
                long _id = db.insert(PatientEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = PatientEntry.buildPatientUri(values.get("patient_id").toString());
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MEDICATION_ORDER: {
                long _id = db.insert(MedicationOrderEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    Log.i("EmberProvider", values.get("medication_id").toString());
                    returnUri = MedicationOrderEntry.buildMedicationOrderUri(values.get("medication_id").toString());
                } else
                    throw new SQLException("Failed to insert row into" + uri);
                break;
            }
            case MEDICATION: {
                long _id = db.insert(MedicationEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MedicationEntry.buildMedicationUri(values.get("medication_id").toString());
                }
            }
            default:
                throw new SQLException("Unknown Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case PATIENT: {
                rowsDeleted = db.delete(PatientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MEDICATION_ORDER: {
                rowsDeleted = db.delete(MedicationOrderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        if (null == selection) selection = "1";
        switch (match) {
            case PATIENT: {
                rowsUpdated = db.update(PatientEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case MEDICATION_ORDER: {
                rowsUpdated = db.update(MedicationOrderEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PATIENT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PatientEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MEDICATION_ORDER: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MedicationOrderEntry.TABLE_NAME, null, value);
                        if (_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MEDICATION: {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MedicationEntry.TABLE_NAME, null, value);
                        if (_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }default:
                return super.bulkInsert(uri, values);
        }

    }
}


