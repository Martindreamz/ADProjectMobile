package iss.workshop.adprojectmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.StationeryRetrievalActivity;
import iss.workshop.adprojectmobile.model.RequisitionDetail;

public class RetrievalItemDetailAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private final List<RequisitionDetail> RDlist2;
    private Context context;
    List<RequisitionDetail> currlist;
    private RequisitionDetail currRD;
    private List<Integer> countList;
    private HashMap<Integer, Integer> changes;


    @Override
    public int getCount() {
        return RDlist2.size();
    }

    public RetrievalItemDetailAdapter(@NonNull Context context, int id, @NonNull List objects) {
        super(context, id, objects);
        System.out.println(objects);
        countList = new ArrayList<>();
        currlist = new ArrayList<RequisitionDetail>();
        changes = StationeryRetrievalActivity.getChanges();
        this.RDlist2 = objects;
        this.context = context;

        for (RequisitionDetail rd : RDlist2) {
            countList.add(rd.getReqQty() - rd.getRcvQty());
        }
    }

    public View getView(final int pos, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_stationery_retrieval_item_detail, null);

        TextView requisitionID = view.findViewById(R.id.stationery_row_detail_id);
        requisitionID.setText(Integer.toString(RDlist2.get(pos).getRequisitionId()));

        TextView reqQty = view.findViewById(R.id.stationery_row_detail_reqQty);
        reqQty.setText(Integer.toString(countList.get(pos)));

        for (RequisitionDetail s : currlist) {
            if ((RDlist2.get(pos).getId() == s.getId()) && (RDlist2.get(pos).getStationeryId() == s.getStationeryId())) {
                currRD = s;
                break;
            }
        }
        final NumberPicker rtvQty = view.findViewById(R.id.stationery_row_detail_rtvQty);
        rtvQty.setValue(changes.get(RDlist2.get(pos).getId()));
        rtvQty.setMax(RDlist2.get(pos).getReqQty() - RDlist2.get(pos).getRcvQty());
        rtvQty.setMin(0);

        rtvQty.setValueChangedListener(new ValueChangedListener() {
                                           @Override
                                           public void valueChanged(int value, ActionEnum action) {

                                               changes.put(RDlist2.get(pos).getId(), value);
                                           }
                                       }
        );

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
