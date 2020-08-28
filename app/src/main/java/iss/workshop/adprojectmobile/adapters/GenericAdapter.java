package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Store.InventoryCheckActivity;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;

public class GenericAdapter<T> extends ArrayAdapter<T> {
    private List<T> list;
    private List<T>filteredList;
    private Context context;
    private T titem;
    private TFilter tFilter;
    private List<Stationery> parentStationery;

    public GenericAdapter(@NonNull Context context, int resource, List Object) {
        super(context, resource,Object);
        this.list=Object;
        this.filteredList=Object;
        this.context=context;
        this.parentStationery = InventoryCheckActivity.getStationeries();
    }
    public View getView(final int pos, View view, @NonNull ViewGroup parent) {
        System.out.println("from adapter");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_discrepency_list_rows, null);

        TextView txtItemCode = view.findViewById(R.id.TxtItemCode);
        TextView txtDesc = view.findViewById(R.id.TxtDesc);
        TextView txtQty = view.findViewById(R.id.TxtQty);


        titem = filteredList.get(pos);
        if(titem instanceof StockAdjustmentDetail){


            StockAdjustmentDetail item=(StockAdjustmentDetail) titem;
            txtItemCode.setText(Integer.toString(item.getStationeryId()));
            txtDesc.setText(item.getStatus());
            txtQty.setText(Integer.toString(item.getDiscpQty()));
        }

        if(titem instanceof Stationery){
            if(titem instanceof Stationery){
                Stationery item = (Stationery)titem;
                txtItemCode.setText(Integer.toString(item.getId()));
                txtDesc.setText(item.getDesc());
                txtQty.setText(Integer.toString(item.getInventoryQty()));
            }


        }

        return view;
    }

    @Override
    public Filter getFilter() {
        if (tFilter == null) {
            tFilter = new GenericAdapter.TFilter();
        }

        return tFilter;
    }

    private class TFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<T> tempList = new ArrayList<T>();

                // search content in friend list
                for (T titem : list) {
                    if(titem instanceof StockAdjustmentDetail) {
                        StockAdjustmentDetail item = (StockAdjustmentDetail) titem;
                        if (item.getStatus().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            tempList.add(titem);
                        }
                    }
                        if(titem instanceof Stationery){
                            Stationery item = (Stationery) titem;
                            if (item.getDesc().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                tempList.add(titem);
                            }

                            }


                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = list.size();
                filterResults.values = list;
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
            filteredList = (ArrayList<T>) results.values;
            notifyDataSetChanged();
        }
    }
}


