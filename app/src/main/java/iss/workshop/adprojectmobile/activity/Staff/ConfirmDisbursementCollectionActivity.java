package iss.workshop.adprojectmobile.activity.Staff;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.CollectionInfo;
import iss.workshop.adprojectmobile.model.DisbursementDetail;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmDisbursementCollectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //comment
    TextView collectionDateView, collectionTimeView;
    Button completeBtn;
    Spinner spinner;
    private TableLayout collectTableLayout;

    int departmentId;

    //for retrieving info
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;

    List<CollectionInfo> collectionInfo;
    List<DisbursementList> disbursement;
    List<DisbursementDetail> disbursementDetail;
    List<DisbursementDetail> latestDisbursementDetail;
    List<DisbursementDetail> currDisbursementDetail = new ArrayList<>();
    List<RequisitionDetail> requisitionDetail;
    List<Stationery> stationery;
    List<DisbursementDetail> filteredDisbursementDetail;
    String collectionDateData;
    String collectionTimeData;
    String collectionPointData;

    public List<DisbursementDetail> getFilteredDisbursementDetail() {
        return filteredDisbursementDetail;
    }

    public void setFilteredDisbursementDetail(List<DisbursementDetail> filteredDisbursementDetail) {
        this.filteredDisbursementDetail = filteredDisbursementDetail;
    }

    public String getCollectionTimeData() {
        return collectionTimeData;
    }

    public void setCollectionTimeData(String collectionTimeData) {
        this.collectionTimeData = collectionTimeData;
    }

    public String getCollectionDateData() {
        return collectionDateData;
    }

    public void setCollectionDateData(String collectionDateData) {
        this.collectionDateData = collectionDateData;
    }

    public String getCollectionPointData() {
        return collectionPointData;
    }

    public void setCollectionPointData(String collectionPointData) {
        this.collectionPointData = collectionPointData;
    }

    public List<DisbursementDetail> getLatestDisbursementDetail() {
        return latestDisbursementDetail;
    }

    public void setLatestDisbursementDetail(List<DisbursementDetail> latestDisbursementDetail) {
        this.latestDisbursementDetail = latestDisbursementDetail;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement_collection);

        latestDisbursementDetail = new ArrayList<>();
        collectTableLayout = (TableLayout) findViewById(R.id.collectTableLayout);

        //defining textViews & Buttons
        collectionDateView = findViewById(R.id.collectionDate);
        collectionTimeView = findViewById(R.id.collectionTime);

        completeBtn = findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (DisbursementDetail dd : filteredDisbursementDetail) {
                    System.out.println(dd);
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiInterface.url)
                        .client(SSLBypasser.getUnsafeOkHttpClient().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterfaceSendDisbursementDetail = retrofit.create(ApiInterface.class);
                Call<List<DisbursementDetail>> sendDisbursementDetailCall = apiInterfaceSendDisbursementDetail.SendDisbursementDetail(filteredDisbursementDetail);

                sendDisbursementDetailCall.enqueue(new Callback<List<DisbursementDetail>>() {
                    @Override
                    public void onResponse(Call<List<DisbursementDetail>> call, Response<List<DisbursementDetail>> response) {
                        System.out.println(response.code());

                        Intent intent = new Intent(getApplicationContext(), RepresentativeMenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<List<DisbursementDetail>> call, Throwable t) {

                    }
                });
            }
        });

        spinner = findViewById(R.id.selectCollectinoPoint);
        spinner.setOnItemSelectedListener(this);

        //retrieving info
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        departmentId = session.getInt("departmentId", 0);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //getting all info to be processed
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<DisbursementList>> callDisbursementForCollection = apiInterface.getNearestDisbursementByDeptId(departmentId);

        callDisbursementForCollection.enqueue(new Callback<List<DisbursementList>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DisbursementList>> call, Response<List<DisbursementList>> response) {
                System.out.println("Response here: " + response.code());
                disbursement = response.body();

                if (disbursement != null) {
                    Call<List<CollectionInfo>> callCollect = apiInterface.getAllCollectionPointforDept();

                    callCollect.enqueue(new Callback<List<CollectionInfo>>() {
                        @Override
                        public void onResponse(Call<List<CollectionInfo>> call, Response<List<CollectionInfo>> response) {
                            System.out.println("Response here: " + response.code());
                            collectionInfo = response.body();

                            for (CollectionInfo cInfo : collectionInfo) {
                                if (cInfo.getCollectionPoint().equals(disbursement.get(0).getDeliveryPoint())) {
                                    LocalTime lt = LocalTime.of(Integer.parseInt(cInfo.getCollectionTime().substring(11, 13)),
                                            Integer.parseInt(cInfo.getCollectionTime().substring(14, 16)),
                                            Integer.parseInt(cInfo.getCollectionTime().substring(17, 19)));

                                    LocalDate ld = LocalDate.of(Integer.parseInt(disbursement.get(0).getDate().substring(0, 4)),
                                            Integer.parseInt(disbursement.get(0).getDate().substring(5, 7)),
                                            Integer.parseInt(disbursement.get(0).getDate().substring(8, 10)));

                                    setCollectionTimeData(lt.toString());
                                    setCollectionDateData(ld.toString());

                                    collectionDateView.setText(collectionDateData);
                                    collectionTimeView.setText(collectionTimeData + " AM");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CollectionInfo>> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });

                    List<String> collectionPoints = new ArrayList<String>();

                    for (DisbursementList dList : disbursement) {
                        collectionPoints.add(dList.getDeliveryPoint());
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, collectionPoints);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<DisbursementList>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });

        Call<List<DisbursementList>> callDisbursementForDisbursementDetail = apiInterface.getNearestDisbursementByDeptId(departmentId);

        callDisbursementForDisbursementDetail.enqueue(new Callback<List<DisbursementList>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DisbursementList>> call, Response<List<DisbursementList>> response) {
                disbursement = response.body();

                if (disbursement != null) {

                    Call<List<DisbursementDetail>> callDisbursementDetail = apiInterface.getDisbursementDetailByDeptId(departmentId);

                    callDisbursementDetail.enqueue(new Callback<List<DisbursementDetail>>() {
                        @Override
                        public void onResponse(Call<List<DisbursementDetail>> call, Response<List<DisbursementDetail>> response) {
                            disbursementDetail = response.body();

                                Call<List<RequisitionDetail>> callRequisitionDetail = apiInterface.getToDeliverRequisitionDetailByDeptId(departmentId);

                                callRequisitionDetail.enqueue(new Callback<List<RequisitionDetail>>() {
                                    @Override
                                    public void onResponse(Call<List<RequisitionDetail>> call, Response<List<RequisitionDetail>> response) {
                                        requisitionDetail = response.body();

                                        Call<List<Stationery>> callStationery = apiInterface.getAllStationery();

                                        callStationery.enqueue(new Callback<List<Stationery>>() {
                                            @Override
                                            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                                                stationery = response.body();

                                                final List<DisbursementDetail> filteredDisbursementDetail = new ArrayList<>();

                                                for (DisbursementList dList : disbursement) {
                                                    for (DisbursementDetail dDetail : disbursementDetail) {
                                                        if (dList.getId() == dDetail.getDisbursementListId()) {
                                                            filteredDisbursementDetail.add(dDetail);
                                                        }
                                                    }
                                                }

                                                if (filteredDisbursementDetail != null) {
                                                    for (DisbursementList dList : disbursement) {
                                                        for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                            if (dList.getId() == dDetail.getDisbursementListId()) {
                                                                dDetail.setDeliveryPoint(dList.getDeliveryPoint());
                                                            }
                                                        }
                                                    }

                                                    for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                        for (RequisitionDetail rDetail : requisitionDetail) {
                                                            if (dDetail.getRequisitionDetailId() == rDetail.getId()) {
                                                                dDetail.setStationeryId(rDetail.getStationeryId());
                                                            }
                                                        }
                                                    }

                                                    for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                        for (Stationery stat : stationery) {
                                                            if (dDetail.getStationeryId() == stat.getId()) {
                                                                dDetail.setStationeryDesc(stat.getDesc());
                                                            }
                                                        }
                                                    }
                                                }
                                                setLatestDisbursementDetail(filteredDisbursementDetail);
                                            }

                                            @Override
                                            public void onFailure(Call<List<Stationery>> call, Throwable t) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<List<RequisitionDetail>> call, Throwable t) {
                                        Log.e("error", t.getMessage());
                                    }
                                });
                            }

                        @Override
                        public void onFailure(Call<List<DisbursementDetail>> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<DisbursementList>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        final String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(ConfirmDisbursementCollectionActivity.this, item, Toast.LENGTH_SHORT).show();

        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                System.out.println(getLatestDisbursementDetail());

                List<DisbursementDetail> emptyList = new ArrayList<>();

                for (DisbursementDetail dDetail : getLatestDisbursementDetail()) {
                    if (dDetail.getDeliveryPoint().equals(item)) {
                        emptyList.add(dDetail);
                    }
                }

                currDisbursementDetail = emptyList;

                for (final DisbursementDetail dDetail : currDisbursementDetail) {
                    View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_confirm_disbursement_collection_item, null, false);
                    TextView itemCode = (TextView) tableRow.findViewById(R.id.itemCode);
                    TextView itemDesc = (TextView) tableRow.findViewById(R.id.itemDesc);
                    TextView itemRqt = (TextView) tableRow.findViewById(R.id.itemRqt);

                    final NumberPicker qty = tableRow.findViewById(R.id.itemRcv);
                    qty.setValue(dDetail.getQty());
                    qty.setMin(0);
                    qty.setValueChangedListener(new ValueChangedListener() {
                        @Override
                        public void valueChanged(int value, ActionEnum action) {
                            dDetail.setQty(value);
                        }
                    });

                    setFilteredDisbursementDetail(filteredDisbursementDetail);

                    itemCode.setText(Integer.toString(dDetail.getStationeryId()));
                    itemDesc.setText(dDetail.getStationeryDesc());
                    itemRqt.setText(Integer.toString(dDetail.getQty()));
                    collectTableLayout.addView(tableRow);
                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RepresentativeMenuActivity.class);
        startActivity(intent);
    }

}