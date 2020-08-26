package iss.workshop.adprojectmobile.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.adapters.DisbursementTableAdapter;
import iss.workshop.adprojectmobile.model.DisbursementList;
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
        currDisbursementLists = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<DisbursementList>> call = apiInterface.retrievalAllDisbursementLists();

        call.enqueue(new Callback<List<DisbursementList>>() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DisbursementList>> call, Response<List<DisbursementList>> response) {
                if (response.code() == 200) {
                    disbursementlist = response.body();
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
            public void onFailure(Call<List<DisbursementList>> call, Throwable t) {


            }

        });
        //Test Data
        String cInfo = "Collection Date: 09/08/2020" + System.lineSeparator() +
                "Collection Time: 9:30am" + System.lineSeparator() + "Delivered By: Store Clerk 1";
        collectionInfo.setText(cInfo);
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
        DisbursementTableAdapter DLadapter = new DisbursementTableAdapter(this,R.layout.activity_disbursement_list_tablerow, currDisbursementLists);
        disbursementListTable.setAdapter(DLadapter);
        disbursementListTable.setOnItemClickListener(this);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(currDisbursementLists.get(i).getDate());
    }
}