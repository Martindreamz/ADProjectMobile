package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.adapters.InventoryCheckAdapter;
import iss.workshop.adprojectmobile.model.Stationery;
import iss.workshop.adprojectmobile.model.StockAdjustmentDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class InventoryCheckActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener,SearchView.OnQueryTextListener {
    Button saveInvtCheckBtn, discripencyListBtn;
   // EditText invtSearch;
    private static List<Stationery> stationeries;
    private boolean fetch_completed;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private InventoryCheckAdapter adapter;
    private static HashMap<Integer,Integer> changes;

    public static HashMap<Integer, Integer> getChanges() {
        return changes;
    }

    public static void setChanges(HashMap<Integer, Integer> changes) {
        InventoryCheckActivity.changes = changes;
    }

    public static List<Stationery> getStationeries() {
        return stationeries;
    }

    public void setStationeries(List<Stationery> stationeries) {
        this.stationeries = stationeries;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_check);
        saveInvtCheckBtn = findViewById(R.id.saveInvtCheckBtn);
        saveInvtCheckBtn.setOnClickListener(this);

        stationeries = new ArrayList();
        changes=new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //get all stationery
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Stationery>> call = apiInterface.getAllStationery();

        call.enqueue(new Callback<List<Stationery>>() {
            @Override
            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                stationeries = response.body();

                if (stationeries != null) {
                    System.out.println("Stationeries recieved!"+stationeries);
                    for(Stationery s :stationeries){
                        s.setReOrderQty(s.getInventoryQty());
                        changes.put(s.getId(),s.getReOrderQty());
                    }
                    setChanges(changes);
                    setStationeries(stationeries);
                    fetch_completed = true;

                    adapter = new InventoryCheckAdapter(getApplicationContext(), R.layout.activity_inventory_check_rows, stationeries);
                    ListView listView = findViewById(R.id.StationeriesListView);

                    if (listView != null) {

                      //  ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.headers, listView,false);
                        //listView.addHeaderView(headerView);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(listView.getOnItemClickListener());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Stationery>> call, Throwable t) {

            }

        });


    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InventoryCheckActivity.this.finish();
                //InventoryCheckActivity.this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
                //ActivityUtils.hideSoftKeyboard(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.saveInvtCheckBtn) {
          //post and send data to get stock adjustment detail
            //Intent intent = new Intent(this, DiscrepencyListActivity.class);
//            startActivity(intent);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.url)
                    .client(SSLBypasser.getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            Call<List<StockAdjustmentDetail>> call=apiInterface.updateInventory(stationeries);
            call.enqueue(new Callback<List<StockAdjustmentDetail>>() {
                @Override
                public void onResponse(Call<List<StockAdjustmentDetail>> call, Response<List<StockAdjustmentDetail>> response) {
                    System.out.println("LOOK HERE"+response.body());
                    if (response.code() == 200) {
                        List<StockAdjustmentDetail> sads = response.body();
                        if(response.body().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Inventory Updated with no discrepencies", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ViewInventoryActivity.class);
                            startActivity(intent);
                        }
                        else if(sads!=null) {
                            Intent intent = new Intent(getApplicationContext(), DiscrepencyListActivity.class);
                            intent.putExtra("sads", (Serializable) sads);
                            System.out.println(sads);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<StockAdjustmentDetail>> call, Throwable t) {

                }
            });



        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StoreClerkHomePageActivity.class);
        startActivity(intent);
    }
}