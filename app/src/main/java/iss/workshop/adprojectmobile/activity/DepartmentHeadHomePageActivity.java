package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import iss.workshop.adprojectmobile.R;

public class DepartmentHeadHomePageActivity extends AppCompatActivity implements View.OnClickListener {
Button delegateAuthority, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_head_home_page);
        delegateAuthority=findViewById(R.id.delegateAuthorityBtn);
        logoutBtn=findViewById(R.id.logOutBtn);
        delegateAuthority.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.delegateAuthorityBtn){
            Intent intent=new Intent(this, DelegateAuthorityActivity.class);
            startActivity(intent);
        }
    }
}