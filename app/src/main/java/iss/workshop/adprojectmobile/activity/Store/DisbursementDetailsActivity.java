package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.Staff.RepresentativeMenuActivity;
import iss.workshop.adprojectmobile.adapters.DisbursementDetailAdapter;
import iss.workshop.adprojectmobile.model.DisbursementDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisbursementDetailsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;
    private ListView ddTable;
    private List<DisbursementDetail> ddList;
    private TextView title;
    private TextView rep;
    private Button back;

    public List<DisbursementDetail> getDdList() {
        return ddList;
    }

    public void setDdList(List<DisbursementDetail> ddList) {
        this.ddList = ddList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_details);
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        ddTable = findViewById(R.id.DDtable);
        ddList = new ArrayList<>();
        title = findViewById(R.id.DDtitle);
        rep = findViewById(R.id.DDrep);
        back = findViewById(R.id.DDback);
        back.setOnClickListener(this);

        title.setText(session.getString("selected_dl_dept", "not found"));
        rep.setText(session.getString("selected_dl_rep", "not found"));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<DisbursementDetail>> call = apiInterface.getAllDisbursementDetail(session.getInt("selected_dl", 0));

        call.enqueue(new Callback<List<DisbursementDetail>>() {
            @Override
            public void onResponse(Call<List<DisbursementDetail>> call, Response<List<DisbursementDetail>> response) {
                System.out.println(response.code());
                if (response.code() == 200) {
                    ddList = response.body();
                    for(DisbursementDetail dd:ddList){
                        System.out.println(dd);
                    }
                    setDdList(ddList);
                    DisbursementDetailAdapter ddAdapter = new DisbursementDetailAdapter(getApplicationContext(), R.layout.activity_disbursement_details_rows, ddList);
                    ddTable.setAdapter(ddAdapter);
                    ddTable.setOnItemClickListener(ddTable.getOnItemClickListener());
                }
            }

            @Override
            public void onFailure(Call<List<DisbursementDetail>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Down", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DisbursementListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.DDback){
            Intent intent=new Intent(this, SignaturePadActivity.class);
            startActivity(intent);

        }

    }
}