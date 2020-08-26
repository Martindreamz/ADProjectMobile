package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Staff.RepresentativeMenuActivity;
import iss.workshop.adprojectmobile.activity.Store.StoreClerkHomePageActivity;
import iss.workshop.adprojectmobile.model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn;
    TextView username;
    TextView password;
    SharedPreferences session;
    SharedPreferences.Editor session_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();


    }

    @Override
    public void onClick(View view) {
        if (view == loginBtn) {
            Employee emp = new Employee(username.getText().toString().trim(), password.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.url)
                    .client(SSLBypasser.getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            //getting all requisitions to be processed
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<Employee> call = apiInterface.login(emp);
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    System.out.println(response.code());
                    if (response.code() != 404) {
                        Employee currEmp = response.body();
                        session_editor.putString("username", currEmp.getName());
                        session_editor.putString("role", currEmp.getRole());
                        session_editor.putInt("departmentId", currEmp.getDepartmentId());
                        session_editor.putInt("staffId", currEmp.getId());
                        session_editor.commit();
                        System.out.println("from login preference"+ session.getInt("staffId",0)+"from currEMP "+currEmp.getId());

                        if (currEmp.getRole().equals("STRMGR") || currEmp.getRole().equals("STRSUPV")) {
                            Toast.makeText(getApplication(), "Hi there, " + currEmp.getName() + " sorry you are not authorized for this mobile app", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplication(), "Welcome, " + currEmp.getName(), Toast.LENGTH_LONG).show();
                            Intent intent;
                            switch (currEmp.getRole()) {
                                case "HEAD":
                                    intent = new Intent(getApplicationContext(), DepartmentHeadHomePageActivity.class);
                                    break;
                                case "DELEGATE":
                                case "STAFF":
                                case "REPRESENTATIVE":
                                    intent = new Intent(getApplicationContext(), RepresentativeMenuActivity.class);
                                    break;
                                default:
                                    intent = new Intent(getApplicationContext(), StoreClerkHomePageActivity.class);
                            }

                            startActivity(intent);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Employee not found or incorrect password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Server down", Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}