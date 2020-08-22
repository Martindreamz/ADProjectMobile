package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.Api;
import iss.workshop.adprojectmobile.Interfaces.RestAdapter;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.Requisition;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationeryRetrievalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationery_retrieval);

//        String URL = "https://10.0.2.2:5001/api/dept/retrieval";
//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.GET,
//                URL,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response.toString());
//                    }
//                },
//                new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("cant fetch",error.toString());
//                    }
//                }
//
//        );
//        requestQueue.add(stringRequest);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.url)
                .client(RestAdapter.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Requisition>> call = api.getPendingRequisition();

        call.enqueue(new Callback<List<Requisition>>() {
            @Override
            public void onResponse(Call<List<Requisition>> call, Response<List<Requisition>> response) {
                List<Requisition> requisitions = response.body();
                if(requisitions!=null){
                    for (Requisition r : requisitions) {
                        Log.d("successful", r.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Requisition>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });
    }
}