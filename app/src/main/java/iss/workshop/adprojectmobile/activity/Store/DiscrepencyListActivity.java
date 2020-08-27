package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.adapters.DiscrepencyAdapter;
import iss.workshop.adprojectmobile.adapters.InventoryCheckAdapter;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;

public class DiscrepencyListActivity extends AppCompatActivity {
    private List<StockAdjustmentDetail> sads;
    private DiscrepencyAdapter<StockAdjustmentDetail> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discrepency_list);
        //sads = new ArrayList();
        Intent intent = getIntent();
        if(intent.getExtras()!=null){
           sads = (List<StockAdjustmentDetail>) getIntent().getSerializableExtra("sads");
            System.out.println("from discrepency"+sads);
        }

        adapter = new DiscrepencyAdapter<StockAdjustmentDetail>(this,R.layout.activity_discrepency_list_rows,sads);
        ListView listView = findViewById(R.id.dl_listView);

        if(listView!=null){
            listView.setAdapter(adapter);
        }
    }
}