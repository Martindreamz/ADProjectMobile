package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import iss.workshop.adprojectmobile.R;

public class MainActivity extends AppCompatActivity {

    Button findRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findRoutes = (Button) findViewById(R.id.findRoutesBtn);

        findRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindRoutesActivity.class);
                startActivity(intent);
            }
        });
    }

//    trying to make asure i can push

}