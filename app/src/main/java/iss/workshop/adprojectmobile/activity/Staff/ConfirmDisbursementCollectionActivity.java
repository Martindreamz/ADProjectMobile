package iss.workshop.adprojectmobile.activity.Staff;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

public class ConfirmDisbursementCollectionActivity extends AppCompatActivity {

    TextView collectionPointView, collectionDateView, collectionTimeView, status;
    Button completeBtn;

    int departmentId;

    //for retrieving info
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;

    List<CollectionInfo> collectionInfo;
    DisbursementList disbursement;
    List<DisbursementDetail> disbursementDetail;
    List<RequisitionDetail> requisitionDetail;
    List<Stationery> stationery;

    String collectionDateData;
    String collectionTimeData;
    String collectionPointData;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement_collection);

        //defining textViews & Buttons
        collectionPointView = findViewById(R.id.collectionPoint);
        collectionDateView = findViewById(R.id.collectionDate);
        collectionTimeView = findViewById(R.id.collectionTime);

        status = findViewById(R.id.Status);
        completeBtn = findViewById(R.id.completeBtn);

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

        Call<DisbursementList> callDisbursementForCollection = apiInterface.getLatestDisbursementByDeptId(departmentId);

        callDisbursementForCollection.enqueue(new Callback<DisbursementList>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DisbursementList> call, Response<DisbursementList> response) {
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
                                if (cInfo.getCollectionPoint().equals(disbursement.getDeliveryPoint())) {
                                    LocalTime lt = LocalTime.of(Integer.parseInt(cInfo.getCollectionTime().substring(11, 13)),
                                            Integer.parseInt(cInfo.getCollectionTime().substring(14, 16)),
                                            Integer.parseInt(cInfo.getCollectionTime().substring(17, 19)));

                                    LocalDate ld = LocalDate.of(Integer.parseInt(disbursement.getDate().substring(0, 4)),
                                            Integer.parseInt(disbursement.getDate().substring(5, 7)),
                                            Integer.parseInt(disbursement.getDate().substring(8, 10)));

                                    setCollectionTimeData(lt.toString());
                                    setCollectionDateData(ld.toString());
                                    setCollectionPointData(disbursement.getDeliveryPoint());

                                    collectionPointView.setText(collectionPointData);
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
                }
            }

            @Override
            public void onFailure(Call<DisbursementList> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });


        Call<DisbursementList> callDisbursementForDisbursementDetail = apiInterface.getLatestDisbursementByDeptId(departmentId);

        callDisbursementForDisbursementDetail.enqueue(new Callback<DisbursementList>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DisbursementList> call, Response<DisbursementList> response) {
                System.out.println("Response here: " + response.code());
                disbursement = response.body();

                if (disbursement != null) {

                    Call<List<DisbursementDetail>> callDisbursementDetail = apiInterface.getDisbursementDetailByDeptId(departmentId);

                    callDisbursementDetail.enqueue(new Callback<List<DisbursementDetail>>() {
                        @Override
                        public void onResponse(Call<List<DisbursementDetail>> call, Response<List<DisbursementDetail>> response) {
                            System.out.println("Response here: " + response.code());
                            disbursementDetail = response.body();

                            final List<DisbursementDetail> filteredDisbursementDetail = new ArrayList<>();

                            for (DisbursementDetail dDetail : disbursementDetail) {
                                if (dDetail.getDisbursementListId() == disbursement.getId()) {
                                    filteredDisbursementDetail.add(dDetail);
                                }
                            }

                            if (filteredDisbursementDetail != null) {
                                Call<List<RequisitionDetail>> callRequisitionDetail = apiInterface.getToDeliverRequisitionDetailByDeptId(departmentId);

                                callRequisitionDetail.enqueue(new Callback<List<RequisitionDetail>>() {
                                    @Override
                                    public void onResponse(Call<List<RequisitionDetail>> call, Response<List<RequisitionDetail>> response) {
                                        System.out.println("Response here: " + response.code());
                                        requisitionDetail = response.body();

                                        for (RequisitionDetail rDetail: requisitionDetail) {
                                            for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                if (dDetail.getRequisitionDetailId() == rDetail.getId()) {
                                                    dDetail.setStationeryId(rDetail.getStationeryId());
                                                }
                                            }
                                        }

                                        Call<List<Stationery>> callStationery = apiInterface.getAllStationery();

                                        callStationery.enqueue(new Callback<List<Stationery>>() {
                                            @Override
                                            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                                                System.out.println("Response here: " + response.code());
                                                stationery = response.body();

                                                if(stationery != null) {
                                                    for (DisbursementDetail dDetail2 : filteredDisbursementDetail) {
                                                        for (Stationery stat: stationery) {
                                                            if (dDetail2.getStationeryId() == stat.getId()) {
                                                                dDetail2.setStationeryDesc(stat.getDesc());
                                                            }
                                                        }
                                                    }
                                                }

                                                for (DisbursementDetail testDDetail : filteredDisbursementDetail){
                                                    System.out.println(testDDetail.getStationeryId());
                                                    System.out.println(testDDetail.getStationeryDesc());
                                                    System.out.println(testDDetail.getQty());
                                                }
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
                        }

                        @Override
                        public void onFailure(Call<List<DisbursementDetail>> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DisbursementList> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}