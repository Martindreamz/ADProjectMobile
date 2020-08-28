package iss.workshop.adprojectmobile.activity.Staff;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adprojectmobile.R;
import iss.workshop.adprojectmobile.Interfaces.ApiInterface;
import iss.workshop.adprojectmobile.Interfaces.SSLBypasser;
import iss.workshop.adprojectmobile.activity.DepartmentHeadHomePageActivity;
import iss.workshop.adprojectmobile.model.CollectionInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

public class CollectionPointLocationsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    //for retrieving collection info
    private SharedPreferences collectionInfo_pref;
    private SharedPreferences.Editor collectionInfo_editor;
    private SharedPreferences session;
    private SharedPreferences.Editor session_editor;
    private static List<CollectionInfo> collectionInfo;

    public static List<CollectionInfo> getCollectionInfo() {
        return collectionInfo;
    }

    //get the spinner from the xml
    Spinner selectCollectionPoint;

    //create a list of items for the spinner
    final List<String> spinnerArray = new ArrayList<String>();

    //coordinates list
    List<Coordinate> coordinateArray = new ArrayList<Coordinate>();

    //google map object
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;

    //declaring coordinate object
    class Coordinate {
        private double v;
        private double v1;

        public Coordinate(double v, double v1) {
            this.v = v;
            this.v1 = v1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_point_locations);

        //retrieving collection info
        collectionInfo_pref = getSharedPreferences("collectioninfo", MODE_PRIVATE);
        collectionInfo_editor = collectionInfo_pref.edit();
        collectionInfo = new ArrayList();
        session = getSharedPreferences("session", MODE_PRIVATE);
        session_editor = session.edit();

        //setting button to view
        selectCollectionPoint = findViewById(R.id.selectCollectionPoint);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.url)
                .client(SSLBypasser.getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //getting all collection info to be processed
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<CollectionInfo>> call = apiInterface.getAllCollectionPointforDept();

        call.enqueue(new Callback<List<CollectionInfo>>() {
            @Override
            public void onResponse(Call<List<CollectionInfo>> call, Response<List<CollectionInfo>> response) {
                System.out.println("Response here: " + response.code());
                collectionInfo = response.body();

                for (CollectionInfo cInfo : collectionInfo) {
                    Log.d("Collection Point: ", cInfo.getCollectionPoint());
                    if (cInfo != null) {
                        spinnerArray.add(cInfo.getCollectionPoint());
                        coordinateArray.add(new Coordinate(Double.parseDouble(cInfo.getLat()), Double.parseDouble(cInfo.getLongi())));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCollectionPoint.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CollectionInfo>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });

        //request location permission
        requestPermission();

        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location (physical device)
    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation = location;

                LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());

                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, 16f);
                //mMap.animateCamera(cameraUpdate);
            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end = latLng;

                mMap.clear();

                start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                //start route finding
                Findroutes(start, end);
            }
        });

        selectCollectionPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                double lat = coordinateArray.get(position).v;
                double lng = coordinateArray.get(position).v1;

                if (item != null) {

                    mMap.clear();

                    if (myLocation != null) {
                        start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        end = new LatLng(lat, lng);
                        //start route finding
                        Findroutes(start, end);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationPermission) {
            getMyLocation();
        }

    }


    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(CollectionPointLocationsActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyAHNMTRIbyTlFWugBR81KZYK33G3eZl1zA")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(CollectionPointLocationsActivity.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start, end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start, end);

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, RepresentativeMenuActivity.class);
        startActivity(intent);
    }
}
