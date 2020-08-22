package iss.workshop.adprojectmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.R;

public class CollectionPointLocationsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_point_locations);
        spinner = findViewById(R.id.selectCollectionPoint);
        spinner.setOnItemSelectedListener(this);
        //Test Data for spinner
        List<String> collectionPoints = new ArrayList<String>();
        collectionPoints.add("Stationery Store - Administration Building");
        collectionPoints.add("Management School");
        collectionPoints.add("Medical School");
        collectionPoints.add("Engineering School");
        collectionPoints.add("Science School");
        collectionPoints.add("University Hospital");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collectionPoints);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(CollectionPointLocationsActivity.this,item, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}