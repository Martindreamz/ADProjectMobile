package iss.workshop.adprojectmobile.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.adapters.RetrievalAdapter;
import iss.workshop.adprojectmobile.model.Requisition;
import iss.workshop.adprojectmobile.model.RequisitionDetail;
import iss.workshop.adprojectmobile.model.Stationery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationeryRetrievalActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private SharedPreferences stationeries_pref;
    private SharedPreferences.Editor stationeries_editor;
    private Button submit;
    private List<RequisitionDetail> requisitionDetails;
    private static List<Stationery> stationeries;
    private ListView RetrievalTable;
    private boolean fetch_completed;
    private static HashMap<Integer, Integer> changes;
    private static HashMap<Integer, Integer> qtyList;

    public static HashMap<Integer, Integer> getChanges() {
        return changes;
    }

    public static void setChanges(HashMap<Integer, Integer> changes) {
        StationeryRetrievalActivity.changes = changes;
    }

    public static List<Stationery> getStationeries() {
        return stationeries;
    }

    public static void setStationeries(List<Stationery> stationeries) {
        StationeryRetrievalActivity.stationeries = stationeries;
    }

    public static HashMap<Integer, Integer> getCountList() {
        return qtyList;
    }

    public static void setCountList(HashMap<Integer, Integer> countList) {
        StationeryRetrievalActivity.qtyList = countList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationery_retrieval);

        stationeries_pref = getSharedPreferences("stationeries", MODE_PRIVATE);
        stationeries_editor = stationeries_pref.edit();
        requisitionDetails = new ArrayList();
        stationeries = new ArrayList();
        qtyList = new HashMap<>();
        changes = new HashMap<>();

        submit = findViewById(R.id.retrivalSubtBtn);


        submit.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //getting all requisitions to be processed
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<RequisitionDetail>> call = apiInterface.getPendingRequisition();

        call.enqueue(new Callback<List<RequisitionDetail>>() {
            @Override
            public void onResponse(Call<List<RequisitionDetail>> call, Response<List<RequisitionDetail>> response) {
                requisitionDetails = response.body();

                List<RequisitionDetail> requisitionsToTest = new ArrayList<>();

                for (RequisitionDetail rd : requisitionDetails) {
                    requisitionsToTest.add(rd);
                    changes.put(rd.getId(), 0);
                }

                if (requisitionDetails != null) {
                    fetch_completed = true;
                    if (fetch_completed) {

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiInterface.url)
                                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        //getting all stationeries to be processed
                        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                        Call<List<Stationery>> call2 = apiInterface.getAllStationery();


                        call2.enqueue(new Callback<List<Stationery>>() {
                            @Override
                            public void onResponse(Call<List<Stationery>> call, Response<List<Stationery>> response) {
                                stationeries = response.body();
                                setStationeries(stationeries);
                                System.out.println(response.code());
                                for (RequisitionDetail rd : requisitionDetails) {
                                    for (Stationery s : stationeries) {
                                        qtyList.put(s.getId(), s.getInventoryQty());
                                        int i = s.getId();
                                        int j = rd.getStationeryId();
                                        if (s.getId() == rd.getStationeryId()) {
                                            rd.setStationery(s.getDesc());
                                            break;
                                        }
                                    }
                                }
                                RetrievalAdapter adapter = new RetrievalAdapter(getApplicationContext(), R.layout.activity_stationery_retrieval_item, requisitionDetails);
                                RetrievalTable = findViewById(R.id.retrievaltable);
                                if (RetrievalTable != null) {
                                    RetrievalTable.setAdapter(adapter);
                                    RetrievalTable.setOnItemClickListener(RetrievalTable.getOnItemClickListener());
                                }
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
                System.out.println(t.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public static void getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            int sum = 0;

            for (int i : changes.values()) {
                sum += i;
            }

            if (sum != 0) {
                List<RequisitionDetail> rdl_tosend = new ArrayList<>();
                for (RequisitionDetail rd : requisitionDetails) {
                    System.out.println(rd);
                }
                for (int i : changes.keySet()) {
                    System.out.println("RD " + i + " needed " + changes.get(i));
                    rdl_tosend.add(new RequisitionDetail(i, 0, 0, changes.get(i), 0, "", "", ""));
                }

                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl(ApiInterface.url)
                        .client(SSLBypasser.getUnsafeOkHttpClient().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface2 = retrofit2.create(ApiInterface.class);

                Call<List<RequisitionDetail>> call2 = apiInterface2.processRetrieval(rdl_tosend);

                call2.enqueue(new Callback<List<RequisitionDetail>>() {
                    @Override
                    public void onResponse(Call<List<RequisitionDetail>> call2, Response<List<RequisitionDetail>> response) {
                        List<RequisitionDetail> requisitions2 = response.body();
                        System.out.println(response.code());
                        if (requisitions2 != null) {
                            for (RequisitionDetail rd : requisitions2)
                                System.out.println(rd);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RequisitionDetail>> call2, Throwable t) {
                        System.out.println("error connecting: " + t.getMessage());
                    }
                });
            }
        }
    }
}