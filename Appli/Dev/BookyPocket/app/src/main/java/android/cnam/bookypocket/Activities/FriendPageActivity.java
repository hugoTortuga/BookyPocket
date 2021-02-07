package android.cnam.bookypocket.Activities;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Page ami avec ses livres
 */
public class FriendPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Attribut
    private ListView found_list;
    public List<Book> books;
    public TextView name_friend_page;
    public Reader friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);
        initializeView();
    }

    /**
     * initialise les éléments de la vue
     */
    private void initializeView(){
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

    /**
     * Affiche le nom prénom et les livres de l'ami dans la vue
     */
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

    /**
     * Bouton retour click
     * @param view
     */
    public void goBackFriendPage(View view) {
        this.finish();
    }

    /**
     * Click sur un livre, redirection vers la page détail livre
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            Book bookToSend = books.get(position);
            ChangeActivity.GoToBookDetailActivity(this,bookToSend);
        }catch (Exception ex){
            Alert.ShowError(this, "Erreur", "" + ex);
        }
    }

}
