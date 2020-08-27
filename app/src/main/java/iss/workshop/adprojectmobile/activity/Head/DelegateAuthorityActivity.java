package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DelegateAuthorityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatePickerDialog picker;
    Button startDate, endDate;
    Button authorizeBtn;
    Spinner spinner;
    List<Employee> departmentReps;
    List<Employee> allEmployee;
    TextView txtView;

    String selectedEmployee;
    final List<String> employees = new ArrayList<String>();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_authority);
        startDate = findViewById(R.id.startDatePicker);
        endDate = findViewById(R.id.endDatePicker);
        authorizeBtn = findViewById(R.id.authorizeDelegate);
        txtView=(TextView) findViewById(R.id.selectedEmployeeTextView) ;
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Employee>> call = apiInterface.getAllDepartmentReps();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.code() == 200) {
                    departmentReps = response.body();
                    setDepartmentReps(departmentReps);
                    for(Employee e: getDepartmentReps() ){
                        employees.add(e.getName());
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
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DelegateAuthorityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DelegateAuthorityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        authorizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String start = startDate.getText().toString();
                final String end = endDate.getText().toString();

                final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                Call<List<Employee>> allEmployeeList = apiInterface.GetAllEmployeesByDept(3);
                allEmployeeList.enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                        if (response.code() == 200) {
                            allEmployee = response.body();
                            setDepartmentReps(allEmployee);

                            Employee resultObj =new Employee();
                            Employee newDelegate =new Employee();
                            Employee oldDelegate = new Employee();

                            for(Employee e : getAllEmployee()){
                                if(e.getRole()=="DELEGATE"){
                                    oldDelegate = e;
                                    oldDelegate.setRole("STAFF");
                                }
                                if(e.getName().equalsIgnoreCase(selectedEmployee)){
                                    newDelegate = e;
                                    newDelegate.setRole("DELEGATE");
                                }
                            }

                            if(oldDelegate!=null && newDelegate!=null){
                                resultObj = new Employee();
                                resultObj.setId(3);
                                resultObj.setName(start);
                                resultObj.setPassword(end);
                                resultObj.setEmail(String.valueOf(oldDelegate.getId()));
                                resultObj.setRole(oldDelegate.getRole());
                                resultObj.setDepartmentId(newDelegate.getId());
                                resultObj.setPhoneNum(newDelegate.getRole());
                            }

                            Call<Employee> DeptDelegateCall = apiInterface.DeptDelegate(resultObj);

                        }
                    }
                    @Override
                    public void onFailure(Call<List<Employee>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEmployee = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}