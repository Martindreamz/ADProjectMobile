package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import iss.workshop.adprojectmobile.R;

public class ReceiveGoodsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
Spinner supplier, poNoRef;
TextView rcvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods);
        supplier=findViewById(R.id.supplier);
        poNoRef=findViewById(R.id.PoNumberRef);
        rcvDate=findViewById(R.id.rcvDate);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        rcvDate.setText(formattedDate);

        supplier.setOnItemSelectedListener(this);
        //Test Data for supplier
        List<String> suppliers = new ArrayList<String>();
        suppliers.add("Main Supplier");
        suppliers.add("Supplier 2");
        suppliers.add("Supplier 3");

        //Test Data for PO Number Ref
        List<String> refNos=new ArrayList<String>();
        refNos.add("098665");
        refNos.add("077665");
        refNos.add("055665");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, suppliers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, refNos);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplier.setAdapter(dataAdapter);
        poNoRef.setAdapter(dataAdapter2);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(ReceiveGoodsActivity.this,item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}