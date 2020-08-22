package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import iss.workshop.adprojectmobile.R;

public class RepresentativeMenuActivity extends AppCompatActivity implements View.OnClickListener {
Button confirmDisbursement, raiseRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_menu);
        confirmDisbursement=findViewById(R.id.confirmDisbursementBtn);
        raiseRequest=findViewById(R.id.raiseBtn);
        raiseRequest.setOnClickListener(this);
        if(confirmDisbursement!=null)
            registerForContextMenu(confirmDisbursement);

    }
@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if(v.getId()==R.id.confirmDisbursementBtn){
            super.onCreateContextMenu(menu,v,menuInfo);
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.representative_menu,menu);
        }
    }
  @Override
  public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.collection:
                Intent intent=new Intent(this, ConfirmDisbursementCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.collectionPoint:
                Intent intent2=new Intent(this, CollectionPointLocationsActivity.class);
                startActivity(intent2);
                break;
            case R.id.distribution:
                Intent intent3=new Intent(this, ConfirmDisbursementDistributionActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onContextItemSelected(item);
  }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, RaiseRequestActivity.class);
        startActivity(intent);
    }
}