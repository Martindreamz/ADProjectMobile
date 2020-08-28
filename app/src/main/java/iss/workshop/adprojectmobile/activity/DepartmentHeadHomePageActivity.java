package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import iss.workshop.adprojectmobile.R;

public class DepartmentHeadHomePageActivity extends AppCompatActivity implements View.OnClickListener {
    Button delegateAuthority, logoutBtn;
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_head_home_page);
        delegateAuthority = findViewById(R.id.delegateAuthorityBtn);
        logoutBtn = findViewById(R.id.logOutBtn);
        delegateAuthority.setOnClickListener(this);
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.delegateAuthorityBtn) {
            Intent intent = new Intent(this, DelegateAuthorityActivity.class);
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