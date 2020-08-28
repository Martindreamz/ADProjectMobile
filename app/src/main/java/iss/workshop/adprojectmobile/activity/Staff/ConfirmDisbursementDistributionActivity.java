package iss.workshop.adprojectmobile.activity.Staff;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.CollectionInfo;
import iss.workshop.adprojectmobile.model.DisbursementDetail;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.Employee;
import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmDisbursementDistributionActivity extends AppCompatActivity {

    Spinner spinner;
    private TableLayout tableLayout;

    int departmentId;

    final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiInterface.url)
            .client(SSLBypasser.getUnsafeOkHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //getting all info to be processed
    final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    //for retrieving info
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;

    List<Employee> employeeList;

    DisbursementList disbursement;
    List<DisbursementDetail> disbursementDetail;
    List<Requisition> requisition;
    List<RequisitionDetail> requisitionDetail;
    List<Stationery> stationery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement_distribution);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //retrieving info
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        departmentId = session.getInt("departmentId", 0);


        Call<DisbursementList> callDisbursementUnderDept = apiInterface.getLatestDisbursementByDeptId(departmentId);

        callDisbursementUnderDept.enqueue(new Callback<DisbursementList>() {
            @Override
            public void onResponse(Call<DisbursementList> call, Response<DisbursementList> response) {
                disbursement = response.body();

                if (disbursement != null) {

                    Call<List<DisbursementDetail>> callDisbursementDetail = apiInterface.getDisbursementDetailByDeptId(departmentId);

                    callDisbursementDetail.enqueue(new Callback<List<DisbursementDetail>>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<List<DisbursementDetail>> call, Response<List<DisbursementDetail>> response) {
                            disbursementDetail = response.body();

                            final List<DisbursementDetail> filteredDisbursementDetail = new ArrayList<>();

                            if (disbursementDetail != null) {
                                for (DisbursementDetail dDetail : disbursementDetail) {
                                    if (dDetail.getDisbursementListId() == disbursement.getId()) {
                                        filteredDisbursementDetail.add(dDetail);
                                    }
                                }
                            }

                            if (filteredDisbursementDetail != null) {
                                Call<List<Requisition>> callRequisitionsUnderDep = apiInterface.getToDeliverRequisitionsByDeptId(departmentId);

                                callRequisitionsUnderDep.enqueue(new Callback<List<Requisition>>() {
                                    @Override
                                    public void onResponse(Call<List<Requisition>> call, Response<List<Requisition>> response) {
                                        requisition = response.body();

                                        if (requisition != null) {
                                            Call<List<Employee>> callEmployeeUnderDep = apiInterface.getAllEmployeesByDept(departmentId);

                                            callEmployeeUnderDep.enqueue(new Callback<List<Employee>>() {
                                                @Override
                                                public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                                                    employeeList = response.body();

                                                    if (employeeList != null) {
                                                        for (Requisition req : requisition) {
                                                            for (Employee emp: employeeList) {
                                                                if (req.getEmployeeId() == emp.getId()) {
                                                                    req.setEmployeeName(emp.getName());
                                                                }
                                                            }
                                                        }

                                                        Call<List<RequisitionDetail>> callRequisitionDetail = apiInterface.getToDeliverRequisitionDetailByDeptId(departmentId);

                                                        callRequisitionDetail.enqueue(new Callback<List<RequisitionDetail>>() {
                                                            @Override
                                                            public void onResponse(Call<List<RequisitionDetail>> call, Response<List<RequisitionDetail>> response) {
                                                                requisitionDetail = response.body();

                                                                for (RequisitionDetail rDetail : requisitionDetail) {
                                                                    for (Requisition req : requisition) {
                                                                        if (rDetail.getRequisitionId() == req.getId()) {
                                                                            rDetail.setEmployeeName(req.getEmployeeName());
                                                                        }
                                                                    }
                                                                }

                                                                List<RequisitionDetail> filteredRequisitionDetail = new ArrayList<>();

                                                                for (RequisitionDetail rDetail : requisitionDetail) {
                                                                    for (Requisition req : requisition) {
                                                                        if (req.getId() == rDetail.getRequisitionId()) {
                                                                            filteredRequisitionDetail.add(rDetail);
                                                                        }
                                                                    }
                                                                }

                                                                if (filteredRequisitionDetail != null) {

                                                                    final List<RequisitionDetail> finalRequisitionDetail = new ArrayList<>();

                                                                    for (RequisitionDetail rDetail : filteredRequisitionDetail) {
                                                                        for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                                            if (rDetail.getId() == dDetail.getRequisitionDetailId()) {
                                                                                finalRequisitionDetail.add(rDetail);
                                                                            }
                                                                        }
                                                                    }

                                                                    if (finalRequisitionDetail != null) {

                                                                        Call<List<Stationery>> callStationery = apiInterface.getAllStationery();

                                                                        callStationery.enqueue(new Callback<List<Stationery>>() {
                                                                            @Override
                                                                            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                                                                                stationery = response.body();

                                                                                if (stationery != null) {
                                                                                    for (RequisitionDetail rDetail : finalRequisitionDetail) {
                                                                                        for (Stationery stat : stationery) {
                                                                                            if (rDetail.getStationeryId() == stat.getId()) {
                                                                                                rDetail.setStationeryDesc(stat.getDesc());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                if (finalRequisitionDetail != null) {
                                                                                    for (RequisitionDetail rDetail : finalRequisitionDetail) {
                                                                                        View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_confirm_disbursement_distribution_item, null, false);
                                                                                        TextView requester = (TextView) tableRow.findViewById(R.id.requestorName);
                                                                                        TextView statDescription = (TextView) tableRow.findViewById(R.id.statDescription);
                                                                                        TextView requestedQty = (TextView) tableRow.findViewById(R.id.requestedQty);
                                                                                        TextView receivedQty = (TextView) tableRow.findViewById(R.id.receivedQty);

                                                                                        requester.setText(rDetail.getEmployeeName());
                                                                                        statDescription.setText(rDetail.getStationeryDesc());
                                                                                        requestedQty.setText(Integer.toString(rDetail.getReqQty()));
                                                                                        receivedQty.setText(Integer.toString(rDetail.getRcvQty()));
                                                                                        tableLayout.addView(tableRow);
                                                                                    }
                                                                                } else {
                                                                                    View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_confirm_disbursement_distribution_item, null, false);
                                                                                    TextView requester = (TextView) tableRow.findViewById(R.id.requestorName);
                                                                                    TextView statDescription = (TextView) tableRow.findViewById(R.id.statDescription);
                                                                                    TextView requestedQty = (TextView) tableRow.findViewById(R.id.requestedQty);
                                                                                    TextView receivedQty = (TextView) tableRow.findViewById(R.id.receivedQty);

                                                                                    requester.setText("");
                                                                                    statDescription.setText("No Requisition Data");
                                                                                    requestedQty.setText("");
                                                                                    receivedQty.setText("");
                                                                                    tableLayout.addView(tableRow);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<List<Stationery>> call, Throwable t) {
                                                                                Log.e("error", t.getMessage());
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(Call<List<RequisitionDetail>> call, Throwable t) {
                                                                Log.e("error", t.getMessage());
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Employee>> call, Throwable t) {
                                                    Log.e("error", t.getMessage());
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Requisition>> call, Throwable t) {
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