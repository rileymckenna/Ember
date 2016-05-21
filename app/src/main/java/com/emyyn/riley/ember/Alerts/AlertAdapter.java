package com.emyyn.riley.ember.Alerts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emyyn.riley.ember.R;
import com.emyyn.riley.ember.Utility;
import com.emyyn.riley.ember.data.EmberContract;
import com.emyyn.riley.ember.medication.MedicationDetails;

import org.slf4j.helpers.Util;

import java.text.ParseException;

import static com.emyyn.riley.ember.Alerts.AlertActivity.AlertFragment.*;

/**
 * Created by riley on 5/17/2016.
 */
public class AlertAdapter extends CursorAdapter implements Animation.AnimationListener {
    private static final int VIEW_TYPE_FIRST = 0;
    private static final int VIEW_TYPE_REST = 1;

    public AlertAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    Context mContext;
    final static int _id = 0;
    final static int COLUMN_MEDICATION_ORDER_ID = 1;
    final static int medication_id = 2;
    final static int patient_id = 3;
    final static int prescriber_id = 4;
    final static int COLUMN_DISPENSE_SUPPLY_VALUE = 5;
    final static int date_written = 6;
    final static int COLUMN_DISPENSE_SUPPLY_UNIT = 7;
    final static int COLUMN_DS_CODE = 8;
    final static int COLUMN_DISPENSE_QUANTITY = 9;
    final static int COLUMN_VALID_START = 10;
    final static int COLUMN_VALID_END = 11;
    final static int COLUMN_DOSAGE_INSTRUCTIONS_TEXT = 12;
    final static int di_asneeded = 13;
    final static int d_route = 14;
    final static int di_method = 15;
    final static int COLUMN_DOSAGE_INSTRUCTIONS_FREQUENCY = 16;
    final static int COLUMN_DI_PERIOD = 17;
    final static int COLUMN_PERIOD_UNITS = 18;
    final static int di_start = 19;
    final static int di_end = 20;
    final static int di_dose_value = 21;
    final static int di_dose_code = 22;
    final static int COLUMN_LAST_UPDATED_AT = 23;
    final static int reason_given = 24;
    final static int COLUMN_STATUS = 25;
    final static int COLUMN_LAST_TAKEN = 26;
    final static int COLUMN_RUNNING_TOTAL = 27;
    final static int COLUMN_PRODUCT = 28;
    final static int COLUMN_NAME_GIVEN = 29;
    final static int COLUMN_NAME_FAMILY = 30;
    final static int COLUMN_NEXT_DOSE = 31;

    public final static String SNOOZE = "snooze";
    public final static String SKIP = "skip";
    public final static String TAKE = "take";


    private String running_total;
    private String patientName;
    private String prescription;
    private String instructions_text;
    private String dose_value;
    private String dose_code;
    private String max_quantity;
    private String timing_period;
    private String di_period_units;
    private String period;
    private String method;
    private String timing_frequency;
    private String child_id;
    private String parent_id;
    private String start;
    private String last;
    private String ds_value;   //total number of pills dispensed
    private String statuss;
    private String freq;
    private String next_dose;
    private View collapsable;
    private String pills;
    private String medication_order_id;

    private boolean isMin = false;
    private boolean isNearTime = false;

    Animation animSlideUp, animSlideDown;

