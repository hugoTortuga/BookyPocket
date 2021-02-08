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

/**
 * Acitivté regroupant les lectures de l'uilisateur courant
 */
public class ReadingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    //Attributs
    private List<Book> books;
    private ListView found_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
        initializeForm();
    }

    /**
     * Initialise les composants de la vue
     */
    private void initializeForm(){
        found_list = (ListView) findViewById(R.id.found_list_my_readings);
        try{
            Reader reader = Session.getCurrentUser();
            if(reader == null){
                Alert.ShowDialog(this, "Anomalie", "L'utilisateur courant est vide");
                return;
            }

            books = DataBaseSingleton.GetDataBaseSingleton(this).getListBookFromIdUser(reader.getId());
            updateListInterface();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Bouton retour click
     * @param view
     */
    public void GoHome(View view) {
        ChangeActivity.ChangeActivity(this, MainActivity.class);
    }

    /**
     * redirige vers l'activité scan book
     * @param view
     */
    public void scanBook(View view) {
        ChangeActivity.ChangeActivity(this, BarCodeReaderActivity.class);
    }

    /**
     * redirige vers l'activité ajouter un livre manuellement
     * @param view
     */
    public void addManuallyBook(View view) {
        ChangeActivity.ChangeActivity(this, RegisterBookActivity.class);
    }

    /**
     * Met à jour la liste de livres dans l'interface
     */
    public void updateListInterface(){
        CustomBookAdapter ca = new CustomBookAdapter(this, (ArrayList<Book>) books);
        found_list.setAdapter(ca);
        found_list.setOnItemClickListener(this);
    }

    /**
     * redirige vers l'activité détail livre
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