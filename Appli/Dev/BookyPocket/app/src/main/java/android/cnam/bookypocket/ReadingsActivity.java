package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.ListActivity;
import android.cnam.bookypocket.ui.ReadingsListAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {
    private MutableLiveData<String> mText;

    private ArrayList<String> titleList = new ArrayList<>(
            Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));

    private ListView readingsList;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;

    public ReadingsActivity() {
        mText = new MutableLiveData<>();
        mText.setValue("This is readings activity");
    }


    public LiveData<String> getText() {
        return mText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);



        readingsList = (ListView) findViewById(R.id.readings_list);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                titleList);
        readingsList.setAdapter(adapter);

        //createList();
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
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

}