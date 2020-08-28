package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.adapters.GenericAdapter;
import iss.workshop.adprojectmobile.model.StockAdjustment;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiscrepencyListActivity extends AppCompatActivity implements View.OnClickListener  {
    private List<StockAdjustmentDetail> sads;
    private GenericAdapter<StockAdjustmentDetail> adapter;
    private Button BtnRequest;
    private SharedPreferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discrepency_list);
        BtnRequest=findViewById(R.id.reqForAdjVoucherBtn);
        BtnRequest.setOnClickListener(this);
        session = getSharedPreferences("session", MODE_PRIVATE);

        //sads = new ArrayList();
        Intent intent = getIntent();
        if(intent.getExtras()!=null){
           sads = (List<StockAdjustmentDetail>) getIntent().getSerializableExtra("sads");
            System.out.println("from discrepency"+sads);
        }

        adapter = new GenericAdapter<StockAdjustmentDetail>(this,R.layout.activity_discrepency_list_rows,sads);
        ListView listView = findViewById(R.id.dl_listView);

        if(listView!=null){
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if(id==R.id.reqForAdjVoucherBtn){
            System.out.println("Button clicked");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.url)
                    .client(SSLBypasser.getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            //session.getInt("staffId", 0)
            Call<StockAdjustment> call = apiInterface.PostTestStkAd(sads,session.getInt("staffId", 0));
            call.enqueue(new Callback<StockAdjustment>() {
                @Override
                public void onResponse(Call<StockAdjustment> call, Response<StockAdjustment> response) {
                    System.out.println("response"+response.code());
                    if (response.code() == 201) {

                        Toast.makeText(getApplicationContext(), "Request sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                        //Intent failure = new Intent(getApplicationContext(), StationeryRetrievalActivity.class);
                        //startActivity(failure);
                    }
                }

                @Override
                public void onFailure(Call<StockAdjustment> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_LONG).show();
                }
            });

            Intent intent = new Intent(this,ViewInventoryActivity.class);
            startActivity(intent);

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, InventoryCheckActivity.class);
        startActivity(intent);
    }
}