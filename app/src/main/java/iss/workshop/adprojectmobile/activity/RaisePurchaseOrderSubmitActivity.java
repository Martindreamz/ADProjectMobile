package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import iss.workshop.adprojectmobile.R;

public class RaisePurchaseOrderSubmitActivity extends AppCompatActivity {
    TextView purchaseOrderSubmit,poDetail, mainSupplier, supplier1, supplier2;
    Button saveBtn, cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_purchase_order_submit);
        purchaseOrderSubmit=findViewById(R.id.purchaseOrderSubmit);
        poDetail=findViewById(R.id.poDetail);

        //Test String
        String poInfo="Deliver To : Logic University Stationery Store"+ System.lineSeparator()+"Order Date: 09/08/2020"+ System.lineSeparator()+"Supplier Legend: ";
        purchaseOrderSubmit.setText(poInfo);
        String poDetailInfo="PO Number: 2000068"+ System.lineSeparator()+"Attn: Daryl Kuok";
        poDetail.setText(poDetailInfo);
    }
}