package com.emyyn.riley.ember.hidden;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emyyn.riley.ember.MedicationDetails;
import com.emyyn.riley.ember.R;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Patient;

/**
 * Created by Riley on 4/20/2016.
 */
public class MyArrayAdapter extends RecyclerView.Adapter<MyArrayAdapter.ViewHolder> {

    private LayoutInflater mInflator;
    private List<MedicationOrder> orders = new ArrayList<>();
    private Patient patient = null;
    private ArrayList<Medication> medications;
    private Context context;

    public MyArrayAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MedicationOrder> list, Patient p, ArrayList<Medication> medicationName) {
        this.patient = p;
        this.medications = medicationName;
        this.orders = list;
        notifyDataSetChanged();
    }

    @Override
    public MyArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicationOrder order = orders.get(position);
        StringBuilder b = new StringBuilder("Medication Order Identifier");
        try {
            for (IdentifierDt i: order.getIdentifier()){
                if (i.isEmpty()){
                    continue;
                }
                b.append(i.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.i("No Medication Name", notNull(m.getCode().getCoding().get(0).getDisplay()));
        try {
            //Log.i("Index of Medication ", Integer.toString(order.getMedication().toString().indexOf("Medication")));
            int iMedication = order.getMedication().toString().indexOf("Medication")+11;

            int endMedication = order.getMedication().toString().length()-14;
            //Log.i("End Length", Integer.toString(endMedication));

            String prescriptionName = order.getMedication().toString().substring(iMedication, endMedication);
            Log.i("Prescription Name" , prescriptionName );
            for (Medication p : medications){
                Log.i("Prescription Name" , p.getId().getIdPart().toString() );
                Log.i("Ids", p.getId().getIdPart() + " getId " + prescriptionName);
                if (p.getId().getIdPart().toString().equalsIgnoreCase(prescriptionName)){
                    Log.i("Code", p.getCode().getText() + "  " + p.getCode().getCoding().get(0).getCode());
                    holder.name.setText(p.getCode().getCoding().get(0).getCode());
                } else if (p.getCode().getText() != null){
                    holder.name.setText(p.getCode().getText());
                } else{
                    Log.i("No Medication Name", notNull(p.getCode().getCoding().get(0).getDisplay()));
                }
            }
            if (order.getMedication().toString() != null) {
                holder.name.setText(order.getMedication().toString().substring(iMedication, endMedication));
            }

            //holder.appointment.setText(b.toString().trim());
            holder.dose.setText( order.getDosageInstructionFirstRep().getText());
            holder.refill.setText(order.getDispenseRequest().getQuantity().getValue().toString()+ "/"+ order.getDispenseRequest().getQuantity().getValue().toString());
            holder.timeTillNextMedication.setText(order.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod().toString() + order.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriodUnits().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //holder.dose.getText(Html.fromHtml(patient.getText().getDiv().getValueAsString()));
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView dose;
        public TextView appointment;
        public TextView refill;
        public TextView timeTillNextMedication;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.dashboard_name);
            dose = (TextView) itemView.findViewById(R.id.details_dosage);
            appointment = (TextView) itemView.findViewById(R.id.details_extra);
            refill = (TextView) itemView.findViewById(R.id.details_refills_tv);
            timeTillNextMedication = (TextView) itemView.findViewById(R.id.details_dose_tv);
        }

        @Override
        public void onClick(View v) {
            Log.i("Position of View", getPosition() + " ");
            Intent intent = new Intent(context, MedicationDetails.class);
            context.startActivity(intent);
        }
    }
    public String notNull(String s) {
        if (s != null) {
            Log.i("Title" ,s);
            return s;
        } else {
            Log.i("Null", "nothing");
            return "nothing";
        }
    }
}
