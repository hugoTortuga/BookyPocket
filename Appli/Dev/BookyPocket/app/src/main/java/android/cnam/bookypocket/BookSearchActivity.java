package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.ui.ReadingsListAdapter;
import android.cnam.bookypocket.utils.Alert;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class BookSearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageButton itemImageButton;



    //image button will change
    //private Drawable addIcon = getResources().getDrawable( R.drawable.plus );
    //private Drawable removeIcon = getResources().getDrawable( R.drawable.minus );


    private ArrayList<String> titleList = new ArrayList<>(
            Arrays.asList("Buenos Aires", "Córdoba", "La Plata","Buenos Aires", "Córdoba", "La Plata",
                    "Buenos Aires", "Córdoba", "La Plata","Buenos Aires", "Córdoba", "La Plata",
                    "Buenos Aires", "Córdoba", "La Plata","Buenos Aires", "Córdoba", "La Plata"));

    private ListView found_list;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);


        found_list = (ListView) findViewById(R.id.found_list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
        found_list.setAdapter(adapter);
        Alert.ShowDialog(this,"found list",""+found_list.getCount());
    }

    public void searchBook(View view){
        try{
            if(Session.isUserConnectedToInternet()){
                Alert.ShowDialog(this, "Recherche",
                        "x résultats");
            }
            else{
                Alert.ShowDialog(this, "Pas de connexion internet",
                        "La recherche de livre est impossible sans connexion internet");
            }

        }catch(Exception ex){
            Alert.ShowError(this, "Le recherche de livre a provoqué une erreur",
                    "" + ex);
        }
    }

}


