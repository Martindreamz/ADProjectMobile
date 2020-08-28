package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.MainActivity;
import iss.workshop.adprojectmobile.adapters.GenericAdapter;
import iss.workshop.adprojectmobile.model.Stationery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewInventoryActivity extends AppCompatActivity
implements View.OnClickListener{
    List<Stationery> allStationery;
    Button btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);

        btnHome=findViewById(R.id.homeBtn);
        btnHome.setOnClickListener(this);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        //get all Stationery
        Call<List<Stationery>> callStn = apiInterface.getAllStationery();
        callStn.enqueue(new Callback<List<Stationery>>() {
            @Override
            public void onResponse(Call<List<Stationery>> callStn, Response<List<Stationery>> stationeryRes) {
                System.out.println("Stationery get " + stationeryRes.code() + stationeryRes.body());
                if (stationeryRes.code() == 200) {
                    allStationery = stationeryRes.body();

                    GenericAdapter<Stationery> adapter = new GenericAdapter<>(getApplicationContext(),R.layout.activity_discrepency_list_rows,allStationery);
                    ListView listView=findViewById(R.id.vs_listView);

                    if(listView!=null){
                        listView.setAdapter(adapter);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<Stationery>> callStn, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to get Inventory", Toast.LENGTH_LONG);

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, StoreClerkHomePageActivity.class);
        startActivity(intent);
    }
}