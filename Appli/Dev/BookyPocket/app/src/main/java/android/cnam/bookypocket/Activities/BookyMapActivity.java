package android.cnam.bookypocket.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.API.API_GooglePlaces;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Library;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class BookyMapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, LocationListener {

    private GoogleMap mMap = null;

    //coord
    private double latitude;
    private double longitude;

    //unique id for requestCode
    private static final int PERMS_CALL_ID = 1234;
    //service from Android platform
    private LocationManager locationManager;

    private SupportMapFragment mapFragment;

    private List<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booky_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_map);


        if (mapFragment != null)
            mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //if we are activating requestCode permission, for the first time
        if(requestCode == PERMS_CALL_ID){
            checkPermissions();
        }
    }

    private void checkPermissions() {
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
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            //this == Activity that we want to be notified of changed GPS value
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //this == Activity that we want to be notified of changed GPS value
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }

        //if permissions are allowed
        loadMap();
    }

    private void loadMap() {
        //notify when all necessary will be done
        BookyMapActivity c = this;
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            //method is called in checkPermission, we don't need to check it again
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //capture the map
                c.mMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));

                //requires permissions (checkPermission())
                googleMap.setMyLocationEnabled(true);


            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{

            mMap = googleMap;

            //when a marker is clicked
            mMap.setOnMarkerClickListener(this);

            //open info window
            mMap.setOnInfoWindowClickListener(this);

//            googleMap.addMarker(new MarkerOptions().position(new LatLng(BookyMapActivity.this.latitude, BookyMapActivity.this.latitude))
//                    .title("Me"));
            CallGoogleMapsAPI task = new CallGoogleMapsAPI(this);
            task.execute();

            //List<Library> libraryList = API_GooglePlaces.Request(0,0,this);
            //List<Library> libraryList2 = DataBaseSingleton.GetDataBaseSingleton(this).getAllLibrary();

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void setMarkers(List<Library> libs){

        for (Library lib: libs ) {
            LatLng position = new LatLng(lib.getLat(), lib.getLong());

            MarkerOptions markerOptions;
            //marker is green if library is opened, otherwise it's red
            if(lib.isOpened()){
                markerOptions = new MarkerOptions().position(position).title(lib.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                //mMap.addMarker(new MarkerOptions().position(position).title(lib.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                markerOptions = new MarkerOptions().position(position).title(lib.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                //mMap.addMarker(new MarkerOptions().position(position).title(lib.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(API_GooglePlaces.WhereAmI(), 12));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();

            marker.showInfoWindow();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        //Toast.makeText(this, "Location : " + this.latitude + " ; " + this.longitude, Toast.LENGTH_LONG).show();

        //loadMap is asynchronous, better to test if map is null
        if(mMap != null){
            LatLng googleLocation = new LatLng(this.latitude, this.longitude);

            mMap.addMarker(new MarkerOptions().position(new LatLng(BookyMapActivity.this.latitude, BookyMapActivity.this.latitude))
                    .title("Me"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
        }
    }

    private class CallGoogleMapsAPI extends AsyncTask<Void, Void, String> {

        private List<Library> libraries;
        private BookyMapActivity it;
        private ProgressDialog dialog;

        public CallGoogleMapsAPI(BookyMapActivity _it) {
            super();
            it = _it;
            dialog = new ProgressDialog(_it);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Chargement...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                Looper.prepare();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            libraries = new ArrayList<Library>();
            try {
                libraries = API_GooglePlaces.Request(0,0, it);
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'appel à l'api Google Maps", "" + ex);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                dialog.dismiss();
                setMarkers(libraries);
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'affichage des données", "" + ex);
            }
        }
    }
}