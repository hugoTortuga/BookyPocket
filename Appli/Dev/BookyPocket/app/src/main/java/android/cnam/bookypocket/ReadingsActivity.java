package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.ListActivity;
import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Intent;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    private List<Book> books;
    private ListView found_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
        found_list = (ListView) findViewById(R.id.found_list_my_readings);


        try{
            Reader reader = Session.getCurrentUser();
            if(reader == null){
                Alert.ShowDialog(this, "Anomalie", "L'utilisateur courant est vide");
                return;
            }

            ORMSQLiteManager manager = new ORMSQLiteManager(this);
            books = manager.getListFromBook(reader.getId());
            updateListInterface();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }


    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    public void searchBook(View view) {
    }

    public void addBookToMyReading(View view) {
        ChangeActivity.ChangeActivity(this, RegisterBookActivity.class);
    }

    public void updateListInterface(){
        CustomAdapter ca = new CustomAdapter(this, (ArrayList<Book>) books);
        found_list.setAdapter(ca);
        found_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        Book bookToSend = books.get(position);
        intent.putExtra("book", bookToSend);
        this.startActivity(intent);
    }
}