package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import iss.workshop.adprojectmobile.R;

public class RaiseRequestActivity extends AppCompatActivity implements View.OnClickListener{
TextView requisitionForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_request);
        requisitionForm=findViewById(R.id.requisitionForm);
        String cInfo="Requisition Form: DDS/111/99"+ System.lineSeparator()+"Date: 09/08/2020";
        requisitionForm.setText(cInfo);
    }

    @Override
    public void onClick(View view) {

    }
}