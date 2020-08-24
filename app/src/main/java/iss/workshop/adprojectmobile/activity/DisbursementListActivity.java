package iss.workshop.adprojectmobile.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import iss.workshop.adprojectmobile.R;

public class DisbursementListActivity extends AppCompatActivity {
TextView collectionInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_list);
         collectionInfo =findViewById(R.id.collectionPointDisbursementInfo);

        //Test Data
        String cInfo="Collection Date: 09/08/2020"+ System.lineSeparator()+
                "Collection Time: 9:30am"+ System.lineSeparator()+"Delivered By: Store Clerk 1";
        collectionInfo.setText(cInfo);
    }
}
