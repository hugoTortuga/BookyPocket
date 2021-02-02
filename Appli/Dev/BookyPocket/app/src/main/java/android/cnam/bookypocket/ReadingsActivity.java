package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.ListActivity;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.cnam.bookypocket.ui.ReadingsListAdapter;
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
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageButton itemImageButton;

    //image button will change
    //private Drawable addIcon = getResources().getDrawable( R.drawable.plus );
    //private Drawable removeIcon = getResources().getDrawable( R.drawable.minus );


    private ArrayList<String> titleList = new ArrayList<>(
            Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));

    private ListView readingsList;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
/*
        readingsList = (ListView) findViewById(R.id.readings_list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
        readingsList.setAdapter(adapter);

        createList();*/
    }

    public void addItems(View v) {
        adapter.add("New Item");
    }

    /**
     * Create listAdapter
     */
    private void createList(){
//        ArrayList<Book> books = Book.getAll();
//        if(books != null){
//            ListView readingsList = (ListView) findViewById(R.id.readings_list);
//            ReadingsListAdapter adapter = new ReadingsListAdapter(this, books);
//        }
    }

    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    public void searchBook(View view) {
    }

    public void addBookToMyReading(View view) {
        ChangeActivity.ChangeActivity(this, RegisterBookActivity.class);
    }
}