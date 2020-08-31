package iss.workshop.adprojectmobile.activity.Staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.activity.LoginActivity;

public class RepresentativeMenuActivity extends AppCompatActivity implements View.OnClickListener {
    Button confirmDisbursement, findRoutes;
Button logout;
    SharedPreferences session;
    SharedPreferences.Editor session_editor;
    String currRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_menu);
        confirmDisbursement = findViewById(R.id.confirmDisbursementBtn);
        logout = findViewById(R.id.logOutBtn);
        logout.setOnClickListener(this);
        if(confirmDisbursement!=null) {
            registerForContextMenu(confirmDisbursement);
        }


        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        currRole = session.getString("role", null);
        if (currRole.equals("STAFF")) {

            confirmDisbursement.setVisibility(View.GONE);
        }

        findRoutes = (Button) findViewById(R.id.findRoutesBtn);
        findRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepresentativeMenuActivity.this, CollectionPointLocationsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.confirmDisbursementBtn) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.disbursement_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disbursementCollection:
                Intent intent = new Intent(this, ConfirmDisbursementCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.disbursementDistribution:
                Intent intent3 = new Intent(this, ConfirmDisbursementDistributionActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.logOutBtn){
            session_editor.clear();
            session_editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        session_editor.putString("username", "");
        session_editor.putString("role", "");
        session_editor.putInt("departmentId", 0);
        session_editor.putInt("staffId", 0);
        session_editor.commit();
        startActivity(intent);
    }
}