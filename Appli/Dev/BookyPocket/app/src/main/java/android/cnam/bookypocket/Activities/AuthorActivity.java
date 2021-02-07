package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.R;
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

/**
 * Activité qui affiche les livres d'un auteur
 */
public class AuthorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Attributs
    private TextView nameAuthor;
    private ListView found_list;
    public List<Book> books_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        initializeView();
    }

    /**
     * On initialise les composants de la vue
     */
    private void initializeView(){
        nameAuthor = (TextView) findViewById(R.id.idNomAuthor);
        try{
            String authorParam = getIntent().getStringExtra("author");
            nameAuthor.setText(authorParam);
            if(!StringUtil.IsNullOrEmpty(authorParam)){

                books_list =  DataBaseSingleton.GetDataBaseSingleton(this).getBooksByAuthorArtistName(authorParam);
                found_list = (ListView) findViewById(R.id.found_list_book_author);
                CustomBookAdapter ca = new CustomBookAdapter(this, (ArrayList<Book>) books_list);
                found_list.setAdapter(ca);
                found_list.setOnItemClickListener(this);
            }
        }
        catch (Exception ex){
            Alert.ShowError(this, "Erreur" ,""+ ex);
        }
    }

    /**
     * Bouton retour click
     * @param view
     */
    public void GoBackActivity(View view) {
        this.finish();
    }

    /**
     * Click sur un livre d'un auteur
     * @param parent
     * @param view
     * @param position
     * @param id
     */
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
