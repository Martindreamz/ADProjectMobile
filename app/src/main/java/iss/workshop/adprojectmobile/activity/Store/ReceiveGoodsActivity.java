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

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.PurchaseOrder;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.Supplier;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiveGoodsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,View.OnClickListener{
Spinner supplier, poNoRef;
TextView rcvDate;
List<Supplier> suppliers;
List<String> supplierddl;
List<Stationery> filteredList; 
List<Stationery> allStationery;
List<PurchaseOrder> purchaseOrders;
List<Integer>poddl;

    public void setSuppliers(List<Supplier>suppliers){
        this.suppliers=suppliers;
    }

    public void setPurchaseOrders(List<PurchaseOrder>purchaseOrders){
        this.purchaseOrders=purchaseOrders;
    }

    public void setAllStationery(List<Stationery> allStationery) {
        this.allStationery = allStationery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods);
        supplier=findViewById(R.id.supplier);
        poNoRef=findViewById(R.id.PoNumberRef);
        rcvDate=findViewById(R.id.rcvDate);

        suppliers=new ArrayList<>();
        purchaseOrders=new ArrayList<>();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        rcvDate.setText(formattedDate);

        supplier.setOnItemSelectedListener(this);
        //Test Data for supplier

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        //get all suppliers
        Call<List<Supplier>> call = apiInterface.GetAllSuppliers();
        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                System.out.println("Supplier "+response.code()+response.body());
                if(response.code()==200){
                    suppliers =response.body();
                    System.out.println("suppliers"+suppliers);
                    setSuppliers(suppliers);

                    supplierddl= new ArrayList<String>();

                    if(suppliers!=null){
                        for(Supplier supplier:suppliers) {
                            supplierddl.add(supplier.getName());
                        }        }
                    else{System.out.println("HELP Lah");}

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, supplierddl);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    supplier.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "System down please try again later",Toast.LENGTH_LONG);

            }
        });

        //get all POs
        Call<List<PurchaseOrder>> callPo = apiInterface.GetAllPos();
        callPo.enqueue(new Callback<List<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<List<PurchaseOrder>> callPo, Response<List<PurchaseOrder>> poRes) {
                System.out.println("PO "+poRes.code()+poRes.body());
                if(poRes.code()==200){
                    purchaseOrders =poRes.body();
                    setPurchaseOrders(purchaseOrders);

                    poddl = new ArrayList<Integer>();

                    for(PurchaseOrder po:purchaseOrders){
                        if(po.getStatus()=="ordered") {
                            poddl.add(po.getId());
                        }
                    }

                    ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_item, poddl);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    poNoRef.setAdapter(dataAdapter2);
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "System down please try again later",Toast.LENGTH_LONG);

            }
        });

        //get all Stationery
        Call<List<Stationery>> callStn = apiInterface.getAllStationery();
        callStn.enqueue(new Callback<List<Stationery>>() {
            @Override
            public void onResponse(Call<List<Stationery>> callStn, Response<List<Stationery>> stationeryRes) {
                System.out.println("Stationery get " + stationeryRes.code() + stationeryRes.body());
                if (stationeryRes.code() == 200) {
                    allStationery = stationeryRes.body();
                    setAllStationery(allStationery);
                }
            }

            @Override
            public void onFailure(Call<List<Stationery>> callStn, Throwable t) {
                Toast.makeText(getApplicationContext(), "System down please try again later",Toast.LENGTH_LONG);

            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int spinnerId = view.getId();

        if(spinnerId==R.id.PoNumberRef){
            int poNum = Integer.parseInt(parent.getItemAtPosition(position).toString());
            for(PurchaseOrder po :purchaseOrders){
                if(po.getId()==poNum){
                    //pass po details to adapter as stationery? 
                    //set supplier 
                    
                }
            }

        }

        if(spinnerId==R.id.supplier) {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(ReceiveGoodsActivity.this, item, Toast.LENGTH_SHORT).show();
            //get supplier id 
            //set po reference to only supplier's 
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.rcvGoodsSaveBtn){
            //create stock adjustment detail and send back to controller
        }
    }
}