package iss.workshop.adprojectmobile.activity.Store;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Staff.RepresentativeMenuActivity;
import iss.workshop.adprojectmobile.activity.Store.DisbursementDetailsActivity;
import iss.workshop.adprojectmobile.activity.Store.StationeryRetrievalActivity;
import iss.workshop.adprojectmobile.adapters.DisbursementDetailAdapter;
import iss.workshop.adprojectmobile.adapters.DisbursementTableAdapter;
import iss.workshop.adprojectmobile.model.DisbursementList;
import iss.workshop.adprojectmobile.model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisbursementListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    TextView collectionInfo;
    Spinner disbursementDropDown;
    List<DisbursementList> disbursementlist;
    List<String> locationList;
    ListView disbursementListTable;
    List<DisbursementList> currDisbursementLists;
    List<Employee> departmentReps;
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;

    public List<Employee> getDepartmentReps() {
        return departmentReps;
    }

    public void setDepartmentReps(List<Employee> departmentReps) {
        this.departmentReps = departmentReps;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    public List<DisbursementList> getDisbursementlist() {
        return disbursementlist;
    }

    public void setDisbursementlist(List<DisbursementList> disbursementlist) {
        this.disbursementlist = disbursementlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_list);
        collectionInfo = findViewById(R.id.collectionPointDisbursementInfo);
        disbursementDropDown = findViewById(R.id.selectCollectionPointStoreDisbursement);
        disbursementListTable = findViewById(R.id.DisbursementListTable);
        disbursementDropDown.setOnItemSelectedListener(this);
        locationList = new ArrayList<>();
        departmentReps = new ArrayList<>();
        currDisbursementLists = new ArrayList<>();
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<DisbursementList>> call = apiInterface.getAllDisbursementLists(session.getInt("staffId", 0));

        call.enqueue(new Callback<List<DisbursementList>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DisbursementList>> call, Response<List<DisbursementList>> response) {
                System.out.println("calling disbursement list: " + response.code());
                if (response.code() == 200) {
                    disbursementlist = response.body();


                    Call<List<Employee>> call2 = apiInterface.getAllDepartmentReps();
                    call2.enqueue(new Callback<List<Employee>>() {
                                      @Override
                                      public void onResponse(Call<List<Employee>> call2, Response<List<Employee>> response2) {
                                          System.out.println("calling dept rep list: " + response2.code());

                                          if (response2.code() == 200) {
                                              departmentReps = response2.body();
//                                              setDepartmentReps(departmentReps);

                                              for (DisbursementList dl : disbursementlist) {
                                                  for (Employee e : departmentReps) {
                                                      if (dl.getDepartmentId() == e.getDepartmentId()) {
                                                          System.out.println("fake email:" + e.getEmail());
                                                          dl.setDepartment(e.getEmail());
                                                          dl.setRepName(e.getName());
                                                          System.out.println("called dl:" + dl);
                                                      }
                                                  }
                                              }
                                              setDisbursementlist(disbursementlist);
                                              HashSet<String> locationHash = new HashSet<>();
                                              for (DisbursementList dl : disbursementlist) {
                                                  locationHash.add(dl.getDeliveryPoint());
                                              }
                                              for (String s : locationHash) {
                                                  locationList.add(s);
                                              }
                                              setLocationList(locationList);
                                              ArrayAdapter dropdown = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, locationList);
                                              dropdown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                              disbursementDropDown.setAdapter(dropdown);


                                          }

                                      }

                                      @Override
                                      public void onFailure(Call<List<Employee>> call2, Throwable t2) {

                                      }
                                  }

                    );
                }
            }

            @Override
            public void onFailure(Call<List<DisbursementList>> call, Throwable t) {


            }

        });
        //Test Data
//        String cInfo = "Collection Date: 09/08/2020" + System.lineSeparator() +
//                "Collection Time: 9:30am" + System.lineSeparator() + "Delivered By: Store Clerk 1";
//        collectionInfo.setText(cInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currDisbursementLists.clear();
        for (DisbursementList dl : getDisbursementlist()) {
            if (dl.getDeliveryPoint().equals(getLocationList().get(i))) {
                currDisbursementLists.add(dl);
            }
        }
        DisbursementTableAdapter DLadapter = new DisbursementTableAdapter(this, R.layout.activity_disbursement_list_tablerow, currDisbursementLists);
        disbursementListTable.setAdapter(DLadapter);
        disbursementListTable.setOnItemClickListener(this);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), Integer.toString(currDisbursementLists.get(i).getId()), Toast.LENGTH_LONG);
        System.out.println(Integer.toString(currDisbursementLists.get(i).getId()));
//        session_editor.putInt("selected_dl", currDisbursementLists.get(i).getId());
//        session_editor.putString("selected_dl_dept", currDisbursementLists.get(i).getDepartment());
//        session_editor.putString("selected_dl_rep", currDisbursementLists.get(i).getRepName());
//        session_editor.putString("selected_dl_status",currDisbursementLists.get(i).getStatus());
//        session_editor.commit();

        if (currDisbursementLists.get(i).getStatus().equals("delivering")) {
            Intent intent = new Intent(getApplicationContext(), SignaturePadActivity.class);
            intent.putExtra("selected_dl", currDisbursementLists.get(i).getId());
            intent.putExtra("selected_dl_dept", currDisbursementLists.get(i).getDepartment());
            intent.putExtra("selected_dl_rep", currDisbursementLists.get(i).getRepName());
            intent.putExtra("selected_dl_status",currDisbursementLists.get(i).getStatus());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), DisbursementDetailsActivity.class);
            intent.putExtra("selected_dl", currDisbursementLists.get(i).getId());
            intent.putExtra("selected_dl_dept", currDisbursementLists.get(i).getDepartment());
            intent.putExtra("selected_dl_rep", currDisbursementLists.get(i).getRepName());
            intent.putExtra("selected_dl_status",currDisbursementLists.get(i).getStatus());
            intent.putExtra("selected_dl_bitmap",currDisbursementLists.get(i).getBitmap());
            startActivity(intent);
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StoreClerkHomePageActivity.class);
        startActivity(intent);
    }
}