package iss.workshop.adprojectmobile.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Store.DisbursementListActivity;
import iss.workshop.adprojectmobile.model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DelegateAuthorityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatePickerDialog picker;
    Button startDateBtn, endDateBtn;
    Button authorizeBtn;
    Spinner spinner;
    List<Employee> departmentReps;
    List<Employee> allEmployee;
    TextView txtView;
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;
    Employee selectedEmployee,oldDelegate;
    private List<String> employees;
    private List<Employee> departmentEmp;
    String startDateStr, endDateStr;
    boolean startDateClear, endDateClear;
    LocalDate startDate, endDate;

    public Employee getOldDelegate() {
        return oldDelegate;
    }

    public void setOldDelegate(Employee oldDelegate) {
        this.oldDelegate = oldDelegate;
    }

    public List<Employee> getDepartmentReps() {
        return departmentReps;
    }

    public List<Employee> getAllEmployee() {
        return allEmployee;
    }

    public void setDepartmentReps(List<Employee> departmentReps) {
        this.departmentReps = departmentReps;
    }

    public void setAllEmployee(List<Employee> allEmployee) {
        this.allEmployee = allEmployee;
    }

    public List<Employee> getDepartmentEmp() {
        return departmentEmp;
    }

    public void setDepartmentEmp(List<Employee> departmentEmp) {
        this.departmentEmp = departmentEmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_authority);
        startDateBtn = findViewById(R.id.startDatePicker);
        endDateBtn = findViewById(R.id.endDatePicker);
        authorizeBtn = findViewById(R.id.authorizeDelegate);
        txtView = (TextView) findViewById(R.id.selectedEmployeeTextView);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        departmentEmp = new ArrayList<>();
        employees = new ArrayList<>();
        startDateStr = "";
        endDateStr = "";
        startDateClear = true;
        endDateClear = true;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Employee>> call = apiInterface.getAllEmployeesByDept(session.getInt("departmentId", 0));

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.code() == 200) {
                    departmentEmp = response.body();
                    setDepartmentEmp(departmentEmp);
                    for (Employee e : getDepartmentEmp()) {
                        System.out.println(e);
                        if (e.getRole().equals("STAFF")) {
                            employees.add(e.getName());
                        }
                        if(e.getRole().equals("DELEGATE")){
                            oldDelegate=e;
                            setOldDelegate(oldDelegate);
                        }
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


        //startDate.setOnClickListener(this);
        //.setOnClickListener(this);
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DelegateAuthorityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDateBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                startDate = LocalDate.of(year, (monthOfYear + 1), dayOfMonth);
                                LocalDateTime selectedDate = LocalDateTime.of(year, (monthOfYear + 1), dayOfMonth, 23, 59, 59);
                                if (selectedDate.isBefore(LocalDateTime.now())) {
                                    Toast.makeText(getApplicationContext(), "select a future date or today for end date  please", Toast.LENGTH_LONG).show();
                                    startDateClear = false;
                                } else {
                                    startDateClear = true;
                                }

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DelegateAuthorityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDateBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                endDate = LocalDate.of(year, (monthOfYear + 1), dayOfMonth);
                                LocalDateTime selectedDate = LocalDateTime.of(year, (monthOfYear + 1), dayOfMonth, 23, 59, 59);
                                if (selectedDate.isBefore(LocalDateTime.now())) {
                                    Toast.makeText(getApplicationContext(), "select a future date or today for start date please", Toast.LENGTH_LONG).show();
                                    startDateClear = false;
                                } else {
                                    startDateClear = true;
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        authorizeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                startDateStr = startDateBtn.getText().toString();
                endDateStr = endDateBtn.getText().toString();
                System.out.println(startDateStr);
                System.out.println(endDateStr);

                if (!startDateClear) {
                    Toast.makeText(getApplicationContext(), "select a future date or today for start date please", Toast.LENGTH_LONG).show();
                } else if (!endDateClear) {
                    Toast.makeText(getApplicationContext(), "select a future date or today for end date  please", Toast.LENGTH_LONG).show();
                } else if (endDate.isBefore(startDate)) {
                    Toast.makeText(getApplicationContext(), "End date cannot be before start date", Toast.LENGTH_LONG).show();
                } else {
                    Employee sendingEmployee = new Employee();
                    sendingEmployee.setId(selectedEmployee.getId());
                    sendingEmployee.setName(startDateStr);
                    sendingEmployee.setPassword(endDateStr);
                    sendingEmployee.setEmail(String.valueOf(oldDelegate.getId()));
                    sendingEmployee.setRole(oldDelegate.getRole());
                    sendingEmployee.setDepartmentId(selectedEmployee.getId());
                    sendingEmployee.setPhoneNum(selectedEmployee.getRole());


                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(ApiInterface.url)
                            .client(SSLBypasser.getUnsafeOkHttpClient().build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final ApiInterface apiInterface2 = retrofit.create(ApiInterface.class);
                    Call<Employee> DeptDelegateCall = apiInterface2.DeptDelegate(sendingEmployee);
                    DeptDelegateCall.enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                            if(response.code()==200){
                                Intent intent = new Intent(getApplicationContext(), DepartmentHeadHomePageActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {

                        }
                    });

                }


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEmployee = departmentEmp.get(i);
        //Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}