package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iss.workshop.adprojectmobile.R;

public class DelegateAuthorityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    DatePickerDialog picker;
    EditText startDate, endDate;
    Button authorize;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_authority);
        startDate = findViewById(R.id.startDatePicker);
        endDate = findViewById(R.id.endDatePicker);
        authorize = findViewById(R.id.authorizeDelegate);

        spinner = findViewById(R.id.selectEmployee);
        spinner.setOnItemSelectedListener(this);
        //Test Data for spinner
        List<String> employees = new ArrayList<String>();
        employees.add("Martin");
        employees.add("Jane");
        employees.add("Deryl");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employees);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(DelegateAuthorityActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (id == R.id.startDatePicker) {
                            startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        } else if (id == R.id.endDatePicker) {
                            endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }
                }, year, month, day);
        picker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        TextView txtView=(TextView) findViewById(R.id.selectedEmployeeTxt) ;
        txtView.setText("Employee Name: "+item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}