package com.emyyn.riley.ember.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by Riley on 4/30/2016.
 */
public class EmberContract {
    private final String TAG = this.getClass().getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.emyyn.riley.ember.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PATIENT = "patient";
    public static final String PATH_MEDICATIONADMINISTRATION = "medicationadministration";
    public static final String PATH_MEDICATION = "medication";
    public static final String PATH_MEDICATIONORDER = "medicationorder";
    public static final String PATH_REMINDERS = "reminders";
    public static final String PATH_PROVIDER = "provider";
    public static final String PATH_RELATIONS = "relations";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    //medication table
    public static final class MedicationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATION;

        //Table Name
        public static final String TABLE_NAME = "medication";

        public static final String COLUMN_CODE_TEXT = "code_text";
        public static final String COLUMN_DISPLAY = "display";
        public static final String COLUMN_PRODUCT = "product_text";

        public static Uri buildMedicationUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }


    }

    public static final class MedicationOrderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATIONORDER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATIONORDER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATIONORDER;

        //Table Name
        public static final String TABLE_NAME = "medication_order";
        public static final String COLUMN_MEDICATION_ORDER_ID = "medication_order_id";
        public static final String COLUMN_MED_KEY = "medication_id";
        public static final String COLUMN_PATIENT_KEY = "patient_id";
        public static final String COLUMN_PRESCRIBER_KEY = "prescriber_id";
        public static final String COLUMN_DISPENSE_SUPPLY_VALUE = "ds_value";
        public static final String COLUMN_DATE_WRITTEN = "date_written";
        public static final String COLUMN_DISPENSE_SUPPLY_UNIT = "ds_unit";
        public static final String COLUMN_DISPENSE_SUPPLY_CODE = "ds_code";
        public static final String COLUMN_DISPENSE_QUANTITY = "d_quantity";
        public static final String COLUMN_VALID_START = "start_date";
        public static final String COLUMN_VALID_END = " end_date";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TEXT = "di_text";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_ASNEEDED = "di_asneeded";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_ROUTE = "d_route";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_METHOD = "di_method";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TIMING_FREQUENCY = "di_frequency";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD = "di_period";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TIMING_PERIOD_UNITS = "di_period_units";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TIMING_START = "di_start";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_TIMING_END = "di_end";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_DOSE_VALUE = "di_dose_value";
        public static final String COLUMN_DOSAGE_INSTRUCTIONS_DOSE_CODE = "di_dose_code";
        public static final String COLUMN_LAST_UPDATED_AT = "last_updated";
        public static final String COLUMN_REASON_GIVEN = "reason_given";
        public static final String COLUMN_STATUS = "status" ;
        public static final String COLUMN_LAST_TAKEN = "last_taken" ;
        public static final String COLUMN_RUNNING_TOTAL = "running_total";

        public static Uri buildMedicationOrderUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        //content://com.emyyn.riley.ember.app/medicationorder/patient/48
        public static Uri buildMedicationOrderWithPatientId(String patient) {
            return CONTENT_URI.buildUpon().appendPath("patient").appendPath(patient).build();
        }


        public static Uri buildMedicationOrderUri(Long medicationOrderId) {
            return ContentUris.withAppendedId(CONTENT_URI, medicationOrderId);
        }

        public static String getPatientId(Uri uri) {
            return uri.getPathSegments().get(2);
        }
        public static String getMedicationOrderId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    //Patient
    public static final class PatientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATIENT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;

        //Table Name
        public static final String TABLE_NAME = "patient";
        public static final String COLUMN_PROVIDER_KEY = "provider_id";
        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_NAME_FAMILY = "last_name";
        public static final String COLUMN_NAME_GIVEN = "first_name";
        public static final String COLUMN_ADDRESS = " street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip_code";
        public static final String COLUMN_RACE = "race";
        public static final String COLUMN_ETHINICITY = "ethic";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_MARRIED_STATUS = "marital_status";

        public String[] getColumns() {
            String[] str = {
                    TABLE_NAME + "." + _ID,
                    COLUMN_PROVIDER_KEY,
                    COLUMN_DOB,
                    COLUMN_GENDER,
                    COLUMN_ACTIVE,
                    COLUMN_PATIENT_ID,
                    COLUMN_NAME_FAMILY,
                    COLUMN_NAME_GIVEN,
                    COLUMN_ADDRESS,
                    COLUMN_CITY,
                    COLUMN_STATE,
                    COLUMN_ZIP,
                    COLUMN_RACE,
                    COLUMN_ETHINICITY,
                    COLUMN_LANGUAGE,
                    COLUMN_MARRIED_STATUS

            };
            return str;
        }

        public static Uri buildPatientUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static final class MedicationAdministrationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICATIONADMINISTRATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATIONADMINISTRATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICATIONADMINISTRATION;

        //Table Name
        public static final String TABLE_NAME = "medication_administration";

        public static final String COLUMN_MED_KEY = "medication_id";
        public static final String COLUMN_MED_ORDER_KEY = "medication_order_id";
        public static final String COLUMN_PATIENT_KEY = "patient_id";
        public static final String COLUMN_PRESCRIBER_KEY = "prescriber_id";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_REASON_GIVEN = "reason_given";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_TIME_ADMINISTERED = "time_administered";
        public static final String COLUMN_DOSAGE_ROUTE = "d_route";
        public static final String COLUMN_DOSAGE_METHOD = "di_method";
        public static final String COLUMN_DOSAGE_RATE_RATIO = "rate_ratio";
        public static final String COLUMN_DOSAGE_RATE_RANGE = "rate_range";

        public static Uri buildMedicationAdministrationUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }


    }

    //Provider
    public static final class ProviderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROVIDER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROVIDER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROVIDER;

        //Table Name
        public static final String TABLE_NAME = "provider";
        public static final String COLUMN_PATIENT_ID = "patient_id";

        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_ACTIVE = "active";

        public static final String COLUMN_NAME_FAMILY = "last_name";
        public static final String COLUMN_NAME_GIVEN = "first_name";
        public static final String COLUMN_ADDRESS = " street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip_code";
        public static final String COLUMN_TELE = "telephone";

        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_SPECIALTY = "specialty";

        public static final String COLUMN_RACE = "race";
        public static final String COLUMN_ETHINICITY = "ethic";
        public static final String COLUMN_LANGUAGE = "language";


        public static Uri buildProviderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static class RelationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONS).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONS;

        //Table Name
        public static final String TABLE_NAME = "relation";
        public static final String COLUMN_PATIENT_ID = "patient_id";
        public static final String COLUMN_CHILD_ID = "child_id";

        public static Uri buildProviderUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://com.emyyn.riley.ember.app/relation/patient/48
        public static Uri buildFamilyUri(String parent) {
            return CONTENT_URI.buildUpon().appendPath("patient").appendPath(parent).build();
        }

        public static String getParentId(Uri uri) {
            Log.i("GetPatientId", uri.getPathSegments().get(2).toString());
            return uri.getPathSegments().get(2);
        }
    }
}
