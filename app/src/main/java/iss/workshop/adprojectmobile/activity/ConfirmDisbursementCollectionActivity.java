package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import iss.workshop.adprojectmobile.R;

public class ConfirmDisbursementCollectionActivity extends AppCompatActivity {
TextView collectionInfo;
Button completeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement_collection);
        collectionInfo=findViewById(R.id.collectionInfo);
        completeBtn=findViewById(R.id.completeBtn);

        //Test Data
        String cInfo="Collection Point: Science Department"+ System.lineSeparator()+"Collection Date: 09/08/2020"+ System.lineSeparator()+"Collection Time: 9:30 AM"+ System.lineSeparator()+"Status: Delivered";
        collectionInfo.setText(cInfo);
    }
}