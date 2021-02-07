package android.cnam.bookypocket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.cnam.bookypocket.R;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;


import java.util.Collections;

public class BookyMapTestActivity extends AppCompatActivity implements LocationListener {

    //unique id for requestCode
    private static final int PERMS_CALL_ID = 1234;
    //service from Android platform
    private LocationManager locationManager;

    private MapFragment mapFragment;

    private GoogleMap googleMap;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private Place place;

    //coord
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booky_map_test);

        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();

    }

    private void checkPermissions(){
        //Location is not allowed on phone, even if it's active here
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //asynchronous call for user permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }
        //Location_Service is provided by Android
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //if GPS captor is active
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //this == Activity that we want to be notified of changed GPS value
            //every 10 seconds

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
            //this == Activity that we want to be notified of changed GPS value
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0,this);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //this == Activity that we want to be notified of changed GPS value
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,0,  this);
        }

        //if permissions are allowed
        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //if we are activating requestCode permission, for the first time
        if(requestCode == PERMS_CALL_ID){
            checkPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }


    private void loadMap(){
        //notify when all necessary will be done
        BookyMapTestActivity c = this;
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            //method is called in checkPermission, we don't need to check it again
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //capture the map
                c.setGoogleMap(googleMap);
                //googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));

                //requires permissions (checkPermission())
                //googleMap.setMyLocationEnabled(true);

                //googleMap.addMarker(new MarkerOptions().position(new LatLng(BookyMapTestActivity.this.latitude, BookyMapTestActivity.this.longitude)).title("Me"));


                googleMap.addMarker(new MarkerOptions().position(new LatLng(BookyMapTestActivity.this.latitude, BookyMapTestActivity.this.latitude))
                                                        .title("Me"));

            }
        });
    }

    private void getPlaces(){

        // Use fields to define the data types to return.
//        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
//
//        // Use the builder to create a FindCurrentPlaceRequest.
//        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
//
//        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
//        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
//            placeResponse.addOnCompleteListener(task -> {
//                if (task.isSuccessful()){
//                    FindCurrentPlaceResponse response = task.getResult();
//                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
//                                placeLikelihood.getPlace().getName(),
//                                placeLikelihood.getLikelihood()));
//                    }
//                } else {
//                    Exception exception = task.getException();
//                    if (exception instanceof ApiException) {
//                        ApiException apiException = (ApiException) exception;
//                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                    }
//                }
//            });
//        } else {
//            // A local method to request required permissions;
//            // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission();
//        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * When recieve a new location information, map is relocated
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        //Toast.makeText(this, "Location : " + this.latitude + " ; " + this.longitude, Toast.LENGTH_LONG).show();

        //loadMap is asynchronous, better to test if map is null
        if(googleMap != null){
            LatLng googleLocation = new LatLng(this.latitude, this.longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
        }
    }
}