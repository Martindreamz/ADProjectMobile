package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Store.InventoryCheckActivity;
import iss.workshop.adprojectmobile.model.Stationery;

public class InventoryCheckAdapter extends ArrayAdapter implements Filterable {
    private final Context context;
    private List<Stationery> Stationeries;
    private List<Stationery> filteredList;
    private StationeryFilter stationeryFilter;
    private List<Stationery> parentStationery;
    Stationery s;

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    public InventoryCheckAdapter(@NonNull Context context, int id, @NonNull List objects) {
        super(context, id, objects);
        this.context = context;
        this.Stationeries = objects;
        this.filteredList = objects;
        this.parentStationery = InventoryCheckActivity.getStationeries();
    }

    public View getView(final int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_inventory_check_rows, null);


        TextView txtItemCode = view.findViewById(R.id.TxtItemCode);


        TextView txtDesc = view.findViewById(R.id.TxtDesc);


        TextView txtQty = view.findViewById(R.id.TxtQty);



            s = filteredList.get(pos);
            txtItemCode.setText(Integer.toString(s.getId()));
            txtDesc.setText(s.getDesc());
            txtQty.setText(Integer.toString(s.getInventoryQty()));

            final NumberPicker qty = view.findViewById(R.id.NPInv);
            qty.setValue(s.getReOrderQty());
            qty.setMin(0);
            qty.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    for(Stationery stationery:parentStationery){
                        if(stationery.getId()==filteredList.get(pos).getId()){
                            stationery.setReOrderQty(value);
                            System.out.println(stationery);
                        }
                    }
                }

            });



        return view;
    }

    @Override
    public Filter getFilter() {
        if (stationeryFilter == null) {
            stationeryFilter = new StationeryFilter();
        }

        return stationeryFilter;
    }

    private class StationeryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Stationery> tempList = new ArrayList<Stationery>();

                // search content in friend list
                for (Stationery stationery : Stationeries) {
                    if (stationery.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(stationery);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = Stationeries.size();
                filterResults.values = Stationeries;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         *
         * @param constraint text
         * @param results    filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Stationery>) results.values;
            notifyDataSetChanged();
        }
    }
}
