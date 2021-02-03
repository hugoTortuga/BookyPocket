package android.cnam.bookypocket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.DBManager.Session;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
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

            books = DataBaseSingleton.GetDataBaseSingleton(this).getListFromBook(reader.getId());
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
        CustomBookAdapter ca = new CustomBookAdapter(this, (ArrayList<Book>) books);
        found_list.setAdapter(ca);
        found_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        Book bookToSend = books.get(position);
        intent.putExtra("book", bookToSend);
        if(bookToSend.getAuthor() != null){
            intent.putExtra("author", bookToSend.getAuthor().getArtistName());
        }
        this.startActivity(intent);
    }
}