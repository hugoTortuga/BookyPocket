package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FriendPageActivity extends AppCompatActivity {

    private SearchView searchView;

    private ListView found_list;
    public List<Book> books_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);



        found_list = (ListView) findViewById(R.id.found_list);
    }

    public void GoBackFriendPage(View view) {
        this.finish();
    }

}
