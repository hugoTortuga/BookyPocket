package android.cnam.bookypocket;

import android.cnam.bookypocket.DBManager.ORMSQLiteManager;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AuthorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView nameAuthor;

    private ListView found_list;
    public List<Book> books_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        nameAuthor = (TextView) findViewById(R.id.idNomAuthor);
        try{
            String authorParam = getIntent().getStringExtra("author");
            nameAuthor.setText(authorParam);
            if(!StringUtil.IsNullOrEmpty(authorParam)){
                ORMSQLiteManager manager = new ORMSQLiteManager(this);
                books_list = manager.getBooksByAuthorArtistName(authorParam);
                found_list = (ListView) findViewById(R.id.found_list_book_author);
                CustomAdapter ca = new CustomAdapter(this, (ArrayList<Book>) books_list);
                found_list.setAdapter(ca);
                found_list.setOnItemClickListener(this);
            }

        }
        catch (Exception ex){
            Alert.ShowError(this, "Erreur" ,""+ ex);
        }

    }



    public void GoBackActivity(View view) {
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            Intent intent = new Intent(this, BookDetailsActivity.class);
            Book bookToSend = books_list.get(position);
            intent.putExtra("book", bookToSend);
            if(nameAuthor != null){
                intent.putExtra("author", nameAuthor.getText());
            }
            this.startActivity(intent);
        }
        catch (Exception ex){
            Alert.ShowDialog(this, "Erreur lors du changement de page", "" + ex);
        }
    }
}
