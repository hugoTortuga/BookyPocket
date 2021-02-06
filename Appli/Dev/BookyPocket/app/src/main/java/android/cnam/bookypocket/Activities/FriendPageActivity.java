package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Reader;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FriendPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchView searchView;

    private ListView found_list;
    public List<Book> books;

    public TextView name_friend_page;
    public Reader friend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);

        friend = getIntent().getParcelableExtra("friend");
        if(friend == null){
            Alert.ShowDialog(this, "Anomalie", "Aucun ami n'a été trouvé");
        }
        else{
            found_list = (ListView) findViewById(R.id.found_list);
            name_friend_page = (TextView) findViewById(R.id.name_friend_page);

            updateListInterface();
        }

    }

    public void updateListInterface(){

        if(friend != null){
            try{
                if(name_friend_page != null)
                    name_friend_page.setText(friend.getFirstName() + " " + friend.getLastName());
                books = DataBaseSingleton.GetDataBaseSingleton(this).getListBookFromIdUser(friend.getId());
            }
            catch(Exception ex){
                Alert.ShowDialog(this,"Erreur", "" + ex);
            }
            if(books == null)
                books = new ArrayList<>();

            CustomBookAdapter ca = new CustomBookAdapter(this, (ArrayList<Book>) books);
            found_list.setAdapter(ca);
            found_list.setOnItemClickListener(this);
        }

    }

    public void goBackFriendPage(View view) {
        this.finish();
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
