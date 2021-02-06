package android.cnam.bookypocket.Activities;

import androidx.fragment.app.FragmentActivity;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Library;
import android.cnam.bookypocket.R;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class BookyMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booky_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_map);

        if(mapFragment != null)
            mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            List<Library> libraryList = DataBaseSingleton.GetDataBaseSingleton(this).getAllLibrary();

            mMap = googleMap;
            for (Library lib: libraryList ) {
                LatLng position = new LatLng(lib.getLat(), lib.getLong());
                mMap.addMarker(new MarkerOptions().position(position).title(lib.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}