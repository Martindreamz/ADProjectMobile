package iss.workshop.adprojectmobile.activity.Store;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.model.DisbursementDetail;
import iss.workshop.adprojectmobile.model.DisbursementList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignaturePadActivity extends Activity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;
    private int dlId;
    private String dlStatus;
    private String dlDept;
    private String dlRep;
    private DisbursementList disbursementlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_singnature_pad);
        Intent intent = getIntent();
        dlId = intent.getIntExtra("selected_dl", 0);
        dlDept = intent.getStringExtra("selected_dl_dept");
        dlRep = intent.getStringExtra("selected_dl_rep");
        dlStatus = intent.getStringExtra("selected_dl_status");


        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(SignaturePadActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }

        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }

        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                //add code here to add bitmap to database
                String img = BitMapToString(signatureBitmap);
//                Toast.makeText(SignaturePadActivity.this, img, Toast.LENGTH_SHORT).show();

                DisbursementList DL = new DisbursementList(dlId, 0, "", "", "Completed", "");
                DL.setBitmap(img);
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiInterface.url)
                        .client(SSLBypasser.getUnsafeOkHttpClient().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<DisbursementList> call = apiInterface.updateDisbursementBitmap(img,dlId);

                call.enqueue(new Callback<DisbursementList>() {
                    @Override
                    public void onResponse(Call<DisbursementList> call, Response<DisbursementList> response) {
                        System.out.println(response.code());
                        System.out.println(response.errorBody());
                        System.out.println(response.message());
                        if (response.code() == 200) {
                            disbursementlist = response.body();
                            Intent intent = new Intent(getApplicationContext(), DisbursementDetailsActivity.class);
                            intent.putExtra("selected_dl", disbursementlist.getId());
                            intent.putExtra("selected_dl_dept", disbursementlist.getDepartment());
                            intent.putExtra("selected_dl_rep", disbursementlist.getRepName());
                            intent.putExtra("selected_dl_status", disbursementlist.getStatus());
                            intent.putExtra("selected_dl_bitmap", disbursementlist.getBitmap());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<DisbursementList> call, Throwable t) {

                    }
                });

            }
        });
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DisbursementListActivity.class);
        startActivity(intent);
    }

}