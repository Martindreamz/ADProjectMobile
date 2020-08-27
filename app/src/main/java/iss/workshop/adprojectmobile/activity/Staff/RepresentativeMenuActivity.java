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
import iss.workshop.adprojectmobile.activity.ConfirmDisbursementCollectionActivity;

public class RepresentativeMenuActivity extends AppCompatActivity implements View.OnClickListener {
    Button confirmDisbursement, raiseRequest, findRoutes;

    SharedPreferences session;
    SharedPreferences.Editor session_editor;
    String currRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_menu);

        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
        currRole = session.getString("role", null);
        if (currRole.equals("STAFF")) {
            confirmDisbursement = findViewById(R.id.confirmDisbursementBtn);
            confirmDisbursement.setVisibility(View.GONE);
        }
        raiseRequest = findViewById(R.id.raiseBtn);
        raiseRequest.setOnClickListener(this);
        if (confirmDisbursement != null)
            registerForContextMenu(confirmDisbursement);

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
            inflater.inflate(R.menu.representative_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collection:
                Intent intent = new Intent(this, ConfirmDisbursementCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.collectionPoint:
                Intent intent2 = new Intent(this, CollectionPointLocationsActivity.class);
                startActivity(intent2);
                break;
            case R.id.distribution:
                Intent intent3 = new Intent(this, ConfirmDisbursementDistributionActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, RaiseRequestActivity.class);
        startActivity(intent);
    }
}