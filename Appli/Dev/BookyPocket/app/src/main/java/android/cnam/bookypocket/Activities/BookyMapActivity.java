package android.cnam.bookypocket.Activities;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.API.API_GooglePlaces;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Library;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
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

            mMap = googleMap;

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
            mMap.addMarker(new MarkerOptions().position(position).title(lib.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
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