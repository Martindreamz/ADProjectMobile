package iss.workshop.adprojectmobile.activity.Store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import iss.workshop.adprojectmobile.R;

public class InventoryCheckActivity extends AppCompatActivity implements View.OnClickListener{
Button saveInvtCheckBtn, discripencyListBtn;
EditText invtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_check);
        invtSearch=findViewById(R.id.invtSearch);
        saveInvtCheckBtn=findViewById(R.id.saveInvtCheckBtn);
        discripencyListBtn=findViewById(R.id.discripencyListBtn);
        saveInvtCheckBtn.setOnClickListener(this);
        discripencyListBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if(id==R.id.discripencyListBtn){
            Intent intent=new Intent(this, DiscrepencyListActivity.class);
            startActivity(intent);
        }
    }
}