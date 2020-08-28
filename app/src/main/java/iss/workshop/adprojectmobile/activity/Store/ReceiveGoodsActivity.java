package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import iss.workshop.adprojectmobile.adapters.GenericAdapter;
import iss.workshop.adprojectmobile.model.PurchaseOrder;
import iss.workshop.adprojectmobile.model.PurchaseOrderDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.StockAdjustment;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;
import iss.workshop.adprojectmobile.model.Supplier;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiveGoodsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner supplier, poNoRef;
    TextView rcvDate;
    List<Supplier> suppliers;
    List<String> supplierddl;
    List<Stationery> filteredList;
    List<Stationery> allStationery;
    public static List<Stationery> selectedStationery;
    private SharedPreferences session;
    List<PurchaseOrder> purchaseOrders;
    List<String> poddl;
    List<PurchaseOrderDetail> pods=new ArrayList<PurchaseOrderDetail>();
    Button BtnRecive;

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public void setAllStationery(List<Stationery> allStationery) {
        this.allStationery = allStationery;
    }

    public static List<Stationery> getSelectedStationery() {
        return selectedStationery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods);
        supplier = findViewById(R.id.supplier);
        poNoRef = findViewById(R.id.PoNumberRef);
        rcvDate = findViewById(R.id.rcvDate);
        suppliers = new ArrayList<>();
        purchaseOrders = new ArrayList<>();
        BtnRecive = findViewById(R.id.rcvGoodsSaveBtn);
        BtnRecive.setOnClickListener(this);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        rcvDate.setText(formattedDate);

        supplier.setOnItemSelectedListener(this);
        poNoRef.setOnItemSelectedListener(this);


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
                System.out.println("Supplier " + response.code() + response.body());
                if (response.code() == 200) {
                    suppliers = response.body();
                    System.out.println("suppliers" + suppliers);
                    setSuppliers(suppliers);

                    supplierddl = new ArrayList<String>();

                    if (suppliers != null) {
                        for (Supplier supplier : suppliers) {
                            supplierddl.add(supplier.getName());
                        }
                    } else {
                        System.out.println("HELP Lah");
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, supplierddl);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    supplier.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "System down please try again later", Toast.LENGTH_LONG);

            }
        });

        //get all POs
        Call<List<PurchaseOrder>> callPo = apiInterface.GetAllPos();
        callPo.enqueue(new Callback<List<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<List<PurchaseOrder>> callPo, Response<List<PurchaseOrder>> poRes) {
                //System.out.println("PO "+poRes.code()+poRes.body());
                if (poRes.code() == 200) {
                    List<PurchaseOrder> allPos = poRes.body();
                    purchaseOrders = new ArrayList<PurchaseOrder>();


                    poddl = new ArrayList<String>();

                    for (PurchaseOrder po : allPos) {
                        //   System.out.println(po.getStatus());
                        if (po.getStatus().equals("ordered")) {
                            // System.out.println(po.getStatus()+ "true!");
                            poddl.add(Integer.toString(po.getId()));
                            purchaseOrders.add(po);
                        }
                    }

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, poddl);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    poNoRef.setAdapter(dataAdapter2);


                    //get and set PODS to POs
                    for (PurchaseOrder po : purchaseOrders) {

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiInterface.url)
                                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                        Call<List<PurchaseOrderDetail>> call = apiInterface.GetPurchaseOrderDetail(po.getId());
                        call.enqueue(new Callback<List<PurchaseOrderDetail>>() {
                            @Override
                            public void onResponse(Call<List<PurchaseOrderDetail>> call, Response<List<PurchaseOrderDetail>> response) {
                                System.out.println("PODs " + response.code() + response.body());
                                //List<PurchaseOrderDetail>Allpods = response.body();
                                pods.addAll(response.body());
                            }

                            @Override
                            public void onFailure(Call<List<PurchaseOrderDetail>> call, Throwable t) {

                            }
                        });
                       // po.setDetailList(pods);
                        //System.out.println("pods set"+pods);
                    }


                    //set list
                    setPurchaseOrders(purchaseOrders);
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "System down please try again later", Toast.LENGTH_LONG);

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
                Toast.makeText(getApplicationContext(), "System down please try again later", Toast.LENGTH_LONG);

            }
        });

        selectedStationery = new ArrayList<Stationery>();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int spinnerId = parent.getId();
        String item = parent.getItemAtPosition(position).toString();


        if (spinnerId == R.id.supplier) {
            Toast.makeText(ReceiveGoodsActivity.this, item, Toast.LENGTH_SHORT).show();
            if(purchaseOrders!=null) {
                List<String> newPoddl = new ArrayList<String>();
                for (Supplier supplier : suppliers) {
                    if (supplier.getName().equals(item)) {
                        int supId = supplier.getId();
                        for (PurchaseOrder po : purchaseOrders) {
                            if (po.getSupplierId() == supId) {
                                newPoddl.add(Integer.toString(po.getId()));
                            }
                        }
                    }
                }
                poddl = newPoddl;
            }
            //get supplier id 
            //set po reference to only supplier's poddl
        }
        System.out.println("from po"+purchaseOrders);

        if (spinnerId == R.id.PoNumberRef) {

            int poNum = Integer.parseInt(parent.getItemAtPosition(position).toString());

            if(purchaseOrders==null){System.out.println("it is null");}

            for (PurchaseOrder po : purchaseOrders) {
                if (po.getId() == poNum) {
                    List<Stationery> select = new ArrayList<Stationery>();
                    String desc="";

                    for(PurchaseOrderDetail pod :pods){
                    if(pod.getPurchaseOrderId()==po.getId()) {
                        Stationery s = new Stationery();
                        s.setId(pod.getStationeryId());
                        s.setInventoryQty(pod.getQty());
                        s.setReOrderQty(po.getId());

                            for (Stationery stationery : allStationery) {
                                if (stationery.getId() == pod.getStationeryId()) {
                                    s.setDesc(stationery.getDesc());
                                }
                            }


                            select.add(s);
                        }
                    }

                    selectedStationery= select;
                    //set supplier
                    GenericAdapter<Stationery> adapter = new GenericAdapter<>(this,R.layout.activity_discrepency_list_rows,selectedStationery);
                    ListView listView=findViewById(R.id.rcvListView);

                    if(listView!=null){
                        listView.setAdapter(adapter);
                    }

                }
            }

            Toast.makeText(ReceiveGoodsActivity.this, item, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rcvGoodsSaveBtn) {
            Stationery stationery = selectedStationery.get(0);
            //set Po status to delivered
            //create stock adjustment detail and send back to controller
            List<StockAdjustmentDetail> sads = new ArrayList<StockAdjustmentDetail>();

            for(Stationery s:selectedStationery){
                StockAdjustmentDetail sad = new StockAdjustmentDetail();
                sad.setStationeryId(s.getId());
                sad.setDiscpQty(s.getInventoryQty());
                sads.add(sad);
            }

            System.out.println(sads);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.url)
                    .client(SSLBypasser.getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            //session.getInt("staffId", 0)
            Call<StockAdjustment> call = apiInterface.PostReceivedGoods(sads,session.getInt("staffId", 0));
            call.enqueue(new Callback<StockAdjustment>() {
                @Override
                public void onResponse(Call<StockAdjustment> call, Response<StockAdjustment> response) {
                    System.out.println("response from sad"+response.code());
                    if (response.code() == 201 || response.code()==200) {
                        Toast.makeText(getApplicationContext(), "Purchase Order updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Purchase Order updated", Toast.LENGTH_LONG).show();
                        //Intent failure = new Intent(getApplicationContext(), StationeryRetrievalActivity.class);
                        //startActivity(failure);
                    }
                }

                @Override
                public void onFailure(Call<StockAdjustment> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to update inventory", Toast.LENGTH_LONG).show();
                }
            });

            Call<PurchaseOrder> callPO = apiInterface.PORecieved(stationery.getReOrderQty());
            callPO.enqueue(new Callback<PurchaseOrder>() {
                @Override
                public void onResponse(Call<PurchaseOrder> callPO, Response<PurchaseOrder> response) {
                    System.out.println("response from po"+response.code());
                    if(response.code()!=200){
                        Toast.makeText(getApplicationContext(), "Failed update PO status", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<PurchaseOrder> callPO, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed update PO status", Toast.LENGTH_LONG);

                }
            });

            Intent intent = new Intent(this,ViewInventoryActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StoreClerkHomePageActivity.class);
        startActivity(intent);
    }
}