    private void convertCursorRowToUXFormat(Cursor cursor) throws ParseException {

        //Log.i("Columns: " , Utility.queryColumns(Utility.getDashboardColumns()));
        patientName = cursor.getString(COLUMN_NAME_GIVEN);
        prescription = cursor.getString(COLUMN_PRODUCT);
        freq = cursor.getString(COLUMN_DI_PERIOD);
        instructions_text = cursor.getString(COLUMN_DOSAGE_INSTRUCTIONS_TEXT);
        start = cursor.getString(COLUMN_VALID_START);
        last = cursor.getString(COLUMN_LAST_TAKEN);
        statuss = cursor.getString(COLUMN_STATUS);
        di_period_units = cursor.getString(COLUMN_PERIOD_UNITS);
        running_total = cursor.getString(COLUMN_RUNNING_TOTAL);
        ds_value = cursor.getString(COLUMN_DISPENSE_SUPPLY_VALUE);
        // timing_period = cursor.getString(COLUMN_LAST_TAKEN);
        pills = instructions_text.substring(5, 6);
        medication_order_id = cursor.getString(COLUMN_MEDICATION_ORDER_ID);
        max_quantity = cursor.getString(COLUMN_DISPENSE_QUANTITY);
        next_dose = cursor.getString(COLUMN_NEXT_DOSE);

        try {
            if (last == null && next_dose == null) {
                int s = (Integer.parseInt(Utility.getNextAdministration(start)));
                if (s > 0) {
                    next_dose = String.valueOf(s);
                } else if (s < 0)
                    next_dose = "Past Due " + Math.abs(s);
                else {
                    int i = Utility.getNextDoseMin(start);
                    next_dose = String.valueOf(i) + " minutes";
                }
            } else if (next_dose == null) {
                next_dose = String.valueOf(Utility.getNextAdministration(last));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (1 > Integer.parseInt(Utility.getNextAdministration(next_dose))) {
            isNearTime = true;
            isMin = false;
            if (Integer.parseInt(Utility.getNextAdministration(next_dose)) <= 1 && Integer.parseInt(Utility.getNextAdministration(next_dose)) > 0) {
                isMin = true;
                int i = Utility.getNextDoseMin(start);
                next_dose = String.valueOf(i);
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_FIRST: {
                layoutId = R.layout.alert_first_item;
                break;
            }
            case VIEW_TYPE_REST: {
                layoutId = R.layout.alert_item;
                break;
            }
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return view;
    }

    public void toggle_contents(View v) {
        if (v.isShown()) {
            slide_up(mContext, v);
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
            slide_down(mContext, v);
        }
    }

    public void slide_down(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void slide_up(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            convertCursorRowToUXFormat(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView patient_name = null;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_FIRST: {
                collapsable = (View) view.findViewById(R.id.expand_activities_button);
                collapsable.setVisibility(View.VISIBLE);
                view.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle_contents(v.findViewById(R.id.expand_activities_button));
                    }
                });
                patient_name = (TextView) view.findViewById(R.id.child_name);
                break;
            }
            case VIEW_TYPE_REST: {
                collapsable = (View) view.findViewById(R.id.expand_activities_button);
                collapsable.setVisibility(View.GONE);
                view.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle_contents(v.findViewById(R.id.expand_activities_button));
                    }
                });
                break;
            }
        }


        //Log.i("Adpater", patientName);
        TextView medication, status, instructions, id, timing, skip, snooze, take, total, pill, period, alert_period_units;
        total = (TextView) view.findViewById(R.id.alert_total);
        medication = (TextView) view.findViewById(R.id.alert_name);
        status = (TextView) view.findViewById(R.id.alert_status);
        instructions = (TextView) view.findViewById(R.id.alert_text);
        alert_period_units = (TextView) view.findViewById(R.id.alert_period_units);
        timing = (TextView) view.findViewById(R.id.time_text);
        pill = (TextView) view.findViewById(R.id.alert_pills);
        id = (TextView) view.findViewById(R.id.alert_medication_id);
        period = (TextView) view.findViewById(R.id.alert_frequency);
        TextView alert_item_time = (TextView) view.findViewById(R.id.alert_item_time);
        alert_item_time.setText(Utility.getNextAdministration(next_dose));
        period.setText(freq);
        id.setText(medication_order_id);
        total.setText(running_total);
        pill.setText(pills);

        //OnLongClickCardlistener
        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, MedicationDetails.class).setData(EmberContract.MedicationOrderEntry.buildMedicationOrderUri(medication_order_id));
                mContext.startActivity(intent);
                return true;
            }
        });

        //Set Units
        if (isMin) {
            alert_period_units.setText(" min");
        } else {
            alert_period_units.setText(di_period_units);
        }

        //Onlclick Listeners for the skip snooze and take
        skip = (TextView) view.findViewById(R.id.alert_skip);
        snooze = (TextView) view.findViewById(R.id.alert_snooze);
        take = (TextView) view.findViewById(R.id.alert_take);
        ImageView check = (ImageView) view.findViewById(R.id.imageView2);
        if (isNearTime) {
            skip.setVisibility(View.VISIBLE);
            snooze.setVisibility(View.VISIBLE);
            take.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);

        } else {
            skip.setVisibility(View.INVISIBLE);
            snooze.setVisibility(View.INVISIBLE);
            take.setVisibility(View.INVISIBLE);
           check.setVisibility(View.INVISIBLE);
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    skipMedication(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    snoozeMedication(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takeMedication(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        ProgressBar details_refill_progress = (ProgressBar) view.findViewById(R.id.alerts_progress);
        details_refill_progress.setScaleY(1f);
        details_refill_progress.setMax(Integer.parseInt(max_quantity));
        if (patient_name != null) {
            patient_name.setText(patientName + "");
        }


        //details_dosage.setText("Take " + freq);

        status.setText(Utility.getStatus((statuss)));
        instructions.setText(instructions_text);
        medication.setText(prescription);

        timing.setText((next_dose));
        details_refill_progress.setProgress(Integer.parseInt(max_quantity) - Integer.parseInt(running_total));
    }


    private String getDoseText() {
        if (Integer.parseInt(instructions_text) > 1) {
            return " doses of ";
        } else {
            return " dose of ";
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == animSlideDown) {
            Toast.makeText(mContext, "Animation SlideDown Stopped",
                    Toast.LENGTH_SHORT).show();
        }
        if (animation == animSlideUp) {
            Toast.makeText(mContext, "Animation SlideUp Stopped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_FIRST : VIEW_TYPE_REST;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


}
