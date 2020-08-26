package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Store.StationeryRetrievalActivity;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;


public class RetrievalAdapter extends ArrayAdapter {

    private List<RequisitionDetail> RDlist;

    private Context context;
    private Set<Integer> itemcodesSet;
    private List<Integer> itemcodesList;
    private HashMap<Integer, List<RequisitionDetail>> splittedRequistionDetail;
    private List<Integer> keylist;
    private List<Stationery> stationeries;
    private List<String> itemname;
    private List<Integer> remainingcount;


    public List<RequisitionDetail> getRDlist() {
        return RDlist;
    }

    public void setRDlist(List<RequisitionDetail> RDlist) {
        this.RDlist = RDlist;
    }

    @Override
    public int getCount() {
        return itemcodesSet.size();
    }

    public RetrievalAdapter(@NonNull Context context, int id, @NonNull List objects) {
        super(context, id, objects);

        stationeries = StationeryRetrievalActivity.getStationeries();
        itemcodesSet = new HashSet<>();
        itemcodesList = new ArrayList<>();
        splittedRequistionDetail = new HashMap<>();
        keylist = new ArrayList<>();
        itemname = new ArrayList<>();
        remainingcount = new ArrayList<>();


        this.RDlist = objects;
        this.context = context;

        for (RequisitionDetail rd : RDlist) {
            itemcodesSet.add(rd.getStationeryId());


            if (splittedRequistionDetail.get(rd.getStationeryId()) == null) {
                List<RequisitionDetail> rdl = new ArrayList<RequisitionDetail>();
                rdl.add(rd);
                splittedRequistionDetail.put(rd.getStationeryId(), rdl);
            } else {
                List<RequisitionDetail> rdl = splittedRequistionDetail.get(rd.getStationeryId());
                rdl.add(rd);
                splittedRequistionDetail.put(rd.getStationeryId(), rdl);
            }
        }


        for (int i : splittedRequistionDetail.keySet()) {
            keylist.add(i);
            itemname.add(splittedRequistionDetail.get(i).get(0).getStationery());
        }


        for (int i : itemcodesSet) {
            add(null);
            itemcodesList.add(i);
        }

        for (int i : itemcodesList) {
            for (Stationery s : stationeries) {
                if (i == s.getId()) {
                    remainingcount.add(s.getInventoryQty());
                }
            }
        }


    }

    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_stationery_retrieval_item, null);

        TextView rownumber = view.findViewById(R.id.stationery_row_number);
        rownumber.setText(Integer.toString(pos));

        TextView description = view.findViewById(R.id.stationery_row_description);
        description.setText(itemname.get(pos) + " ("+remainingcount.get(pos)+")");

        int key = keylist.get(pos);
        List<RequisitionDetail> list = splittedRequistionDetail.get(key);
        RetrievalItemDetailAdapter adapter = new RetrievalItemDetailAdapter(context, R.layout.activity_stationery_retrieval_item_detail, list);
        ListView requistionList = view.findViewById(R.id.stationery_row_detail);
        if (requistionList != null) {
            requistionList.setAdapter(adapter);
            requistionList.setOnItemClickListener(requistionList.getOnItemClickListener());
            getTotalHeightofListView(requistionList);


        }

        return view;
    }

    public static void getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}


