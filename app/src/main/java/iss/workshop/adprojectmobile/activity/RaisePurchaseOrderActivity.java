package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import iss.workshop.adprojectmobile.R;

public class RaisePurchaseOrderActivity extends AppCompatActivity implements View.OnClickListener{
TextView purchaseOrder;
Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_purchase_order);
        purchaseOrder=findViewById(R.id.purchaseOrder);
        nextBtn=findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        //Test string
        String poInfo="Deliver To : Logic University Stationery Store"+ System.lineSeparator()+"Order Date: 09/08/2020"+ System.lineSeparator()+"Supplier Legend: ";
        purchaseOrder.setText(poInfo);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, RaisePurchaseOrderSubmitActivity.class);
        startActivity(intent);
    }
}