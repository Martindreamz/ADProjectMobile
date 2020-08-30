package iss.workshop.adprojectmobile.activity.Staff;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ConfirmDisbursementDistributionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    int selectedEmpId;

    public int getSelectedEmpId() {
        return selectedEmpId;
    }

    public void setSelectedEmpId(int selectedEmpId) {
        this.selectedEmpId = selectedEmpId;
    }

    List<DisbursementList> disbursement ;
    List<DisbursementDetail> disbursementDetail = new ArrayList<>();
    List<DisbursementDetail> currDisbursementDetail = new ArrayList<>();
    List<Requisition> requisition;
    List<RequisitionDetail> requisitionDetail;
    List<Stationery> stationery;

    public List<DisbursementDetail> getDisbursementDetail() {
        return disbursementDetail;
    }

    public void setDisbursementDetail(List<DisbursementDetail> disbursementDetail) {
        this.disbursementDetail = disbursementDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement_distribution);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        spinner = findViewById(R.id.selectRequestor);
        spinner.setOnItemSelectedListener(this);

        //retrieving info
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        departmentId = session.getInt("departmentId", 0);

        Call<List<Employee>> callEmployeeUnderDept = apiInterface.getAllEmployeesByDept(departmentId);

        callEmployeeUnderDept.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employeeList = response.body();

                if (employeeList != null) {
                    List<String> employees = new ArrayList<String>();

                    for (Employee employee : employeeList) {
                        employees.add(employee.getName());
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, employees);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });

        Call<List<DisbursementList>> callDisbursementUnderDept = apiInterface.getNearestDisbursementByDeptId(departmentId);

        callDisbursementUnderDept.enqueue(new Callback<List<DisbursementList>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DisbursementList>> call, Response<List<DisbursementList>> response) {
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
                                    for (DisbursementList dList : disbursement) {
                                        if (dDetail.getDisbursementListId() == dList.getId()) {
                                            filteredDisbursementDetail.add(dDetail);
                                        }
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

                                                                                    for (DisbursementDetail dDetail : filteredDisbursementDetail) {
                                                                                        for (RequisitionDetail rDetail : finalRequisitionDetail) {
                                                                                            if (dDetail.getRequisitionDetailId() == rDetail.getId()) {
                                                                                                dDetail.setRequestedEmp(rDetail.getEmployeeName());
                                                                                                dDetail.setStationeryDesc(rDetail.getStationeryDesc());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                setDisbursementDetail(filteredDisbursementDetail);
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
                } else {
                    View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_confirm_disbursement_distribution_item, null, false);
                    TextView statDescription = (TextView) tableRow.findViewById(R.id.statDescription);
                    TextView receivedQty = (TextView) tableRow.findViewById(R.id.receivedQty);

                    statDescription.setText("Items are not collected yet!");
                    receivedQty.setText("");
                    tableLayout.addView(tableRow);
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
        Toast.makeText(ConfirmDisbursementDistributionActivity.this, item, Toast.LENGTH_SHORT).show();

        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                List<DisbursementDetail> emptyList = new ArrayList<>();

                for (DisbursementDetail dDetail : disbursementDetail) {
                    if (dDetail.getRequestedEmp().equals(item)) {
                        emptyList.add(dDetail);
                    }
                }

                System.out.println("EMPTY LIST: " + emptyList);

                currDisbursementDetail = emptyList;

                System.out.println("CURRENT LIST: " + currDisbursementDetail);

                for (DisbursementDetail dDetail : currDisbursementDetail) {
                    View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_confirm_disbursement_distribution_item, null, false);
                    TextView statDescription = (TextView) tableRow.findViewById(R.id.statDescription);
                    TextView receivedQty = (TextView) tableRow.findViewById(R.id.receivedQty);

                    statDescription.setText(dDetail.getStationeryDesc());
                    receivedQty.setText(Integer.toString(dDetail.getQty()));
                    tableLayout.addView(tableRow);
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
}