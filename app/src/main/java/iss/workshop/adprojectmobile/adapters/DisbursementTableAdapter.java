package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.RequisitionDetail;

public class DisbursementTableAdapter extends ArrayAdapter {

    List<DisbursementList> DBL;
    Context context;

    @Override
    public int getCount() {
        return DBL.size();
    }

    public DisbursementTableAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.DBL = objects;
        this.context = context;

//        for (DisbursementList dl : DBL) {
//            add(null);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_disbursement_list_tablerow, null);

        TextView department = view.findViewById(R.id.dl_department);
        department.setText(DBL.get(pos).getDepartment());
        System.out.println("dept name: "+DBL.get(pos).getDepartment());

        TextView representative = view.findViewById(R.id.dl_representative);
        representative.setText(DBL.get(pos).getRepName());

        return view;
    }


}
