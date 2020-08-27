package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.DisbursementDetail;

public class DisbursementDetailAdapter extends ArrayAdapter {

    List<DisbursementDetail> DBlist;
    Context context;

    @Override
    public int getCount() {
        return DBlist.size();
    }
    
    public DisbursementDetailAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        
        this.context = context;
        this.DBlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_disbursement_details_rows, null);

        TextView itemCode = view.findViewById(R.id.itemcodeDD);
        itemCode.setText(DBlist.get(position).getRequisitionDetail());

        TextView description = view.findViewById(R.id.descDD);
        description.setText(DBlist.get(position).getDisbursementList());

        TextView plannedQty = view.findViewById(R.id.plannedDD);
        plannedQty.setText(Integer.toString(DBlist.get(position).getQty()));

        return view;
    }
}
